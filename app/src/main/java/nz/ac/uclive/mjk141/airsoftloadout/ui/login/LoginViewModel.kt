package nz.ac.uclive.mjk141.airsoftloadout.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is a login Fragment"
    }
}
