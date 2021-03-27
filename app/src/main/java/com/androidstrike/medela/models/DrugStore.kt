package com.androidstrike.medela.models

class DrugStore {

    lateinit var id: String
    lateinit var name: String
    lateinit var price: String
    lateinit var stock: String

    constructor()
    constructor(id: String, name: String, price: String, stock: String) {
        this.id = id
        this.name = name
        this.price = price
        this.stock = stock

    }


}