package fullpagedeveloper.com.intentexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author maulanaahmad fullpagedeveloper@gmail.com
 */

class MainActivity : AppCompatActivity() {

    companion object {
        val REQUEST_SELECT_PHONE_NUMBER = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.linkAaja_Buttom).setOnClickListener {
            //explisit intent
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.implisitIntent).setOnClickListener {
            //implisit intent
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hello world") //text yang di kirim
                type =
                    "text/plain" //MIMEType atau jenis mime : text/*, pengirim akan sering mengirim text/plain, text/rtf, text/html, text/json
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.implisitIntentInDetail).setOnClickListener {
            //implisit intent in detail
            val intent = Intent().apply {
                //action = "an intent action that does no exist" //menguji jika tidak ada aplikasi yang merespond
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hello world")
                type = "text/rtf"
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Log.d("MainActivity", "resolveActivity Is Null")
                Toast.makeText(this, "Tidak ada aplikasi", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.implisitIntentInChooseTitle).setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hello world")
                type = "text/plain"
            }

            val chooser = Intent.createChooser(intent, "Sher text with")
            startActivity(chooser)
        }

        findViewById<Button>(R.id.implisitIntentInFilterManifest).setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hello world")
                type = "text/plain"
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        //if apps don't exist go to playstore if there are apps open the application
        findViewById<Button>(R.id.implisitIntentToPlaystoreOrOpenAppas).setOnClickListener {
            val packageName = "com.telkom.mwallet"

            var intent = packageManager.getLaunchIntentForPackage(packageName)
            if (intent?.resolveActivity(packageManager) != null) {
                intent = Intent(Intent.ACTION_VIEW).apply {
                }
                startActivity(intent)
            } else {
                intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://play.google.com/store/apps/details?id=${packageName}")
                }
                startActivity(intent)
            }
        }

        //insert a contact
        findViewById<Button>(R.id.insetAContact).setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                type = ContactsContract.Contacts.CONTENT_TYPE
                putExtra(ContactsContract.Intents.Insert.NAME, "Maulana")
                putExtra(ContactsContract.Intents.Insert.EMAIL, "fullpagedeveloper@gmail.com")
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER)
            }
        }


        //read contact
        findViewById<Button>(R.id.readAContact).setOnClickListener {

            showContactPermission()

            val intent = Intent(Intent.ACTION_PICK).apply {
                type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER)
            }
        }

        //read all contact
        findViewById<Button>(R.id.readAllContact).setOnClickListener {
            startActivity(Intent(Intent(this, ThridActivity::class.java)))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {

            val contactUri: Uri = data?.data ?: return

            val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

            contentResolver.query(contactUri, projection, null, null, null).use { cursor ->
                //if the cursor return is valid, get the phone
                if (cursor?.moveToFirst()!!) {
                    val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val name = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val number = cursor.getString(numberIndex)
                    editTextPhoneNumber.setText(number)
                    Log.d("MainActivitu", "Detai : $number # ${name}")
                }
            }
        }
    }

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    fun showContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            //Tidak diperlukan penjelasan, kami dapat meminta izin
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)

        } else {

            //getContat()
        }
    }

    //menanggapi respon permintaan izin sistem memanggil onRequestPermissionsResult
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) { PERMISSIONS_REQUEST_READ_CONTACTS -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //getContat()
                }
            }
        }
    }

    /*private fun getContat() {

        val contactUri: Uri? = null

        val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

        contactUri?.let {
            contentResolver.query(it, projection, null, null, null).use { cursor ->
                //if the cursor return is valid, get the phone
                if (cursor?.moveToFirst()!!) {
                    val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val number = cursor.getString(numberIndex)

                    editTextPhoneNumber.setText(number)
                    Log.d("MainActivitu", "Detail : $number")
                }
            }
        }
    }*/
}
