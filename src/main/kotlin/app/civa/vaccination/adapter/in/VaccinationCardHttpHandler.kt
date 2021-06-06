package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.usecases.VaccinationCardUseCases
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*

@Controller
class VaccinationCardHttpHandler(
    private val vaccinationCardUseCases: VaccinationCardUseCases
) {

    suspend fun createOne(req: ServerRequest): ServerResponse {
        val (petId, species) = req.awaitBody<VaccinationCardPayloadIn>()

        return try {
            val cardId = vaccinationCardUseCases.createOne(petId, species)
            created(req locationOf cardId).buildAndAwait()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    suspend fun readOne(req: ServerRequest): ServerResponse {
        return try {
            val id = req.pathVariable("id").toUUID()
            val card = vaccinationCardUseCases.findById(id)
            ok().bodyValueAndAwait(VaccinationCardPayloadOut from card)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    suspend fun deleteOne(req: ServerRequest): ServerResponse {
        return try {
            val id = req.pathVariable("id").toUUID()
            vaccinationCardUseCases.deleteOne(id)
            noContent().buildAndAwait()
        } catch (e: Exception) {
            handleException(e)
        }
    }
}
