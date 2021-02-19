package app.civa.vaccination.domain

enum class DateTimeStatus(val message: String) {
    VALID("Provided application is valid"),
    BEFORE("Provided application is before today"),
    SAME("Provided application already added"),
    INTERVAL("Provided application cannot be added today");

}
