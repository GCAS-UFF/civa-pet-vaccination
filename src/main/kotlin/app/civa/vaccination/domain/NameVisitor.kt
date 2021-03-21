package app.civa.vaccination.domain

interface NameVisitor {

    fun seeClassification(reference: String)
    fun seeCommercial(commercial: String)
}
