package app.civa.vaccination.domain

import java.util.function.Consumer

class Vaccine(
    private val species: Collection<Species>,
    private val name: String,
    private val commercialName: String,
    private val company: String,
    private val batch: Batch,
    private val expirationDate: ExpirationDate
) {
    constructor(builder: VaccineBuilder) : this(
        builder.species,
        builder.name,
        builder.commercialName,
        builder.company,
        builder.batch,
        builder.expirationDate
    )

    companion object {

        const val SPECIES_DOESNT_MATCH = "Species doesn't match vaccine's species"
    }

    fun accept(visitor: VaccineVisitor) {
        visitor.seeSpecies(species)
        visitor.seeName(name)
        visitor.seeCommercialName(commercialName)
        visitor.seeCompany(company)
        visitor.seeBatch(batch)
        visitor.seeExpirationDate(expirationDate)
    }

    fun accept(consumer: Consumer<String>) {
        consumer.accept(name)
    }

    fun mustBeValid() {
        expirationDate.mustBeValid()
    }

    fun matches(species: Species) = this.species.contains(species)

    fun mustMatchSpecies(species: Species) {
        when (matches(species)) {
            false -> throw IllegalStateException(SPECIES_DOESNT_MATCH)
        }
    }

    override fun equals(other: Any?) =
        (other is Vaccine)
        && species == other.species
        && name == other.name

    override fun hashCode(): Int {
        var result = species.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "Vaccine(species=$species,  " +
               "name='$name', " +
               "commercialName='$commercialName', " +
               "company='$company', " +
               "batch=$batch, " +
               "expirationDate=$expirationDate)"
    }

}
