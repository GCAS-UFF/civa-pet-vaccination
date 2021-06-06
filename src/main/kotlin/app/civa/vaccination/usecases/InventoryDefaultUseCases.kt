package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Vaccine
import org.springframework.stereotype.Service
import java.util.*

@Service
class InventoryDefaultUseCases(
    private val persistence: InventoryPersistence
) : InventoryUseCases {
    override suspend fun createOne(vetId: UUID): String {
        TODO("Not yet implemented")
    }

    override suspend fun findByVetId(vetId: UUID) =
        persistence.findByVetId(vetId)

    override suspend fun addStock(controlId: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveVaccine(controlId: UUID): Vaccine {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveVaccine(name: String): Vaccine {
        TODO("Not yet implemented")
    }
}
