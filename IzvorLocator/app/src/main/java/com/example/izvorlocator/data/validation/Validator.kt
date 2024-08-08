package com.example.izvorlocator.data.validation

object Validator {
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
            (email.isNotEmpty())
        )
    }
    fun validatePhone(phone: String): ValidationResult{
        return ValidationResult(
            (phone.isNotEmpty())
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