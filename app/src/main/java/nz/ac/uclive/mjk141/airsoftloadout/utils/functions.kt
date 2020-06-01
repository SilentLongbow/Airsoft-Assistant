package nz.ac.uclive.mjk141.airsoftloadout.utils

import android.util.Patterns

/**
 * Verifies that the username string provided meets length requirements.
 */
fun validUsernameLength(username: String?): Boolean {
    return if (username != null) {
        val correctLength = username.length in USERNAME_MIN_LENGTH..USERNAME_MAX_LENGTH
        username.isNotBlank() && correctLength
    } else false
}

fun validEmail(email: String?): Boolean {
    return if (email != null) {
        Patterns.EMAIL_ADDRESS
            .matcher(email)
            .matches()
    } else false
}

fun validPassword(password: String?): Boolean {
    return if (password != null) {
        val correctLength = password.length >= PASSWORD_MIN_LENGTH
        password.isNotBlank() && correctLength
    } else false
}