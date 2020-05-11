package nz.ac.uclive.mjk141.airsoftloadout.ui.equipment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EquipmentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is equipment Fragment"
    }
    val text: LiveData<String> = _text
}