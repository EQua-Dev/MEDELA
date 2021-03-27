package com.androidstrike.medela.interfaces

interface IFirebaseLoadDone {
    fun onFirebaseUserDone (lstEmail:List<String>)
    fun onFirebaseLoadFailed(message: String)
}