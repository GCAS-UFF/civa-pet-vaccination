package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.DomainException
import app.civa.vaccination.usecases.VaccinationCardUseCases
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import java.util.*

@Controller
class VaccinationCardHttpHandler(
    private val vaccinationCardUseCases: VaccinationCardUseCases
) {

    suspend fun createOne(req: ServerRequest): ServerResponse {
        return try {
            val (petId, species) = req.awaitBody<VaccinationCardPayloadIn>()

            when (val cardId = vaccinationCardUseCases.createOne(petId, species)) {
                null -> badRequest().buildAndAwait()
                else -> created(req locationOf cardId).buildAndAwait()
            }
        } catch (e: DomainException) {
            status(INTERNAL_SERVER_ERROR).bodyValueAndAwait(e.explain())
        }
    }

    suspend fun readOne(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id").toUUID()

        return when (val card = vaccinationCardUseCases.findById(id)) {
            null -> notFound().buildAndAwait()
            else -> ok().bodyValueAndAwait(VaccinationCardPayloadOut from card)
        }
    }

    suspend fun deleteOne(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id").toUUID()

        return when (vaccinationCardUseCases.deleteOne(id)) {
            null -> notFound().buildAndAwait()
            else -> noContent().buildAndAwait()
        }
    }
}

private infix fun ServerRequest.locationOf(id: String) =
    this.uriBuilder().path("/{id}").build(id)

private fun String.toUUID() = UUID.fromString(this)
