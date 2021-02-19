package app.civa.vaccination.domain

import app.civa.vaccination.domain.DateTimeStatus.*
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset.UTC
import java.time.temporal.ChronoUnit.DAYS

class ApplicationDateTime
private constructor(private val value: LocalDateTime) {

    companion object {

        const val VACCINATION_INTERVAL_DAYS: Long = 10

        fun now() = ApplicationDateTime(LocalDateTime.now(UTC))

        fun of(day: Int, month: Month, year: Int, hour: Int, minute: Int) =
            ApplicationDateTime(LocalDateTime.of(year, month, day, hour, minute))
    }

    fun mapStatus(other: ApplicationDateTime): DateTimeStatus {
        val otherTime = other.value.truncatedTo(DAYS)
        val thisTime = value.truncatedTo(DAYS)

        return when {
            otherTime.isBefore(thisTime) -> BEFORE
            otherTime.isEqual(thisTime) -> SAME
            otherTime.minusDays(VACCINATION_INTERVAL_DAYS)
                .isBefore(thisTime) -> INTERVAL
            else -> VALID
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationDateTime

        return when (mapStatus(other)) {
            VALID -> false
            else -> true
        }
    }

    override fun hashCode() = value.hashCode()

    override fun toString(): String {
        return "ApplicationDateTime(value=$value)"
    }

}
