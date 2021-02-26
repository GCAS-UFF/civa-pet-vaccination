package app.civa.vaccination.domain

interface VaccineBuilder : Builder<Vaccine> {

    val species: Collection<Species>
    val name: String
    val commercialName: String
    val company: String
    val batch: Batch
    val expirationDate: ExpirationDate

}
