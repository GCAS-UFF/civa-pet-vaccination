package app.civa.vaccination.domain

interface Builder<T> {
    fun build(): T
}