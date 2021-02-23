package app.civa.vaccination.domain

import java.time.LocalDate

class VaccineBuilderImpl : VaccineBuilder{

    override lateinit var species: Collection<Species>
    override lateinit var name: String
    override lateinit var commercialName: String
    override lateinit var company: String
    override lateinit var batch: Batch
    override lateinit var expirationDate: ExpirationDate

    fun species(species: Collection<Species>) =
        apply { this.species = species }

    fun name(name: String) =
        apply { this.name = name }

    fun commercialName(commercialName: String) =
        apply { this.commercialName = commercialName }

    fun company(company: String) =
        apply { this.company = company }

    fun batch(batch: Batch) =
        apply { this.batch = batch }

    fun expirationDate(date: LocalDate) =
        apply { this.expirationDate = ExpirationDate(date) }

    override fun build() = Vaccine(this)

}
