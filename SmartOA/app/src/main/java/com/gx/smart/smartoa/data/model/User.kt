package com.gx.smart.smartoa.data.model

/**
 *@author xiaosy
 *@create 2019-10-24
 *@Describe
 **/
class User {

    var phone: String
    var password: String

    constructor(phone: String, password: String) {
        this.phone = phone
        this.password = password
    }


    override fun toString(): String {
        return "phone $phone password $password"
    }
}