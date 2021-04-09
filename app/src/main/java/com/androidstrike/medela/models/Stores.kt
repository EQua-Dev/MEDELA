package com.androidstrike.medela.models

class Stores {

    lateinit var active: String
    lateinit var address: String
    lateinit var phone: String
    lateinit var storename: String

    constructor()
    constructor(active: String, address: String, phone: String, storename: String) {
        this.active = active
        this.address = address
        this.phone = phone
        this.storename = storename
    }


}