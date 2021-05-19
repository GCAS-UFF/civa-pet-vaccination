package app.civa.vaccination.adapter.`in`

import app.civa.vaccination.usecases.VaccinationCardPortIn
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Controller
class VaccinationCardHandler(
    private val vaccinationCardPortIn: VaccinationCardPortIn
) {

    fun createOne(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<VaccinationCardPayloadIn>()
            .map { it.toPair() }
            .flatMap { this.vaccinationCardPortIn.createOne(it) }
            .flatMap { Mono.just(VaccinationCardPayloadOut.from(it)) }
            .flatMap { ServerResponse.ok().bodyValue(it) }
    }

}
