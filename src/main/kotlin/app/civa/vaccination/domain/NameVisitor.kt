package app.civa.vaccination.domain

interface NameVisitor {

    fun visitClassification(reference: String)
    fun visitCommercial(commercial: String)

}
