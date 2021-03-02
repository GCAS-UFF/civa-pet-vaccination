package app.civa.vaccination.domain

class IllegalApplicationException(status: DateTimeStatus) :
        IllegalArgumentException("${status.message}. Status=$status") {

    companion object {

        infix fun from(status: DateTimeStatus?) = when (status) {
            null -> BusinessException()
            else -> IllegalApplicationException(status)
        }

    }
}
