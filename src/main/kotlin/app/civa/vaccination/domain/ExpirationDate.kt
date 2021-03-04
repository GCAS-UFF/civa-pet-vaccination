package app.civa.vaccination.domain

import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset.UTC

class ExpirationDate
private constructor(
    private val value: LocalDate
) {

    companion object {

        const val VACCINE_EXPIRED = "Vaccine has expired"

        infix fun of(date: LocalDate) = ExpirationDate(date)

        infix fun from(period: Period) = ExpirationDate(
            LocalDate.now(UTC).plus(period))
    }

    private fun hasExpired(): Boolean {
        val today = LocalDate.now(UTC)
        return value.isBefore(today)
    }

    fun mustBeValid() {
        when (this.hasExpired()) {
            true -> throw IllegalStateException(VACCINE_EXPIRED)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpirationDate
        return when {
            value != other.value -> false
            else -> true
        }
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "ExpirationDate(value=$value)"

}
