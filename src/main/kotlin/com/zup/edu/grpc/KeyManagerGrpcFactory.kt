package com.zup.edu.grpc

import com.zup.KeymanagerCadastraServiceGrpc
import com.zup.KeymanagerDeletaServiceGrpc
import com.zup.KeymanagerListaServiceGrpc
import com.zup.KeymanagerListaTodasServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun cadastraChave() = KeymanagerCadastraServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deletaChave() = KeymanagerDeletaServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChave() = KeymanagerListaServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaTodasChave() = KeymanagerListaTodasServiceGrpc.newBlockingStub(channel)

}