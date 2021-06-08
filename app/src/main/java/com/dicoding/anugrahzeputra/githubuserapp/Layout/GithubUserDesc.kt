package com.dicoding.anugrahzeputra.githubuserapp.Layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.anugrahzeputra.githubuserapp.ObjectModel.GithubUser
import com.dicoding.anugrahzeputra.githubuserapp.R

class GithubUserDesc : AppCompatActivity() {

    private lateinit var tvphotodesc : ImageView
    private lateinit var tvname : TextView
    private lateinit var tvid : TextView
    private lateinit var tvrepo : TextView
    private lateinit var tvlocation : TextView
    private lateinit var tvfoll : TextView
    private lateinit var tvfoll2 : TextView
    private lateinit var tvcompany : TextView

    companion object {
        const val EXTRA_GITHUBUSER = "extra_githubuser"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_desc)

        tvphotodesc = findViewById(R.id.photo)
        tvname = findViewById(R.id.itemName)
        tvid = findViewById(R.id.userId)
        tvfoll = findViewById(R.id.desc_foll)
        tvfoll2 = findViewById(R.id.desc_foll2)
        tvrepo = findViewById(R.id.desc_repo)
        tvlocation = findViewById(R.id.location)
        tvcompany = findViewById(R.id.company)


        val githubuser = intent.getParcelableExtra<GithubUser>(EXTRA_GITHUBUSER) as GithubUser
        Glide.with(this)
            .load(githubuser.photo)
            .apply(RequestOptions().override(350, 550))
            .into(tvphotodesc)
        tvname.text = githubuser.name
        tvid.text = githubuser.id

        val userFollower = "Follower = " + githubuser.follower
        val userFollowing = "Following = " +  githubuser.following
        val userRepo = "Jumlah Repository = " + githubuser.repo
        val userLocation = "Location = " + githubuser.location
        val userCompany = "Company = " + githubuser.company

        tvfoll.text = userFollower
        tvfoll2.text = userFollowing
        tvrepo.text = userRepo
        tvlocation.text = userLocation
        tvcompany.text = userCompany
    }
}