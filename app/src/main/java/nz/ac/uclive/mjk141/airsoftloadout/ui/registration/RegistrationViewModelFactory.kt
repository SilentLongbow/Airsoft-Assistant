package nz.ac.uclive.mjk141.airsoftloadout.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nz.ac.uclive.mjk141.airsoftloadout.database.daos.UserDao

class RegistrationViewModelFactory(private val database: UserDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}