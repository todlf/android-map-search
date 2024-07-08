package campus.tech.kakao.map

data class SearchData (
    val name: String,
    val address: String,
    val category: String
){
    companion object {
        const val TABLE_NAME = "searchTable"
        const val TABLE_COLUMN_NAME = "name"
        const val TABLE_COLUMN_ADDRESS = "address"
        const val TABLE_COLUMN_CATEGORY = "category"

        const val SAVED_SEARCH_TABLE_NAME = "savedSearchTable"
        const val SAVED_SEARCH_COLUMN_NAME = "savename"
    }
}
