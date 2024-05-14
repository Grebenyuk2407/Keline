package dev.androidbroadcast.keline.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.androidbroadcast.keline.data.User
import dev.androidbroadcast.keline.util.Constance.USER_COLLECTION
import dev.androidbroadcast.keline.util.RegisterFailedState
import dev.androidbroadcast.keline.util.RegisterValidation
import dev.androidbroadcast.keline.util.Resource
import dev.androidbroadcast.keline.util.validateEmail
import dev.androidbroadcast.keline.util.validatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFailedState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        checkValidation(user, password)
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid, user)
                    }

                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())

                }
        } else{
            val registerFailedState = RegisterFailedState(
                validateEmail(user.email), validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFailedState)
            }
        }
    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)

            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidate = validatePassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidate is RegisterValidation.Success

        return shouldRegister
    }
}