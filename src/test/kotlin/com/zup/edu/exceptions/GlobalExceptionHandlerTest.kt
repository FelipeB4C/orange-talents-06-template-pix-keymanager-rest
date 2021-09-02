package com.zup.edu.exceptions

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    val requestGenerica = HttpRequest.GET<Any>("/")

    @Test
    internal fun `deve retornar 404 quando statusException for not found`(){

        val mensagem = "Não encontrado"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, notFoundException)

        assertEquals(HttpStatus.NOT_FOUND, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 422 quando statusException for already existis`(){

        val mensagem = "Chave pix já existente"
        val alreadyExistsException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, alreadyExistsException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 400 quando statusException for Invalid Argument`(){

        val mensagem = "Dados da requisição estão inválidos"
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, invalidArgumentException)

        assertEquals(HttpStatus.BAD_REQUEST, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 500 quando statusException for erro inesperado`(){

        val mensagem = "Erro inesperado"
        val erroInesperado = StatusRuntimeException(Status.UNKNOWN.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, erroInesperado)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.status)
        assertNotNull(resposta.body())
        assertEquals("Não foi possível completar a requisição devido ao erro: ${mensagem} (UNKNOWN)", (resposta.body() as JsonError).message)
    }

}