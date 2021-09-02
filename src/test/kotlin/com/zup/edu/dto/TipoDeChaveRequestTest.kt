package com.zup.edu.dto

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoDeChaveRequestTest {

    @Nested
    inner class ALEATORIA {

        @Test
        fun `deve ser valido quando chave aleatoria for nula ou vazia`() {
            with(TipoDeChaveRequest.ALEATORIA) {
                assertTrue(valida(null))
                assertTrue(valida(""))
            }
        }

        @Test
        fun `nao deve ser valido quando chave aleatoria possuir um valor`() {
            with(TipoDeChaveRequest.ALEATORIA) {
                assertFalse(valida("0R@Nge tA1enT$"))
            }
        }

    }

    @Nested
    inner class CPF {

        @Test
        fun `deve ser valido quando CPF for um numero adequado`() {
            with(TipoDeChaveRequest.CPF) {
                assertTrue(valida("32823940049"))
            }
        }

        @Test
        fun `nao deve ser valido quando CPF for invalido, tiver pontuacao ou letras`() {
            with(TipoDeChaveRequest.CPF) {
                assertFalse(valida("99237907810"))
                assertFalse(valida("328.239.400-49"))
                assertFalse(valida("99A379078b0"))
            }
        }

        @Test
        fun `nao deve ser valido quando CPF nao for informado`() {
            with(TipoDeChaveRequest.CPF) {
                assertFalse(valida(""))
                assertFalse(valida(null))
            }
        }

    }

    @Nested
    inner class CELULAR {

        @Test
        fun `deve ser valido quando for um celular adequado`() {
            with(TipoDeChaveRequest.CELULAR) {
                assertTrue(valida("+5561984125806"))
            }
        }

        @Test
        fun `nao deve ser valido quando for um celular invalido ou com pontuacao`() {
            with(TipoDeChaveRequest.CELULAR) {
                assertFalse(valida("985310404"))
                assertFalse(valida("+556198412-5806"))
            }
        }

        @Test
        fun `nao deve ser valido quando celular nao for informado`() {
            with(TipoDeChaveRequest.CELULAR) {
                assertFalse(valida(""))
                assertFalse(valida(null))
            }
        }

    }

    @Nested
    inner class EMAIL {

        @Test
        fun `deve ser valido quando for um e-mail adequado`() {
            with(TipoDeChaveRequest.EMAIL) {
                assertTrue(valida("usuario@email.com"))
            }
        }

        @Test
        fun `nao deve ser valido quando for um e-mail nao adequado`() {
            with(TipoDeChaveRequest.EMAIL) {
                assertFalse(valida("usuarioemail.com"))
            }
        }

        @Test
        fun `nao deve ser valido quando e-mail nao for informado`() {
            with(TipoDeChaveRequest.EMAIL) {
                assertFalse(valida(""))
                assertFalse(valida(null))
            }
        }

    }


}