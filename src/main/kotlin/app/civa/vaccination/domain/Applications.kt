package app.civa.vaccination.domain

import app.civa.vaccination.domain.DateTimeStatus.VALID
import java.util.*

const val MAX_VACCINES = 8

class Applications : HashMap<String, Collection<VaccineApplication>>(MAX_VACCINES) {

    infix fun add(entry: VaccineApplication) {
        val (vaccineName, newApplication) = entry.toPair()

        when (val status = this.mapStatus(vaccineName, newApplication)) {
            null -> this.save(vaccineName, newApplication)
            else -> throw InvalidApplicationException from status
        }
    }

    private fun mapStatus(vaccineName: String, newApplication: VaccineApplication) =
        this[vaccineName]
            ?.map { it mapStatusFrom newApplication }
            ?.lastOrNull { it != VALID }

    infix fun findBy(id: UUID) =
         this.flatMap { it.value }
            .firstOrNull { it.id == id }
            ?: throw ApplicationNotFoundException from id


    infix fun deleteBy(id: UUID) {
        val (vaccineName, application) = this.findBy(id).toPair()

        val filteredApplications = this[vaccineName] minus application

        when (filteredApplications.size) {
            0 -> this.remove(vaccineName)
            else -> this[vaccineName] = filteredApplications
        }
    }

    private fun save(vaccineName: String, application: VaccineApplication) =
        this.merge(vaccineName, listOf(application)) { a, b -> a + b }

    override fun toString() = "Applications(applications=${this.entries})"
}
