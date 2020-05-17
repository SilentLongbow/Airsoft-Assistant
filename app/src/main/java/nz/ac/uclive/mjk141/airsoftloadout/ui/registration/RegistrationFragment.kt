package nz.ac.uclive.mjk141.airsoftloadout.ui.registration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_registration.view.*
import nz.ac.uclive.mjk141.airsoftloadout.R
import nz.ac.uclive.mjk141.airsoftloadout.ui.login.LoginFragmentDirections

class RegistrationFragment : Fragment() {

    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registrationViewModel =
            ViewModelProvider(this).get(RegistrationViewModel::class.java)

        val layout = inflater.inflate(R.layout.fragment_registration, container, false)
        layout.sign_in_clickable_label.setOnClickListener {
            findNavController().navigate(RegistrationFragmentDirections.actionRegistrationToLogin())
        }


        return layout
    }

}
