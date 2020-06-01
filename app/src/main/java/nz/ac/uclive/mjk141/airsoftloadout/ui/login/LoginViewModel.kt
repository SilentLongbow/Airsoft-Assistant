package nz.ac.uclive.mjk141.airsoftloadout.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import nz.ac.uclive.mjk141.airsoftloadout.database.daos.UserDao
import nz.ac.uclive.mjk141.airsoftloadout.database.tables.User
import nz.ac.uclive.mjk141.airsoftloadout.utils.EMPTY_STRING
import nz.ac.uclive.mjk141.airsoftloadout.utils.validEmail

class LoginViewModel(private var database: UserDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> = _userId

    val email = MutableLiveData(EMPTY_STRING)
    val password = MutableLiveData(EMPTY_STRING)

    private val _validEmail = MutableLiveData<Boolean>()
    val validEmail: LiveData<Boolean> = _validEmail

    private val _correctEmail = MutableLiveData<Boolean>()
    val correctEmail: LiveData<Boolean> = _correctEmail

    private val _correctPassword = MutableLiveData<Boolean>()
    val correctPassword: LiveData<Boolean> = _correctPassword

    private val _validationComplete = MutableLiveData<Boolean>()
    val validationComplete: LiveData<Boolean> = _validationComplete

    private val _loginSuccessful = MutableLiveData<Boolean>()
    val loginSuccessful: LiveData<Boolean> = _loginSuccessful

    init {
        _correctEmail.value = false
        _correctPassword.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Email and password will never be null as they're tied to edit-texts.
     */
    fun checkCredentials() {
        disableValidationCompleteFlag()
        resetLoginFlags()
        email.value = email.value?.trim()
        _validEmail.value = validEmail(email.value)
        if (_validEmail.value == true) {
            checkDatabase()
        } else {
            enableValidationCompleteFlag()
        }
    }

    private fun checkDatabase() {
        uiScope.launch {
            Log.i("LoginViewModel", "Attempting Login")
            attemptLogin(email.value!!, password.value!!)
            Log.i("LoginViewModel", "ValidationComplete")
            enableValidationCompleteFlag()
        }
    }

    private fun resetLoginFlags() {
        _correctEmail.value = false
        _correctPassword.value = false
    }

    private fun enableValidationCompleteFlag() {
        _validationComplete.value = true
    }

    private fun disableValidationCompleteFlag() {
        _validationComplete.value = false
    }

    private suspend fun attemptLogin(email: String, password: String) {
        return withContext(Dispatchers.IO) {
            Log.i("LoginViewModel", "Now in context")
            val foundUser: User? = database.selectByEmail(email)
            if (foundUser == null) {
                _correctEmail.postValue(false)
                _correctPassword.postValue(false)
            } else {
                _correctEmail.postValue(true)
                if (foundUser.password == password) {
                    _correctPassword.postValue(true)
                    _userId.postValue(foundUser.id)
                    _loginSuccessful.postValue(true)
                } else {
                    _correctPassword.postValue(false)
                }
            }
        }
    }
}
