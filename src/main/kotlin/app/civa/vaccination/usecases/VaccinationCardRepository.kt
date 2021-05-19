package app.civa.vaccination.usecases

import app.civa.vaccination.domain.VaccinationCard
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface VaccinationCardRepository : ReactiveMongoRepository<VaccinationCard, String> {
}