package nz.ac.uclive.mjk141.airsoftloadout.ui.login
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.view.*

import nz.ac.uclive.mjk141.airsoftloadout.R

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val layout = inflater.inflate(R.layout.fragment_login, container, false)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToSignUp()
        }

        layout.sign_up_clickable_label.setOnClickListener {
            navigateToSignUp()
        }

        return layout
    }

    private fun navigateToSignUp() {
        findNavController().navigate(LoginFragmentDirections.actionLoginToRegistration())
    }

}
