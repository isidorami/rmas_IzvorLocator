package com.example.izvorlocator.data.validation

object Validator {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
    private val phoneRegex = Regex("^[+]?[0-9]{10,13}\$")

    fun validateFirstname(firstname: String): ValidationResult{
        return ValidationResult(
            (firstname.isNotEmpty() && firstname.length>=2)
        )
    }
    fun validateLastname(lastname: String): ValidationResult{
        return ValidationResult(
            (lastname.isNotEmpty() && lastname.length>=2)
        )
    }
    fun validateEmail(email: String): ValidationResult{
        return ValidationResult(
            (email.isNotEmpty() && email.matches(emailRegex))
        )
    }
    fun validatePhone(phone: String): ValidationResult{
        return ValidationResult(
            (phone.isNotEmpty() && phone.matches(phoneRegex))
        )
    }
    fun validatePassword(password: String): ValidationResult{
        return ValidationResult(
            (password.isNotEmpty() && password.length>=6)
        )
    }
}

data class ValidationResult(
    val status : Boolean
)