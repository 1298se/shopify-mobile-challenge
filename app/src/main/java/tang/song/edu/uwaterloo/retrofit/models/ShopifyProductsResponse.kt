package tang.song.edu.uwaterloo.retrofit.models

data class ShopifyProductsResponse (
    val products: List<ProductResponse>
) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}