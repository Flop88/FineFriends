package ru.mvlikhachev.babyfind.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid: String?,
    var displayName: String,
    var email: String?,
    var role: String
) {

}