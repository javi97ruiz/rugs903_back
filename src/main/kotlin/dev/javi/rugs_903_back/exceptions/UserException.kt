package dev.javi.rugs_903_back.exceptions

sealed class UserException (message: String) :Exception(message) {
    class UsernameNotFoundException(message: String): RuntimeException(message)

}
