package com.dicoding.anugrahzeputra.githubuserapp.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.anugrahzeputra.githubuserapp.adapters.SpAdapter
import com.dicoding.anugrahzeputra.githubuserapp.objectmodels.GithubUser
import com.dicoding.anugrahzeputra.githubuserapp.R
import com.dicoding.anugrahzeputra.githubuserapp.databinding.ActivityGithubUserDescBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GithubUserDesc : AppCompatActivity() {

    private lateinit var binding : ActivityGithubUserDescBinding

    companion object {
        const val EXTRA_GITHUBUSER = "extra_githubuser"
        private val TAB_TITLES = intArrayOf(
            R.string.fragmentfollowers,
            R.string.fragmentfollowing
        )

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_desc)

        binding = ActivityGithubUserDescBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val githubuser = intent.getParcelableExtra<GithubUser>(EXTRA_GITHUBUSER) as GithubUser
        Glide.with(this)
            .load(githubuser.photo)
            .apply(RequestOptions().override(350, 550))
            .into(binding.itemPhoto)
        binding.itemName.text = githubuser.name
        binding.userId.text = githubuser.id

        val userFollower = "Follower = " + githubuser.follower
        val userFollowing = "Following = " +  githubuser.following
        val userRepo = "Jumlah Repository = " + githubuser.repo
        val userLocation = "Location = " + githubuser.location
        val userCompany = "Company = " + githubuser.company

        binding.desc.text = userFollower
        binding.descFoll.text = userFollowing
        binding.descRepo.text = userRepo
        binding.location.text = userLocation
        binding.company.text = userCompany

        viewPagerShow()
    }

    private fun viewPagerShow() {
        val spAdapter = SpAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.vp2foll)
        viewPager.adapter = spAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) {
                tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }
}