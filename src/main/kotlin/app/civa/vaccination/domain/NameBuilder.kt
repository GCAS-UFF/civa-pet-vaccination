package app.civa.vaccination.domain

interface NameBuilder : Builder<Name> {

    var classification: String
    var commercial: String

}
