package com.androidstrike.medela.models

class Histories {

    var drugName: String? = null
    var drugQty: String? = null
    var drugCost: String? = null
    var storeName: String? = null
    var datePurchased: String? = null
    var storeAddress: String? = null
    var storePhone: String? = null

    constructor()

    constructor(
        drugName: String?,
        drugQty: String?,
        drugCost: String?,
        storeName: String?,
        datePurchased: String?,
        storeAddress: String?,
        storePhone: String?
    ) {
        this.drugName = drugName
        this.drugQty = drugQty
        this.drugCost = drugCost
        this.storeName = storeName
        this.datePurchased = datePurchased
        this.storeAddress = storeAddress
        this.storePhone = storePhone
    }


}