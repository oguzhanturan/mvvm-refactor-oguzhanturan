package school.cactus.succulentshop.register.validation

import android.util.Patterns
import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator


class EmailValidator : Validator {
    override fun validate(field: String): Int? {
        val checkEmail = Patterns.EMAIL_ADDRESS.matcher(field.trim()).matches()

        return when {
            field.isEmpty() -> R.string.this_field_is_required
            !(checkEmail) -> R.string.email_invalid
            else -> null
        }
    }
}
