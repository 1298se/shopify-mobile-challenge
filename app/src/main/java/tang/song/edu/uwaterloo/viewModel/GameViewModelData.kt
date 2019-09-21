package tang.song.edu.uwaterloo.viewModel

import tang.song.edu.uwaterloo.retrofit.models.ProductResponse

data class GameViewModelData(
    var productsData: List<ProductResponse>,
    var matchedProductsData: List<ProductResponse>,
    var selectedPositions: List<Int>,
    var shouldAllowSelection: Boolean
)
