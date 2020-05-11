package nz.ac.uclive.mjk141.airsoftloadout.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ths is profile Fragment"
    }
    val text: LiveData<String> = _text
}
