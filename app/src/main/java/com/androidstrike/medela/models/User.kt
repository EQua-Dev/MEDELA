package com.androidstrike.medela.models

import java.net.Inet4Address

class User {

    lateinit var name:String
    var email: String? = null
    lateinit var phone: String
    lateinit var gender: String
    lateinit var dateOfBirth: String
    lateinit var blood_group: String
    lateinit var genotype: String
    lateinit var allergies: String
    lateinit var address: String
    lateinit var date_joined: String

    constructor(){

    }



    constructor(uid: String, email: String?)
    constructor(
        name: String,
        email: String?,
        phone: String,
        gender: String,
        dateOfBirth: String,
        blood_group: String,
        genotype: String,
        allergies: String,
        address: String,
        date_joined: String
    ) {
        this.name = name
        this.email = email
        this.phone = phone
        this.gender = gender
        this.dateOfBirth = dateOfBirth
        this.blood_group = blood_group
        this.genotype = genotype
        this.allergies = allergies
        this.address = address
        this.date_joined = date_joined
    }

}