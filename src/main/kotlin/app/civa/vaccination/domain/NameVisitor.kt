package app.civa.vaccination.domain

interface NameVisitor {

    infix fun seeClassification(reference: String)
    infix fun seeCommercial(commercial: String)
}
