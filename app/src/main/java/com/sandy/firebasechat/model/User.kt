package com.sandy.firebasechat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val fullName : String, val username: String, val profileImageUrl: String):Parcelable {
    constructor() : this("", "","", "")
}