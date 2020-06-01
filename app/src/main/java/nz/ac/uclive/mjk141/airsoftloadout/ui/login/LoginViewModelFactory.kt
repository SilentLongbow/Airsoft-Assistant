package nz.ac.uclive.mjk141.airsoftloadout.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nz.ac.uclive.mjk141.airsoftloadout.database.daos.UserDao
import java.lang.IllegalArgumentException

class LoginViewModelFactory(private val database: UserDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}