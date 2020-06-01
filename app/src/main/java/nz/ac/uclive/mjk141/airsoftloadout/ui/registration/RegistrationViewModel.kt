package nz.ac.uclive.mjk141.airsoftloadout.ui.registration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import nz.ac.uclive.mjk141.airsoftloadout.database.daos.UserDao
import nz.ac.uclive.mjk141.airsoftloadout.database.tables.User
import nz.ac.uclive.mjk141.airsoftloadout.utils.EMPTY_STRING
import nz.ac.uclive.mjk141.airsoftloadout.utils.validEmail
import nz.ac.uclive.mjk141.airsoftloadout.utils.validPassword
import nz.ac.uclive.mjk141.airsoftloadout.utils.validUsernameLength

class RegistrationViewModel(private val database: UserDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> = _userId

    val username = MutableLiveData(EMPTY_STRING)
    val userEmail = MutableLiveData(EMPTY_STRING)
    val userPassword = MutableLiveData(EMPTY_STRING)

    private val _validUsername = MutableLiveData<Boolean>()
    val validUsername: LiveData<Boolean> = _validUsername

    private val _validEmail = MutableLiveData<Boolean>()
    val validEmail: LiveData<Boolean> = _validEmail

    private val _validPassword = MutableLiveData<Boolean>()
    val validPassword: LiveData<Boolean> = _validPassword

    private val _uniqueUsername = MutableLiveData<Boolean>()
    val uniqueUsername: LiveData<Boolean> = _uniqueUsername

    private val _uniqueEmail = MutableLiveData<Boolean>()
    val uniqueEmail: LiveData<Boolean> = _uniqueEmail

    private val _eventValidationFinished = MutableLiveData<Boolean>()
    val eventValidationFinished: LiveData<Boolean> = _eventValidationFinished

    private val _databaseQueried = MutableLiveData<Boolean>()
    val databaseQueried: LiveData<Boolean> = _databaseQueried

    private val _registrationSuccessful = MutableLiveData<Boolean>()
    val registrationSuccessful: LiveData<Boolean> = _registrationSuccessful

    init {
        resetValidFlags()
        resetUniqueFlags()
        _eventValidationFinished.value = false
        _registrationSuccessful.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun validateInput() {
        resetValidFlags()
        resetUniqueFlags()
        eventValidationStarted()
        trimInputs()
        _validUsername.value = validUsernameLength(username.value)
        _validEmail.value = validEmail(userEmail.value)
        _validPassword.value = validPassword(userPassword.value)

        val validationArray = arrayOf(_validUsername.value, _validEmail.value, _validPassword.value)
        if (validationArray.all { it!! }) {
            attemptUserCreation()
        } else {
            finishValidationEvent()
        }
    }

    private fun resetValidFlags() {
        _validUsername.value = false
        _validEmail.value = false
        _validPassword.value = false
    }

    private fun resetUniqueFlags() {
        _uniqueUsername.value = false
        _uniqueEmail.value = false
    }

    private fun eventValidationStarted() {
        _eventValidationFinished.value = false
        _databaseQueried.value = false
    }

    private fun finishValidationEvent() {
        _eventValidationFinished.value = true
    }

    private fun trimInputs() {
        username.value = username.value?.trim()
        userEmail.value = userEmail.value?.trim()
    }

    private fun attemptUserCreation() {
        uiScope.launch {
            selectProfileByUsername()
            selectProfileByEmail()
            _databaseQueried.value = true
            if (_uniqueUsername.value!! && uniqueEmail.value!!) {
                createUser()
            } else {
                finishValidationEvent()
            }
        }
    }


    private suspend fun selectProfileByUsername() {
        val enteredUsername: String = username.value!!
        return withContext(Dispatchers.IO) {
            val existingProfile: Long? = database.selectIdByUsername(enteredUsername.trim())
            val uniqueUsername = existingProfile == null
            _uniqueUsername.postValue(uniqueUsername)
        }
    }

    private suspend fun selectProfileByEmail() {
        val enteredEmail: String = userEmail.value!!
        return withContext(Dispatchers.IO) {
            val existingProfile: Long? = database.selectIdByEmail(enteredEmail.trim())
            val uniqueEmail = existingProfile == null
            _uniqueEmail.postValue(uniqueEmail)
        }
    }


    private fun createUser() {
        uiScope.launch {
            val newUser = User(
                0,
                userEmail.value!!,
                username.value!!,
                userPassword.value!!
            )
            insertUserIntoDatabase(newUser)

        }
    }

    private suspend fun insertUserIntoDatabase(newUser: User) {
        return withContext(Dispatchers.IO) {
            val newUserId = database.insert(newUser)
            _userId.postValue(newUserId)
            _registrationSuccessful.postValue(true)
        }
    }




}
