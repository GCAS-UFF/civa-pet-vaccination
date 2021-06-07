package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.DomainException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter
import java.util.*

@Configuration
class HttpRouter {

    @Bean
    fun vaccinationCard(handler: VaccinationCardHttpHandler) = coRouter {
        "/vaccination-card".nest {
            accept(APPLICATION_JSON).nest {
                GET("/{id}", handler::readOne)
                DELETE("/{id}", handler::deleteOne)
                POST("", handler::createOne)
            }
        }
    }

    @Bean
    fun inventory(handler: InventoryHttpHandler) = coRouter {
        "/inventory".nest {
            accept(APPLICATION_JSON).nest {
                GET("", handler::readAll)
                POST("", handler::createOne)
            }
        }
    }
}

fun String.toUUID(): UUID = UUID.fromString(this)

infix fun ServerRequest.locationOf(id: String) =
    this.uriBuilder().path("/{id}").build(id)

suspend fun handleException(e: Exception) = when (e) {
    is NoSuchElementException -> notFound().buildAndAwait()
    is IllegalArgumentException -> badRequest().buildAndAwait()
    is DuplicateKeyException -> badRequest()
        .bodyValueAndAwait(ExplanationPayloadOut(e.message!!))
    is DomainException -> unprocessableEntity()
        .bodyValueAndAwait(ExplanationPayloadOut(e.explain()))
    else -> status(INTERNAL_SERVER_ERROR).buildAndAwait()
}
