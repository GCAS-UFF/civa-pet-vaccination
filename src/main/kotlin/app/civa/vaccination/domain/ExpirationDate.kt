package app.civa.vaccination.domain

import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset.UTC

class ExpirationDate
private constructor(
    private val date: LocalDate
) {

    companion object {

        infix fun of(date: LocalDate) = ExpirationDate(date)

        infix fun from(period: Period) = ExpirationDate(
            LocalDate.now(UTC).plus(period)
        )
    }

    fun mustBeValid() = apply {
        date.mustBeValid()
    }

    override fun toString() =
        "ExpirationDate(value=$date)"
}

private fun LocalDate.hasExpired(): Boolean {
    val today = LocalDate.now(UTC)
    return this.isBefore(today)
}

private fun LocalDate.mustBeValid() = apply {
    if (this.hasExpired()) {
        throw ExpiredVaccineException from this
    }
}
