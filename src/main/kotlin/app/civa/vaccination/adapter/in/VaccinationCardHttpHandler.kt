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

            vaccinationCardUseCases.createOne(petId, species)
                ?.let { created(req locationFrom it).buildAndAwait() }
                ?: badRequest().buildAndAwait()
        } catch (e: DomainException) {
            status(INTERNAL_SERVER_ERROR).bodyValueAndAwait(e.explain())
        }
    }

    suspend fun readOne(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id").toUUID()

        return vaccinationCardUseCases.findById(id)
            ?.let { ok().bodyValueAndAwait(VaccinationCardPayloadOut from it) }
            ?: notFound().buildAndAwait()
    }
}

private infix fun ServerRequest.locationFrom(id: String) =
    this.uriBuilder().path("/{id}").build(id)

private fun String.toUUID() = UUID.fromString(this)
