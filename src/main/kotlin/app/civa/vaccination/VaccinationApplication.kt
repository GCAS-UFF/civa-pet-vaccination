package app.civa.vaccination

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VaccinationApplication

fun main(args: Array<String>) {
    runApplication<VaccinationApplication>(*args)
}
