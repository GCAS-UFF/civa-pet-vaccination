package app.civa.vaccination.domain

class Batch
private constructor(
    private val prefix: String,
    private val suffix: String
) {

    val value: String
        get() = "$prefix/$suffix"

    companion object {

        // Batch pattern: 081/22
        private val pattern = Regex("\\d{3}/\\d{2}")

        infix fun from(value: String): Batch {
            value mustMatch pattern

            val (prefix, suffix) = value.split("/")
            return Batch(prefix, suffix)
        }
    }

    override fun toString() = "Batch(prefix='$prefix', suffix='$suffix')"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Batch
        return when {
            prefix != other.prefix -> false
            suffix != other.suffix -> false
            else -> true
        }
    }

    override fun hashCode() = prefix.hashCode() * 31 + suffix.hashCode()
}

private infix fun String.mustMatch(pattern: Regex) {
    when (this matches pattern) {
        false -> throw IllegalArgumentException()
    }
}
