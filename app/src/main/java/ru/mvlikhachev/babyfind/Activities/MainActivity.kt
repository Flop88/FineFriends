package ru.mvlikhachev.babyfind.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import ru.mvlikhachev.babyfind.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    // Create menu
    override fun
            onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.sign_out -> {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                FirebaseAuth.getInstance().signOut()
                startActivity(intent)
                finish()

                true
            }
            R.id.about_program -> {
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                true
            }
//                R.id.settings_program -> {
//                    startActivity(Intent(this@MainActivity, SettingActivity::class.java))
//                    true
//                }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}