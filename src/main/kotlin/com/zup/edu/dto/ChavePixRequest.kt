package com.zup.edu.dto

import com.zup.CadastraChavePixRequest
import com.zup.TipoDeChave
import com.zup.TipoDeConta
import com.zup.edu.anotacoes.ValidPixKey
import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
class ChavePixRequest(
    @field:Size(max = 77) val valorDaChave: String?,
    @field:NotNull val tipoDeChave: TipoDeChaveRequest?,
    @field:NotNull val tipoDeConta: TipoDeContaRequest?,
) {

    fun toModelGrpc(clienteId: UUID): CadastraChavePixRequest {
        return CadastraChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoDeChave(tipoDeChave?.atributoGrpc ?: TipoDeChave.CHAVE_DESCONHECIDA)
            .setValorDaChave(valorDaChave ?: "")
            .setTipoDeConta(tipoDeConta?.atributoGrpc ?: TipoDeConta.CONTA_DESCONHECIDO)
            .build()
    }

}


enum class TipoDeChaveRequest(val atributoGrpc: TipoDeChave) {

    CPF(TipoDeChave.CPF) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            if (!chave.matches("[0-9]+".toRegex())) return false

            return CPFValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },

    CELULAR(TipoDeChave.CELULAR) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoDeChave.EMAIL) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },

    ALEATORIA(TipoDeChave.CHAVE_ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank() // não deve ser preenchido
    };

    abstract fun valida(chave: String?): Boolean

}


enum class TipoDeContaRequest(val atributoGrpc: TipoDeConta) {

    CONTA_CORRENTE(TipoDeConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoDeConta.CONTA_POUPANCA)

}