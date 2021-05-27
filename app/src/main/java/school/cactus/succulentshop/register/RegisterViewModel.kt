package school.cactus.succulentshop.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.login.validation.PasswordValidator
import school.cactus.succulentshop.register.validation.EmailValidator
import school.cactus.succulentshop.register.validation.UsernameValidator

class RegisterViewModel(
    private val store: JwtStore,
    private val repository: RegisterRepository
) : BaseViewModel() {

    private val emailValidator = EmailValidator()
    private val usernameValidator = UsernameValidator()
    private val passwordValidator = PasswordValidator()

    val email = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _emailErrorMessage = MutableLiveData<Int>()
    private val _usernameErrorMessage = MutableLiveData<Int>()
    private val _passwordErrorMessage = MutableLiveData<Int>()

    val emailErrorMessage: LiveData<Int> = _emailErrorMessage
    val usernameErrorMessage: LiveData<Int> = _usernameErrorMessage
    val passwordErrorMessage: LiveData<Int> = _passwordErrorMessage

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    init {
        _progressBarVisibility.value = false
    }


    fun onRegisterButtonClick() {
        if (isEmailValid() and isUsernameValid() and isPasswordValid()) {
            _progressBarVisibility.value = true
            repository.sendRegisterRequest(
                email.value.orEmpty(),
                username.value.orEmpty(),
                password.value.orEmpty(),
                object : RegisterRepository.RegisterRequestCallback {
                    override fun onRegisterSuccess(jwt: String) {
                        store.saveJwt(jwt)
                        _progressBarVisibility.value = false
                        val directions = RegisterFragmentDirections.registerSuccessful()
                        navigation.navigate(directions)
                    }

                    override fun onClientError(errorMessage: String) {
                        _snackbarState.value = SnackbarState(
                            error = errorMessage,
                            duration = BaseTransientBottomBar.LENGTH_LONG
                        )
                        _progressBarVisibility.value = false
                    }

                    override fun onTokenExpired() {
                        TODO("Not yet implemented")
                    }

                    override fun onUnexpectedError() {
                        _snackbarState.value = SnackbarState(
                            errorRes = R.string.unexpected_error_occurred,
                            duration = BaseTransientBottomBar.LENGTH_LONG
                        )
                    }

                    override fun onFailure() {
                        _snackbarState.value = SnackbarState(
                            errorRes = R.string.check_your_connection,
                            duration = Snackbar.LENGTH_INDEFINITE,
                            action = SnackbarAction(
                                text = R.string.retry,
                                action = {
                                    onRegisterButtonClick()
                                }
                            )
                        )
                    }
                })
        }
    }

    fun onAlreadyHaveAccountButtonClick() {
        val directions = RegisterFragmentDirections.haveAccount()
        navigation.navigate(directions)
    }

    private fun isEmailValid(): Boolean {
        _emailErrorMessage.value = emailValidator.validate(email.value.orEmpty())
        return _emailErrorMessage.value == null
    }

    private fun isUsernameValid(): Boolean {
        _usernameErrorMessage.value = usernameValidator.validate(username.value.orEmpty())
        return _usernameErrorMessage.value == null
    }

    private fun isPasswordValid(): Boolean {
        _passwordErrorMessage.value = passwordValidator.validate(password.value.orEmpty())
        return _passwordErrorMessage.value == null
    }
}