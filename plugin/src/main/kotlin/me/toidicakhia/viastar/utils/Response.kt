package me.toidicakhia.viastar.utils

data class Response(
    val statusCode: Int,
    val text: String,
    val headers: Map<String, String>
) {
    val location = headers["Location"] ?: ""
}