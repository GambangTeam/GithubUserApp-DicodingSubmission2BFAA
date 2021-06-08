package com.dicoding.anugrahzeputra.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.anugrahzeputra.githubuserapp.Adapters.RvAdapter
import com.dicoding.anugrahzeputra.githubuserapp.Layout.GithubUserDesc
import com.dicoding.anugrahzeputra.githubuserapp.ObjectModel.GithubUser
import com.dicoding.anugrahzeputra.githubuserapp.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var errMsg : String
    private var list: ArrayList<GithubUser> = arrayListOf()
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvGithubapp.setHasFixedSize(true)

        getGithubData()
    }

    private fun getGithubData(){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=anugrah"
        client.addHeader("Authentication","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")
        client.addHeader("User-agent","request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val listUser = ArrayList<GithubUser>()
                    val result = responseBody?.let { String(it) }
                    Log.d(TAG, result!!)

                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val user = GithubUser()
                        user.name = username
                        user.avatar = avatar
                        listUser.add(user)
                        Log.d(TAG, username)
                    }
                    list.addAll(listUser)
                    showRecyclerList()
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                errMsg = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : NotFound"
                    else -> "$statusCode : Bad Request"
                }
                Log.d(TAG, errMsg)
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, errMsg, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
/*
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
    }*/

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
        intentIS.type = "text/plain"
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