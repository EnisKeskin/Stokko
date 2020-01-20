package com.example.stockko.dataClass

class User {
    var userid: String = ""
    var username: String = ""
    var storeName: String = ""
    var storeAddress: String =""

    constructor(userid: String, username: String, storeName: String, storeAddress: String) {
        this.userid = userid
        this.username = username
        this.storeName = storeName
        this.storeAddress = storeAddress
    }

    constructor()

}