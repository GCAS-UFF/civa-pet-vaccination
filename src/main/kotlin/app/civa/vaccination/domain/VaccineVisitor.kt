package app.civa.vaccination.domain

interface VaccineVisitor {

    fun seeSpecies(species: Collection<Species>)
    fun seeName(name: String)
    fun seeCommercialName(commercialName: String)
    fun seeCompany(company: String)
    fun seeBatch(batch: Batch)
    fun seeExpirationDate(expirationDate: ExpirationDate)

}
