package app.civa.vaccination.adapter.`in`

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class HttpRouter {

    @Bean
    fun vaccinationCard(httpHandler: VaccinationCardHttpHandler) = coRouter {
        "/vaccination-card".nest {
            accept(APPLICATION_JSON).nest {
                GET("/{id}", httpHandler::readOne)
                DELETE("/{id}", httpHandler::deleteOne)
                POST("", httpHandler::createOne)
            }
        }
    }
}
