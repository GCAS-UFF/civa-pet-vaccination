package app.civa.vaccination.domain

interface FabricationBuilder : Builder<Fabrication> {

    var company: String
    var batch: Batch
    var expirationDate: ExpirationDate

}
