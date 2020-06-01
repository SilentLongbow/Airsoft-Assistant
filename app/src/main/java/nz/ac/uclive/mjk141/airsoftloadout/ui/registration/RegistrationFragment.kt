package nz.ac.uclive.mjk141.airsoftloadout.ui.registration
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_registration.*
import nz.ac.uclive.mjk141.airsoftloadout.MainActivity
import nz.ac.uclive.mjk141.airsoftloadout.R
import nz.ac.uclive.mjk141.airsoftloadout.database.LoadoutDatabase
import nz.ac.uclive.mjk141.airsoftloadout.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegistrationBinding
            .inflate(inflater, container, false)

        val application = requireNotNull(activity).application
        val database = LoadoutDatabase.getInstance(application).userDao
        val viewModelFactory = RegistrationViewModelFactory(database)
        sharedPreferences = requireNotNull(activity).getSharedPreferences(
            getString(R.string.stored_user_credentials),
            Context.MODE_PRIVATE
        )

        registrationViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegistrationViewModel::class.java)

        binding.registrationViewModel = registrationViewModel

        registerSignLabelListener(binding)
        registerSignUpButtonListener(binding)
        registerLiveDataObservers()

        return binding.root
    }

    private fun registerSignLabelListener(binding: FragmentRegistrationBinding) {
        binding.signInClickableLabel.setOnClickListener {
            findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationToLogin())
        }
    }

    private fun registerSignUpButtonListener(binding: FragmentRegistrationBinding) {
        binding.signUpButton.setOnClickListener {
            registrationViewModel.validateInput()
        }
    }

    private fun registerLiveDataObservers() {
        registrationViewModel.eventValidationFinished
            .observe(viewLifecycleOwner, Observer { eventValidationFinished: Boolean ->
                if (eventValidationFinished) {
                    invalidFieldAnimationDispatcher()
                }
            })

        registrationViewModel.registrationSuccessful
            .observe(viewLifecycleOwner, Observer { registrationSuccessful: Boolean ->
                if (registrationSuccessful) {
                    setLoggedInUserPreference()
                    navigateToMainActivity()
                }
            })
    }

    private fun invalidFieldAnimationDispatcher() {
        val validUsername = registrationViewModel.validUsername.value ?: false
        val validEmail = registrationViewModel.validEmail.value ?: false
        val validPassword = registrationViewModel.validPassword.value ?: false
        val uniqueUsername = registrationViewModel.uniqueUsername.value ?: false
        val uniqueEmail = registrationViewModel.uniqueEmail.value ?: false

        val databaseChecked = registrationViewModel.databaseQueried.value ?: false

        if (!validUsername) {
            displayIncorrectField(
                username_text_input,
                getString(R.string.invalid_username_length)
            )
        } else if (databaseChecked && !uniqueUsername) {
                displayIncorrectField(
                    username_text_input,
                    getString(R.string.username_exists_message)
                )
            }
        if (!validEmail) {
            displayIncorrectField(
                email_text_input,
                getString(R.string.invalid_email)
            )
        } else if (databaseChecked && !uniqueEmail) {
            displayIncorrectField(
                email_text_input,
                getString(R.string.email_exists_message)
            )
        }
        if (!validPassword) {
            displayIncorrectField(
                password_text_input,
                getString(R.string.invalid_password_length)
            )
        }
    }

    private fun displayIncorrectField(fieldToChange: EditText, errorText: String) {
        val fieldShake = AnimationUtils.loadAnimation(context, R.anim.horizontal_shake)
        fieldToChange.error = errorText
        fieldToChange.startAnimation(fieldShake)
    }

    private fun setLoggedInUserPreference() {
        with (sharedPreferences.edit()) {
            putLong(
                getString(R.string.stored_user_id_key),
                registrationViewModel.userId.value!!
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
