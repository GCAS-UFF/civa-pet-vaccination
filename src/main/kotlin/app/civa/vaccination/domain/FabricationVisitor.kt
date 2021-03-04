package app.civa.vaccination.domain

interface FabricationVisitor {

    fun seeCompany(company: String)
    fun seeBatch(batch: Batch)
    fun seeExpirationDate(expirationDate: ExpirationDate)

}
