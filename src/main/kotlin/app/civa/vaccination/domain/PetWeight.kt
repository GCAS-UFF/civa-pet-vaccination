package app.civa.vaccination.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.function.Function
import java.util.function.Predicate

class PetWeight private constructor(private val value: BigDecimal) {

    companion object {

        private const val PET_WEIGHT_MUST_BE_POSITIVE =
            "Pet weight must be positive: 0 (ZERO) or greater"

        private val isNegativeOrZero = Predicate<Double> { it <= 0 }

        private val bigDecimalConverter = Function<Double, BigDecimal> {
            BigDecimal.valueOf(it).setScale(2, RoundingMode.HALF_EVEN)
        }
    }

    constructor(value: Double) : this(bigDecimalConverter.apply(value)) {
        when {
            isNegativeOrZero.test(value) ->
                throw IllegalArgumentException(PET_WEIGHT_MUST_BE_POSITIVE)
        }
    }

    fun get() = this.value.toDouble()

    override fun toString(): String {
        return "PetWeight(value=$value)"
    }
}