package tang.song.edu.uwaterloo.view.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import tang.song.edu.uwaterloo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener {
            val gameIntent = Intent(this, GameActivity::class.java)
            startActivity(gameIntent)
        }
    }
}
