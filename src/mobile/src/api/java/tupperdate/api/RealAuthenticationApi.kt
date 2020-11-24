package tupperdate.api

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class RealAuthenticationApi(
    private val activity: AppCompatActivity,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthenticationApi {

    /**
     * The [PhoneAuthProvider.ForceResendingToken] that might be used if the force parameter is
     * requested when sending a new code.
     */
    private val forceToken = MutableStateFlow<PhoneAuthProvider.ForceResendingToken?>(null)

    /**
     * A [String] that will be used to perform phone number verification.
     */
    private val verification = MutableStateFlow<String?>(null)

    override suspend fun requestCode(
        number: String,
        force: Boolean
    ): AuthenticationApi.RequestCodeResult = suspendCoroutine { continuation ->

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            private val isResumed = AtomicBoolean(false)

            private fun resumeOnce(f: () -> Unit) {
                if (isResumed.compareAndSet(false, true)) {
                    f()
                }
            }

            override fun onVerificationCompleted(
                credential: PhoneAuthCredential,
            ) {
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            resumeOnce {
                                continuation.resume(AuthenticationApi.RequestCodeResult.LoggedIn)
                            }
                        } else {
                            resumeOnce {
                                continuation.resume(AuthenticationApi.RequestCodeResult.InternalError)
                                result.exception?.printStackTrace();
                            }
                        }
                    }
            }

            override fun onVerificationFailed(
                problem: FirebaseException,
            ) {
                when (problem) {
                    is FirebaseAuthInvalidCredentialsException -> resumeOnce {
                        continuation.resume(
                            AuthenticationApi.RequestCodeResult.InvalidNumberError
                        )
                    }
                    else -> resumeOnce {
                        problem.printStackTrace();
                        continuation.resume(
                            AuthenticationApi.RequestCodeResult.InternalError
                        )
                    }
                }
            }

            override fun onCodeSent(
                verification: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@RealAuthenticationApi.verification.value = verification
                this@RealAuthenticationApi.forceToken.value = token
                resumeOnce {
                    continuation.resume(AuthenticationApi.RequestCodeResult.RequiresVerification)
                }
            }
        }

        val token = forceToken.value
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .apply {
                if (token != null && force) {
                    setForceResendingToken(token)
                }
            }
            .setActivity(activity)
            .setPhoneNumber(number)
            .setTimeout(25, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .build()
        // TODO: Replace setExecutor if necessary

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun verify(code: String): AuthenticationApi.VerificationResult {
        val verification = this@RealAuthenticationApi.verification.value
        return if (verification == null) {
            AuthenticationApi.VerificationResult.InternalError
        } else {
            suspendCoroutine { continuation ->
                firebaseAuth.signInWithCredential(
                    PhoneAuthProvider.getCredential(
                        verification,
                        code
                    )
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        continuation.resume(AuthenticationApi.VerificationResult.LoggedIn)
                    } else {
                        continuation.resume(AuthenticationApi.VerificationResult.InvalidVerificationError)
                    }
                }
            }
        }
    }

    val auth: Flow<AuthInfo?> = currentUser(firebaseAuth)
            .map { user ->
                val token = user?.getIdToken(false)?.await()?.token
                token?.let { AuthInfo(it) }
            }
            .catch { emit(null) }

    override val connected: Flow<Boolean>
        get() = currentUser(firebaseAuth).map { it != null }

    override val uid: Flow<String?>
        get() = currentUser(firebaseAuth).map { it?.uid }
}

data class AuthInfo(
    val token: String,
)

/**
 * Returns a [Flow] of the current [FirebaseUser], and emits one on every authentication change.
 *
 * @param auth the [FirebaseAuth] that's used to retrieve the user.
 */
@OptIn(ExperimentalCoroutinesApi::class)
private fun currentUser(auth: FirebaseAuth): Flow<FirebaseUser?> {
    return callbackFlow {
        val callback = FirebaseAuth.AuthStateListener { auth ->
            safeOffer(auth.currentUser)
        }
        auth.addAuthStateListener(callback)
        awaitClose { auth.removeAuthStateListener((callback)) }
    }
}

/**
 * Safely offers an item in a [SendChannel]. If the [SendChannel] is closed, no exception will be
 * thrown. This might be useful in scenarios where recomposition happens while an operation is
 * being performed.
 */
fun <T> SendChannel<T>.safeOffer(element: T): Boolean {
    return kotlin.runCatching { offer(element) }.getOrDefault(false)
}
