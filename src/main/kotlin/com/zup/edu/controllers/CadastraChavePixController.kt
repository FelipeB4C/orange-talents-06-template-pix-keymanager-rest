package com.zup.edu.controllers

import com.zup.CadastraChavePixRequest
import com.zup.KeymanagerCadastraServiceGrpc
import com.zup.TipoDeChave
import com.zup.TipoDeConta
import com.zup.edu.anotacoes.ValidPixKey
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Validated
@Controller("/api/v1/clientes")
class CadastraChavePixController(
    private val cadastraChavePixClient: KeymanagerCadastraServiceGrpc.KeymanagerCadastraServiceBlockingStub
) {

    val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Post("/{clienteId}/pix")
    fun cadastra(@PathVariable clienteId: UUID, @Valid @Body request: ChavePixRequest): HttpResponse<Any> {

        LOGGER.info("Cadastrando nova chave pix")

        val chavePix = request.toModelGrpc(clienteId)
        val chaveSalva = cadastraChavePixClient.cadastraChavePix(chavePix)

        LOGGER.info("Chave pix cadastrada")

        return HttpResponse.created(location(clienteId, chaveSalva.pixId))
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse
        .uri("/api/v1/clientes/$clienteId/pix/${pixId}")

}

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