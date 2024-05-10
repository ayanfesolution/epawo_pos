package com.epawo.custodian.model.category.response

data class CategoryResponse(
    val categoryId : Int,
    val categoryName : String,
    val products : List<CategoryProducts>
)

data class CategoryProducts(
    val productId : Int,
    val productName : String,
    val invoiceModeID : Int,
    val productCode : String? = ""
)
