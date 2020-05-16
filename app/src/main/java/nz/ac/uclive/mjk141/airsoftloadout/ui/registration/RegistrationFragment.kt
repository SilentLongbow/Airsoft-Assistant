package nz.ac.uclive.mjk141.airsoftloadout.ui.registration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import nz.ac.uclive.mjk141.airsoftloadout.R

class RegistrationFragment : Fragment() {

    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registrationViewModel =
            ViewModelProvider(this).get(RegistrationViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_registration, container, false)
        return root
    }

}
