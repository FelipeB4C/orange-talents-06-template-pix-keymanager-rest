package com.zup.edu.controllers

import com.zup.CadastraChavePixRequest
import com.zup.CadastraChavePixResponse
import com.zup.KeymanagerCadastraServiceGrpc
import com.zup.TipoDeChave
import com.zup.edu.dto.ChavePixRequest
import com.zup.edu.dto.TipoDeChaveRequest
import com.zup.edu.dto.TipoDeContaRequest
import com.zup.edu.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class CadastraChavePixControllerTest(){

    @field:Inject
    lateinit var cadastraStub: KeymanagerCadastraServiceGrpc.KeymanagerCadastraServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient


    @Test
    internal fun `deve cadastrar uma nova chave pix`(){
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostasGrpc = CadastraChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .build()

        BDDMockito.given(cadastraStub.cadastraChavePix(Mockito.any())).willReturn(respostasGrpc)

        val novaChavePix = ChavePixRequest(valorDaChave = "teste@email.com",
            TipoDeChaveRequest.EMAIL,
            TipoDeContaRequest.CONTA_CORRENTE
            )

        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", novaChavePix)
        val response = client.toBlocking().exchange(request, ChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))

    }


    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun stubMock() = Mockito.mock(KeymanagerCadastraServiceGrpc.KeymanagerCadastraServiceBlockingStub::class.java)
    }

}