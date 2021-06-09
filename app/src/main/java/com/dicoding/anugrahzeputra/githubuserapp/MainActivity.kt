package com.dicoding.anugrahzeputra.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView.OnQueryTextListener
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

        getGithubData()
        searchGithubUser()
    }

    private fun searchGithubUser() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    list.clear()
                    getUserSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun getUserSearch(text: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$text"
        client.addHeader("Authentication", "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")
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
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val photo = item.getString("avatar_url")
                        val user = GithubUser()
                        user.name = username
                        user.photo = photo
                        listUser.add(user)
                        Log.d(TAG, username)
                    }
                    list.addAll(listUser)
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

    private fun getGithubData() {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader("Authentication", "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")
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
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val photo = item.getString("avatar_url")
                        val user = GithubUser()
                        user.name = username
                        user.photo = photo
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
        return true
    }
}