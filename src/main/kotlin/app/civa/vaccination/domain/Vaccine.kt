package app.civa.vaccination.domain

import java.util.*

class Vaccine(
    private val species: Collection<Species>,
    private val name: String,
    private val commercialName: String,
    private val company: String,
    private val batch: Batch,
    private val expirationDate: ExpirationDate
) {

    companion object {

        const val SPECIES_DOESNT_MATCH = "Species doesn't match vaccine's species"
    }

    constructor(builder: VaccineBuilder) : this(
        builder.species,
        builder.name,
        builder.commercialName,
        builder.company,
        builder.batch,
        builder.expirationDate
    )

    infix fun accept(visitor: VaccineVisitor) {
        visitor.seeSpecies(species)
        visitor.seeName(name)
        visitor.seeCommercialName(commercialName)
        visitor.seeCompany(company)
        visitor.seeBatch(batch)
        visitor.seeExpirationDate(expirationDate)
    }

    fun mustBeValid() = apply { expirationDate.mustBeValid() }

    infix fun mustMatch(species: Species) {
        when (this.species matches species) {
            false -> throw IllegalStateException(SPECIES_DOESNT_MATCH)
        }
    }

    fun apply(weight: PetWeight) = application {
        id = UUID.randomUUID()
        createdOn = ApplicationDateTime.now()
        petWeight = weight
        vaccine = this@Vaccine.mustBeValid()
    }

    infix fun pairNameWith(application: VaccineApplication) =
        this.name to application

    override fun equals(other: Any?) =
        (other is Vaccine)
        && species == other.species
        && name == other.name

    override fun hashCode() = 31 * species.hashCode() + name.hashCode()

    override fun toString() =
        "Vaccine(species=$species,  " +
        "name='$name', " +
        "commercialName='$commercialName', " +
        "company='$company', " +
        "batch=$batch, " +
        "expirationDate=$expirationDate)"

}

fun vaccine(lambda: VaccineEntityBuilder.() -> Unit): Vaccine {
    val builder = VaccineEntityBuilder()
    builder.lambda()
    return builder.build()
}
