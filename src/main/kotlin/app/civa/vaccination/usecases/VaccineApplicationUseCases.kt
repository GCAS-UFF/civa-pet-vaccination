package app.civa.vaccination.usecases

import app.civa.vaccination.domain.PetWeight
import java.util.*

interface VaccineApplicationUseCases {

    suspend fun addApplication(cardId: UUID, vaccineControlId: UUID, petWeight: PetWeight): String?
    suspend fun deleteApplication(cardId: UUID, applicationId: UUID): Boolean

}
