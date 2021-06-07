package com.dicoding.anugrahzeputra.githubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubUser(
    var name: String? = "",
    var id: String? = "",
    var follower: String? = "",
    var following: String? = "",
    var location: String? = "",
    var company: String = "",
    var repo: String? = "",
    var photo: Int = 0
) : Parcelable
