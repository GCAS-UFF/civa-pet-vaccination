package app.civa.vaccination.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.function.Predicate

class PetWeight
private constructor(private val weight: BigDecimal) {

    val value: Double
        get() = this.weight.toDouble()

    companion object {

        infix fun from(weight: Double) =
            when (weight test { it <= 0 }) {
                true -> throw InvalidPetWeightException from weight
                else -> PetWeight(weight scaledTo 2)
            }
    }

    override fun toString() = "PetWeight(value=$weight)"

}

private infix fun Double.scaledTo(scale: Int): BigDecimal =
    BigDecimal.valueOf(this).setScale(scale, RoundingMode.HALF_EVEN)

private infix fun Double.test(predicate: Predicate<Double>) = predicate.test(this)
