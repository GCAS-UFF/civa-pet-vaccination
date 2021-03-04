package app.civa.vaccination.domain

class NameEntityBuilder : NameBuilder {

    override lateinit var classification: String
    override lateinit var commercial: String

    override fun build() = Name(this)

}
