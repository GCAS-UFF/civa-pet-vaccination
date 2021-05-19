package app.civa.vaccination.adapter.out

import app.civa.vaccination.domain.VaccinationCard
import app.civa.vaccination.usecases.VaccinationCardPortOut
import app.civa.vaccination.usecases.VaccinationCardRepository
import org.springframework.stereotype.Repository

@Repository
class MongoDBVaccinationCardAdapterOut(
    private val repository: VaccinationCardRepository
) : VaccinationCardPortOut {

    override fun createOne(vaccinationCard: VaccinationCard) =
        repository.save(vaccinationCard)
}