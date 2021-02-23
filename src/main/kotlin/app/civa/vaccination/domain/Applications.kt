package app.civa.vaccination.domain

import java.lang.RuntimeException
import java.util.*
import kotlin.NoSuchElementException

class Applications : HashMap<String, Collection<VaccineApplication>>() {
    companion object {

        const val APPLICATION_NOT_FOUND = "Vaccine Application not found"
    }

    fun addEntry(entry: VaccineApplication) {
        val (vaccineName, application) = entry.toPair()

        when (this[vaccineName]?.any { it == application }) {
            true -> handleException(vaccineName, application)
            else -> mergeEntry(vaccineName, application)
        }
    }

    fun findById(applicationId: UUID): VaccineApplication? {
        return this.flatMap { it.value }
            .firstOrNull { it.id == applicationId }
    }

    fun deleteById(applicationId: UUID) {
        when (val entry = this.findById(applicationId)) {
            null -> throw NoSuchElementException(APPLICATION_NOT_FOUND)
            else -> deleteApplication(entry)
        }
    }

    fun countAll() = this.flatMap { it.value }.count()

    private fun deleteApplication(entry: VaccineApplication) {
        val (vaccineName, application) = entry.toPair()

        val filteredSet = this[vaccineName]!!
            .filter { it.id != application.id }
            .toSet()

        when (filteredSet.size) {
            0 -> this.remove(vaccineName)
            else -> this[vaccineName] = filteredSet
        }
    }

    private fun handleException(name: String, application: VaccineApplication) {
        val status = this[name]
            ?.lastOrNull { it == application }
            ?.mapStatus(application)

        when (status) {
            null -> throw RuntimeException()
            else -> throw IllegalApplicationException(status)
        }
    }

    private fun mergeEntry(vaccineName: String, application: VaccineApplication) =
        this.merge(vaccineName, listOf(application)) { a, b -> a + b }

    override fun toString(): String {
        return "Applications(applications=${this.entries})"
    }

}
