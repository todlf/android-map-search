package campus.tech.kakao.map

data class Document(
    val placeName: String,
    val distance: String?,
    val placeUrl: String,
    val categoryName: String,
    val addressName: String,
    val roadAddressName: String,
    val id: String,
    val phone: String,
    val categoryGroupCode: String,
    val categoryGroupName: String,
    val x: String,
    val y: String
)