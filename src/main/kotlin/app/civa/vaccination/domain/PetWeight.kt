package app.civa.vaccination.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.function.Predicate

class PetWeight
private constructor(private val weight: BigDecimal) {

    val value: Double
        get() = this.weight.toDouble()

    companion object {

        private const val PET_WEIGHT_MUST_BE_POSITIVE =
            "Pet weight must be positive: 0 (ZERO) or greater"

        infix fun from(value: Double) =
            when (value test { it <= 0 }) {
                true -> throw IllegalArgumentException(PET_WEIGHT_MUST_BE_POSITIVE)
                else -> PetWeight(value scaledTo 2)
            }
    }

    override fun toString() = "PetWeight(value=$weight)"

}

private infix fun Double.scaledTo(scale: Int): BigDecimal =
    BigDecimal.valueOf(this).setScale(scale, RoundingMode.HALF_EVEN)

private infix fun Double.test(predicate: Predicate<Double>) = predicate.test(this)
