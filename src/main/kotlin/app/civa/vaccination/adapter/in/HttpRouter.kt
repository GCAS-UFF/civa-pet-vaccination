package app.civa.vaccination.adapter.`in`

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

@Configuration
class HttpRouter {

    @Bean
    fun vaccinationCardRouter(vaccinationCardHandler: VaccinationCardHandler) = router {
        "/vaccination-card".nest {
            accept(APPLICATION_JSON).nest {
                POST(vaccinationCardHandler::createOne)
            }
        }
    }
}
