package com.androidstrike.medela.models

class Drugs {

    lateinit var adult: String
    lateinit var children: String
    lateinit var manufacturer: String
    lateinit var image: String
    lateinit var name: String

    constructor()
    constructor(adult: String, children: String, manufacturer: String, image: String, name: String) {
        this.adult = adult
        this.children = children
        this.manufacturer = manufacturer
        this.image = image
        this.name = name
    }

}