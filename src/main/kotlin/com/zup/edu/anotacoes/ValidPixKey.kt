package com.zup.edu.anotacoes

import com.zup.edu.dto.ChavePixRequest
import jakarta.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "Chave pix inválida (\${validatedValue.tipoDeChave})",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> =[]
)

@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, ChavePixRequest>{

    override fun isValid(value: ChavePixRequest?, context: ConstraintValidatorContext?): Boolean {

        if(value?.tipoDeChave == null) return false

        return value.tipoDeChave.valida(value.valorDaChave)
    }

}
