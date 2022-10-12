package com.example.eventer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fragments.AccountFragment
import fragments.ChatsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val chatsFragment = ChatsFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.background=null
        bottomNavigationView.menu.getItem(1).isEnabled = false

        val activity_nr = intent.getIntExtra("activity", 0)

        if (activity_nr == 2) {
            bottomNavigationView.menu.getItem(2).setChecked(true)
            replaceFragment(accountFragment)
        }
        else {
            replaceFragment(chatsFragment)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_chats -> replaceFragment(chatsFragment)
                R.id.ic_account -> replaceFragment(accountFragment)
            }
            true
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, CreateEvent::class.java)        //!!!!!!!!! change myfriends to my girlfriend
            startActivity(intent)
            finish()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}