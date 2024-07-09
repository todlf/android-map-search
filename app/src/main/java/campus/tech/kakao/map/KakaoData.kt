package campus.tech.kakao.map

import com.google.gson.annotations.SerializedName

data class KakaoData(
    @SerializedName("documents") val documents: List<Document>,
    val meta: Meta
)

data class Meta(
    @SerializedName("meta")
    val is_end: Boolean,
    val pageable_count: Int,
    val same_name: Any,
    val total_count: Int
)

data class Document(
    val place_name: String,
    val distance: String,
    val place_url: String,
    val category_name: String,
    val address_name: String,
    val road_address_name: String,
    val id: String,
    val phone: String,
    @SerializedName("category_group_code") val categoryGroupCode: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    @SerializedName("x") val x: String?,
    @SerializedName("y") val y: String?
)
