package com.collisioncatcher.retrofit.entity

import org.bson.types.ObjectId
import java.time.LocalDateTime

public data class Hardware(val serialNo: String, val userName: String)

data class User(
    val userId: ObjectId?,
    val name: String?,
    val password: String?,
    val age: String?,
    val userName: String?,
    val phoneNo: String?,
    val hardware: Hardware?,
    val memberSince: String?,
    val membersList: List<Member>?
)

data class Member(
    val memberId: ObjectId?,
    val name: String,
    val phoneNo: String,
    val relation: String?
)

data class MapData(val latitude: Double, val longitude: Double)

data class Alert(
    val alertId : ObjectId?,
    val userName : String,
    val typeLabel : String,
    val location : String,
    val time : LocalDateTime
)

data class Speed(
    val speedId : String,
    val hardwareId : String,
    val speed : String,
    val timestamp : Int
)