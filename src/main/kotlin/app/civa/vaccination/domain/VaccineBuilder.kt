package app.civa.vaccination.domain

import java.time.LocalDate

class VaccineBuilder : Builder<Vaccine> {

    lateinit var species: Collection<Species>
    lateinit var name: String
    lateinit var commercialName: String
    lateinit var company: String
    lateinit var batch: Batch
    lateinit var expirationDate: ExpirationDate

    fun species(vararg species: Species) =
        apply { this.species = species.toSet() }

    fun name(name: String) =
        apply { this.name = name }

    fun commercialName(commercialName: String) =
        apply { this.commercialName = commercialName }

    fun company(company: String) =
        apply { this.company = company }

    fun batch(value: String) =
        apply { this.batch = Batch.from(value) }

    fun expirationDate(date: LocalDate) =
        apply { this.expirationDate = ExpirationDate(date) }

    override fun build() = Vaccine(this)

}