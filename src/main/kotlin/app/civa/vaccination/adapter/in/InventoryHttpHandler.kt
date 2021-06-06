package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.usecases.InventoryUseCases
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait

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
}
