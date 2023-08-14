package com.martinus.gitfriends.walktought

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.martinus.gitfriends.R

class walktroughtActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var walktroughA: walktroughA


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walktrought)

        viewPager = findViewById(R.id.viewPager)

        val fragments = listOf(
            walktrought(),

            )

        walktroughA = walktroughA(this, fragments)
        viewPager.adapter = walktroughA

        // Tambahkan kode lain yang diperlukan untuk mengatur tampilan WalkthroughActivity
    }
}
