package nz.ac.uclive.mjk141.airsoftloadout.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import nz.ac.uclive.mjk141.airsoftloadout.MainActivity
import nz.ac.uclive.mjk141.airsoftloadout.R
import nz.ac.uclive.mjk141.airsoftloadout.database.LoadoutDatabase
import nz.ac.uclive.mjk141.airsoftloadout.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(activity).application
        val database = LoadoutDatabase.getInstance(application).userDao
        val viewModelFactory = LoginViewModelFactory(database)

        sharedPreferences = requireNotNull(activity).getSharedPreferences(
            requireNotNull(activity).packageName,
            Context.MODE_PRIVATE
        )

        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        val binding = FragmentLoginBinding
            .inflate(inflater, container, false)


        binding.loginViewModel = loginViewModel

        registerBackButtonOverrideEvent()
        registerSignInButtonListener(binding)
        registerSignUpOptionListener(binding)
        registerViewModelObservers()

        return binding.root
    }

    private fun registerBackButtonOverrideEvent() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToSignUp()
        }
    }

    private fun registerSignUpOptionListener(binding: FragmentLoginBinding) {
        binding.signUpClickableLabel.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun registerSignInButtonListener(binding: FragmentLoginBinding) {
        binding.loginButton.setOnClickListener {
            loginViewModel.checkCredentials()
        }
    }

    private fun registerViewModelObservers() {
        loginViewModel.validationComplete
            .observe(viewLifecycleOwner, Observer { validationComplete: Boolean ->
                if (validationComplete) {
                    displayIncorrectFieldAnimations()
                }
            })

        loginViewModel.loginSuccessful
            .observe(viewLifecycleOwner, Observer { loginSuccessful: Boolean ->
                if (loginSuccessful) {
                    setLoggedInUserPreference()
                    navigateToMainActivity()
                }
            })

    }

    private fun displayIncorrectFieldAnimations() {
        val correctEmail: Boolean = loginViewModel.correctEmail.value ?: false
        val validEmail: Boolean = loginViewModel.validEmail.value ?: false
        val correctPassword: Boolean = loginViewModel.correctPassword.value ?: false

        if (!correctEmail || !validEmail) {
            displayIncorrectField(email_text_input, getString(R.string.incorrect_email_text))
        }
        if (!correctPassword) {
            displayIncorrectField(password_text_input, getString(R.string.incorrect_password_text))
        }
    }

    private fun displayIncorrectField(fieldToChange: EditText, errorText: String) {
        val fieldShake = AnimationUtils.loadAnimation(context, R.anim.horizontal_shake)
        fieldToChange.error = errorText
        fieldToChange.startAnimation(fieldShake)
    }

    private fun navigateToSignUp() {
        findNavController().navigate(LoginFragmentDirections.actionLoginToRegistration())
    }

    private fun setLoggedInUserPreference() {
        with (sharedPreferences.edit()) {
            putLong(
                getString(R.string.stored_user_id_key),
                loginViewModel.userId.value!!
            )
            apply()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

}
