package app.civa.vaccination.domain

import app.civa.vaccination.domain.DateTimeStatus.VALID
import java.util.*

const val MAX_VACCINES = 8

class Applications : HashMap<String, Collection<VaccineApplication>>(MAX_VACCINES) {

    infix fun findBy(id: UUID) =
        this.flatMap { it.value }
            .firstOrNull { it.id == id }
            ?: throw ApplicationNotFoundException from id

    infix fun add(application: VaccineApplication) {
        when (val status = this mapStatusFrom application) {
            null -> this save application
            else -> throw InvalidApplicationException from status
        }
    }

    infix fun deleteBy(id: UUID) {
        val application = this findBy id
        val vaccineKey = application.getKey()

        val filteredApplications = this[vaccineKey] minus application

        when (filteredApplications.size) {
            0 -> this.remove(vaccineKey)
            else -> this[vaccineKey] = filteredApplications
        }
    }

    private infix fun mapStatusFrom(application: VaccineApplication): DateTimeStatus? {
        val vaccineKey = application.getKey()

        return this[vaccineKey]
            ?.map { it mapStatusFrom application }
            ?.lastOrNull { it != VALID }
    }

    private infix fun save(application: VaccineApplication) =
        this.merge(application.getKey(), listOf(application)) { a, b -> a + b }

    override fun toString() = "Applications(applications=${this.entries})"
}
