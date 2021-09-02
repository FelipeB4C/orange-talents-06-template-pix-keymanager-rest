package com.zup.edu.controllers

import com.zup.KeymanagerCadastraServiceGrpc
import com.zup.edu.dto.ChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

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