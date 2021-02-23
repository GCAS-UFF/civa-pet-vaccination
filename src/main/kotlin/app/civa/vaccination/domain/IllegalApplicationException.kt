package app.civa.vaccination.domain

class IllegalApplicationException(status: DateTimeStatus) :
        IllegalArgumentException("${status.message}. Status=$status")
