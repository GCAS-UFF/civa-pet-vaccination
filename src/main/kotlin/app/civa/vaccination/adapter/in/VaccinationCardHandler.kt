package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.DomainException
import app.civa.vaccination.usecases.VaccinationCardPortIn
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import java.util.*

@Controller
class VaccinationCardHandler(
    private val vaccinationCardPortIn: VaccinationCardPortIn
) {

    suspend fun createOne(req: ServerRequest): ServerResponse {
        return try {
            val (petId, species) = req.awaitBody<VaccinationCardPayloadIn>()

            vaccinationCardPortIn.createOne(petId, species)
                ?.let { created(buildLocation(req, it)).buildAndAwait() }
                ?: badRequest().buildAndAwait()
        } catch (e: DomainException) {
            status(INTERNAL_SERVER_ERROR).bodyValueAndAwait(e.explain())
        }
    }

    suspend fun readOne(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id")

        return vaccinationCardPortIn.findById(UUID.fromString(id))
            ?.let {
                ok().bodyValueAndAwait(VaccinationCardPayloadOut.from(it))
            }
            ?: notFound().buildAndAwait()
    }

    private fun buildLocation(req: ServerRequest, id: String) =
        req.uriBuilder().path("/{id}").build(id)

}
