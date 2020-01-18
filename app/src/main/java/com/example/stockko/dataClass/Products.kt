package com.example.stockko.dataClass

class Products {
    var key: String? = null
    var categoryId: String? = null
    var name: String? = null
    var detail: String? = null
    var piece: String? = null
    var image: String? = null
    var date: String? = null


    constructor()

    constructor(
        key: String?,
        categoryId: String?,
        name: String?,
        detail: String?,
        piece: String?,
        image: String?,
        date: String?
    ) {
        this.key = key
        this.categoryId = categoryId
        this.name = name
        this.detail = detail
        this.piece = piece
        this.image = image
        this.date = date
    }

}