package app.civa.vaccination.adapter.`in`

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class HttpRouter {

    @Bean
    fun vaccinationCard(handler: VaccinationCardHandler) = coRouter {
        "/vaccination-card".nest {
            accept(APPLICATION_JSON).nest {
                GET("/{id}", handler::readOne)
                POST("", handler::createOne)
            }
        }
    }
}
