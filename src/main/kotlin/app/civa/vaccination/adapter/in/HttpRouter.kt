package app.civa.vaccination.adapter.`in`

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

@Configuration
class HttpRouter {

    @Bean
    fun mainRouter(vaccinationCardHandler: VaccinationCardHandler) = router {
        accept(APPLICATION_JSON).nest {
            POST("/vaccination-card", vaccinationCardHandler::createOne)
        }
    }
}
