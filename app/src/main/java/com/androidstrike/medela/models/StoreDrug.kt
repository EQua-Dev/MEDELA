package com.androidstrike.medela.models

class StoreDrug {

    lateinit var id: String
    lateinit var price: String
    lateinit var stk_qty: String

    constructor()
    constructor(id: String, price: String, stk_qty: String) {
        this.id = id
        this.price = price
        this.stk_qty = stk_qty
    }


}