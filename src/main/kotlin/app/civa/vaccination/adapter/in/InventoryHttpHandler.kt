package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.usecases.InventoryUseCases
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Controller
class InventoryHttpHandler(
    private val inventoryUseCases: InventoryUseCases
) {

    suspend fun readAll(req: ServerRequest): ServerResponse {
        val params = req.queryParams()
        return try {
            val vetId = params.getFirst("vetId")
                ?.toUUID()
                ?: throw IllegalArgumentException()

            val inventory = inventoryUseCases.findByVetId(vetId)
            ok().bodyValueAndAwait(inventory)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    suspend fun createOne(req: ServerRequest): ServerResponse {
        return try {
            val (vetId) = req.awaitBody<InventoryPayloadIn>()
            val inventoryId = inventoryUseCases.createOne(vetId)
            created(req locationOf inventoryId).buildAndAwait()
        } catch (e: Exception) {
            handleException(e)
        }
    }
}
