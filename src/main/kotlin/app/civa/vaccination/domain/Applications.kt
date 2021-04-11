package app.civa.vaccination.domain

import app.civa.vaccination.domain.DateTimeStatus.VALID
import java.util.*

class Applications : HashMap<String, Collection<VaccineApplication>>() {

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

    infix fun findBy(id: UUID): VaccineApplication? {
        return this.flatMap { it.value }
            .firstOrNull { it.id == id }
    }

    infix fun deleteBy(id: UUID) {
        when (val application = this findBy id) {
            null -> throw ApplicationNotFoundException from id
            else -> this delete application
        }
    }

    private infix fun delete(entry: VaccineApplication) {
        val (vaccineName, application) = entry.toPair()

        val filteredApplications = this[vaccineName] minus application

        when (filteredApplications?.size) {
            null -> throw ApplicationNotFoundException from application.id
            0 -> this.remove(vaccineName)
            else -> this[vaccineName] = filteredApplications
        }
    }

    private fun save(vaccineName: String, application: VaccineApplication) =
        this.merge(vaccineName, listOf(application)) { a, b -> a + b }

    fun countAll() = this.flatMap { it.value }.count()

    override fun toString() = "Applications(applications=${this.entries})"

}
