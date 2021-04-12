package app.civa.vaccination.domain

interface FabricationVisitor {

    infix fun seeCompany(company: String)
    infix fun seeBatch(batch: Batch)
    infix fun seeExpirationDate(expirationDate: ExpirationDate)

}
