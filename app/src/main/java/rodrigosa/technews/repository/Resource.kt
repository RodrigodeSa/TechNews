package rodrigosa.technews.repository

class Resource<T>(
    val data: T?,
    val error: String? = null
)