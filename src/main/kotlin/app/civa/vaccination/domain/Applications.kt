package app.civa.vaccination.domain

import app.civa.vaccination.domain.DateTimeStatus.VALID
import java.util.*

class Applications : HashMap<String, Collection<VaccineApplication>>() {

    infix fun add(entry: VaccineApplication) {
        val (vaccineName, newApplication) = entry.toPair()

        val status = this[vaccineName]
            ?.map { it mapStatusFrom newApplication }
            ?.lastOrNull { it != VALID }

        when (status) {
            null -> this.save(vaccineName, newApplication)
            else -> throw InvalidApplicationException from status
        }
    }

    infix fun findBy(applicationId: UUID): VaccineApplication? {
        return this.flatMap { it.value }
            .firstOrNull { it.id == applicationId }
    }

    infix fun deleteBy(applicationId: UUID) {
        when (val application = this findBy applicationId) {
            null -> throw ApplicationNotFoundException from applicationId
            else -> this delete application
        }
    }

    private infix fun delete(entry: VaccineApplication) {
        val (vaccineName, application) = entry.toPair()

        val filteredApplications = this[vaccineName] minus application

        when (filteredApplications?.size) {
            null ->  throw ApplicationNotFoundException from application.id
            0 -> this.remove(vaccineName)
            else -> this[vaccineName] = filteredApplications
        }
    }

    private fun save(vaccineName: String, application: VaccineApplication) =
        this.merge(vaccineName, listOf(application)) { a, b -> a + b }

    fun countAll() = this.flatMap { it.value }.count()

    override fun toString() = "Applications(applications=${this.entries})"

}
