package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.domain.DomainException
import app.civa.vaccination.usecases.VaccinationCardPortIn
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
            val (petId, species) = req.awaitBody<VaccinationCardPayloadIn>().toPair()
            val cardId = vaccinationCardPortIn.createOne(petId, species)

            created(buildLocation(req, cardId))
                .contentType(APPLICATION_JSON)
                .buildAndAwait()
        } catch (e: IllegalArgumentException) {
            badRequest()
                .contentType(APPLICATION_JSON)
                .buildAndAwait()
        } catch (e: DomainException) {
            badRequest().bodyValueAndAwait(e.explain())
        }
    }

    suspend fun readOne(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id")

        return vaccinationCardPortIn.findById(UUID.fromString(id))
            ?.let {
                ok().contentType(APPLICATION_JSON)
                    .bodyValueAndAwait(VaccinationCardPayloadOut.from(it))
            } ?: notFound().buildAndAwait()
    }

    private fun buildLocation(req: ServerRequest, id: String) =
        req.uriBuilder().path("/{id}").build(id)

}
