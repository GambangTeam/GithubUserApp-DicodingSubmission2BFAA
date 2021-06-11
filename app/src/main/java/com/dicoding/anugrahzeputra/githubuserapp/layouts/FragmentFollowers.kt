package com.dicoding.anugrahzeputra.githubuserapp.layouts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.anugrahzeputra.githubuserapp.R
import com.dicoding.anugrahzeputra.githubuserapp.adapters.RvAdapter
import com.dicoding.anugrahzeputra.githubuserapp.objectmodels.GithubUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class FragmentFollowers : Fragment() {
    //private lateinit var binding: FragmentFollowersBinding
    private lateinit var rvFragment: RecyclerView
    private lateinit var errMsg: String
    private lateinit var progressBar: ProgressBar
    private var list: ArrayList<GithubUser> = arrayListOf()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }
    
    companion object {
        private val TAG = FragmentFollowers::class.java.simpleName
        const val EXTRA_GITHUBUSER = "extra_githubuser"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.followersProgressBar)
        rvFragment = view.findViewById(R.id.rvFollowers)
        rvFragment.setHasFixedSize(true)

        val dataImport = requireActivity().intent.getParcelableExtra<GithubUser>(EXTRA_GITHUBUSER) as GithubUser
        getUserFollowers(dataImport.id.toString())
    }

    private fun getUserFollowers(name: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$name/followers"
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

                    val responseObject = JSONArray(result)
                    for (i in 0 until responseObject.length()){
                        val jsonObject = responseObject.getJSONObject(i)
                        val userName: String = jsonObject.getString("login")
                        createUserData(userName)
                    }
                    Log.d(TAG, list.toString())
                    showRecyclerList()
                    progressBar.visibility = View.INVISIBLE
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
                progressBar.visibility = View.VISIBLE
                errMsg = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : NotFound"
                    else -> "$statusCode : Bad Request"
                }
                Log.d(TAG, errMsg)
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(activity, errMsg, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun createUserData(name: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$name"
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
                    Toast.makeText(activity, errMsg, Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    private fun showRecyclerList() {
        rvFragment.layoutManager = LinearLayoutManager(activity)
        val cardViewAdapter = RvAdapter(list)
        rvFragment.adapter = cardViewAdapter
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
        val intentMD = Intent(activity, GithubUserDesc::class.java)
        intentMD.putExtra(GithubUserDesc.EXTRA_GITHUBUSER, data)
        startActivity(intentMD)
    }
}