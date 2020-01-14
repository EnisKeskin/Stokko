package com.example.stockko.dataClass

class Products {
    var id: Int? = null
    var categoryId: Int? = null
    var name: String? = null
    var detail: String? = null
    var piece: String? = null
    var image: String? = null
    var date: String? = null

    constructor(
        id: Int?,
        categoryId: Int?,
        name: String?,
        detail: String?,
        piece: String?,
        image: String?,
        date: String?
    ) {
        this.id = id
        this.categoryId = categoryId
        this.name = name
        this.detail = detail
        this.piece = piece
        this.image = image
        this.date = date
    }

    constructor()

}