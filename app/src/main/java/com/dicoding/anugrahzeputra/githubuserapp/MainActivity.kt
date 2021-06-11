package com.dicoding.anugrahzeputra.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import com.dicoding.anugrahzeputra.githubuserapp.adapters.RvAdapter
import com.dicoding.anugrahzeputra.githubuserapp.layouts.GithubUserDesc
import com.dicoding.anugrahzeputra.githubuserapp.objectmodels.GithubUser
import com.dicoding.anugrahzeputra.githubuserapp.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var errMsg: String
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

        supportActionBar?.title = "Github Users Search"

        //recyclerViewConfig()
        getGithubUsers()
        searchGithubUser()
    }

    private fun searchGithubUser() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    getGithubUsers()
                    //return true
                } else {
                    list.clear()
                    getUserSearch(query)
                    binding.progressBar.visibility = View.VISIBLE
                    //return true
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    getGithubUsers()
                    binding.progressBar.visibility = View.VISIBLE
                    //return true
                } else {
                    getUserSearch(newText)
                    //return false
                    //return true
                }
                return true
            }
        })
    }

    private fun getUserSearch(text: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$text"
        //client.addHeader("Authorization", "token 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")
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    list.clear()
                    val result = responseBody?.let { String(it) }
                    Log.d(TAG, result!!)

                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val userlogin = item.getString("login")
                        Log.d(TAG, userlogin)
                        getGithubData(userlogin)
                    }
                    showRecyclerList()
                    binding.progressBar.visibility = View.INVISIBLE
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
                binding.progressBar.visibility = View.INVISIBLE
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

    private fun getGithubUsers() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        //client.addHeader("Authorization", "token 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")
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    list.clear()
                    val result = responseBody?.let { String(it) }
                    Log.d(TAG, result!!)

                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val userlogin = item.getString("login")
                        Log.d(TAG, userlogin)
                        getGithubData(userlogin)
                    }
                    Log.d(TAG, list.toString())
                    showRecyclerList()
                    binding.progressBar.visibility = View.INVISIBLE
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
                binding.progressBar.visibility = View.INVISIBLE
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

    private fun getGithubData(userlogin: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$userlogin"
        //client.addHeader("Authorization", "token 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")
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-agent", "request")
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
                    val username = responseObject.getString("name")
                    val userid = responseObject.getString("login")
                    val photo = responseObject.getString("avatar_url")
                    val followers = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")
                    val repo = responseObject.getInt("public_repos")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("company")
                    val user = GithubUser()
                    user.name = username
                    user.id = userid
                    user.photo = photo
                    user.follower = followers.toString()
                    user.following = following.toString()
                    user.repo = repo.toString()
                    user.location = location
                    user.company = company

                    listUser.add(user)
                    Log.d(TAG, username)

                    list.addAll(listUser)
                    //showRecyclerList()
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
    private fun recyclerViewConfig() {
        binding.rvGithubapp.layoutManager = LinearLayoutManager(binding.rvGithubapp.context)
        binding.rvGithubapp.setHasFixedSize(true)
        binding.rvGithubapp.addItemDecoration(DividerItemDecoration(
            binding.rvGithubapp.context, DividerItemDecoration.VERTICAL
        ))
    }
*/
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuitem, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.language) {
            val lIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(lIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}