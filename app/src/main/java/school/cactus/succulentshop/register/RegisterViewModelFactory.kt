package school.cactus.succulentshop.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.auth.JwtStore

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory(
    private val store: JwtStore,
    private val repository: RegisterRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RegisterViewModel(store, repository) as T
}