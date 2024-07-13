package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
interface RetrofitService {
    @GET("v2/local/search/category.{format}")
    fun requestProducts(
        @Header("Authorization") Authorization: String,
        @Path("format") format: String,
        @Query("category_group_code") category_group_code: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int
    ): Call<KakaoData>
}