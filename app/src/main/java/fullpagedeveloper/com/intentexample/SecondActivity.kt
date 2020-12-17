package fullpagedeveloper.com.intentexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
/**
 * @author maulanaahmad fullpagedeveloper@gmail.com
 */

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val text = intent?.getStringExtra(Intent.EXTRA_TEXT)
        Log.d("SecondActivity", "Text is $text")
    }
}
