package app.civa.vaccination.domain

import java.util.*
import kotlin.NoSuchElementException

class Applications : HashMap<String, Collection<VaccineApplication>>() {

    companion object {

        const val APPLICATION_NOT_FOUND = "Vaccine Application not found"
    }

    infix fun add(entry: VaccineApplication) {
        val (vaccineName, application) = entry.toPair()

        when (application happenedRecentlyIn this[vaccineName]) {
            true -> handleException(vaccineName, application)
            else -> mergeEntry(vaccineName, application)
        }
    }

    infix fun findBy(applicationId: UUID): VaccineApplication? {
        return this.flatMap { it.value }
            .firstOrNull { it.id == applicationId }
    }

    infix fun deleteBy(applicationId: UUID) {
        when (val entry = this findBy applicationId) {
            null -> throw NoSuchElementException(APPLICATION_NOT_FOUND)
            else -> this delete entry
        }
    }

    fun countAll() = this.flatMap { it.value }.count()

    private infix fun delete(entry: VaccineApplication) {
        val (vaccineName, application) = entry.toPair()

        val filteredApplications = this[vaccineName] minus application

        when (filteredApplications.size) {
            0 -> this.remove(vaccineName)
            else -> this[vaccineName] = filteredApplications
        }
    }

    private fun handleException(name: String, application: VaccineApplication) {
        val status = this[name]
            ?.lastOrNull { it == application }
            ?.mapStatusFrom(application)

        throw IllegalApplicationException from status
    }

    private fun mergeEntry(vaccineName: String, application: VaccineApplication) =
        this.merge(vaccineName, listOf(application)) { a, b -> a + b }

    override fun toString() = "Applications(applications=${this.entries})"

}

infix fun Collection<VaccineApplication>?.minus(application: VaccineApplication) =
    this!!.filter { it.id != application.id }
