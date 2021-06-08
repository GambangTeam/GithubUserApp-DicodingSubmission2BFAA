package com.dicoding.anugrahzeputra.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.anugrahzeputra.githubuserapp.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var list: ArrayList<GithubUser> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvGithubapp.setHasFixedSize(true)

        list.addAll(githubuserData())
        showRecyclerList()
    }

    private fun githubuserData(): ArrayList<GithubUser> {
        val name = resources.getStringArray(R.array.name)
        val userName = resources.getStringArray(R.array.username)
        val location = resources.getStringArray(R.array.location)
        val repository = resources.getStringArray(R.array.repository)
        val company = resources.getStringArray(R.array.company)
        val followers = resources.getStringArray(R.array.followers)
        val following = resources.getStringArray(R.array.following)
        val avatar = resources.obtainTypedArray(R.array.avatar)

        val listgithubuser = ArrayList<GithubUser>()
        for (position in name.indices) {
            val githubuser = GithubUser()
            githubuser.name = name[position]
            githubuser.id = userName[position]
            githubuser.location = location[position]
            githubuser.repo = repository[position]
            githubuser.company = company[position]
            githubuser.follower = followers[position]
            githubuser.following = following[position]
            githubuser.photo = avatar.getResourceId(position, 0)
            listgithubuser.add(githubuser)
        }
        avatar.recycle()
        return listgithubuser
    }

    private fun showRecyclerList() {
        binding.rvGithubapp.layoutManager = LinearLayoutManager(this)
        val cardViewAdapter = RvAdapter(list)
        binding.rvGithubapp.adapter = cardViewAdapter
        cardViewAdapter.setItemInClick(object : RvAdapter.ItemInClickCallback {
            override fun itemClicked(data: GithubUser) = intentMovetoDetail(data)
            override fun buttonClicked(data: GithubUser) = intentImplicitSEND(data)
        })
    }

    private fun intentImplicitSEND(data: GithubUser) {
        val intentIS = Intent(Intent.ACTION_SEND)
        intentIS.setType("text/plain")
        val content = "Ini nih user github yang aku mau share" +
                "\nNamanya " + data.name +
                "\nuseridnya " + data.id +
                "\nfollowernya " + data.follower +
                "\nyang dia follow " + data.following +
                "\nreponya ada " + data.repo + " buah" +
                "\ndia ada di " + data.location +
                "\ndia bekerja di " + data.company
        intentIS.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(intentIS)
    }

    private fun intentMovetoDetail(data: GithubUser) {
        val intentMD = Intent(this@MainActivity, GithubUserDesc::class.java)
        intentMD.putExtra(GithubUserDesc.EXTRA_GITHUBUSER, data)
        startActivity(intentMD)
    }
}