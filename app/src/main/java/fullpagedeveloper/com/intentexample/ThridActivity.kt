package fullpagedeveloper.com.intentexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.RemoteException
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_thrid.*
import java.util.regex.Pattern

/**
 * @author maulanaahmad fullpagedeveloper@gmail.com
 */

class ThridActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thrid)

        fun isInvalideNumber(s: String): Boolean{
            val regex = Pattern.compile("^(?=.*[0])(?=.*[A-Z]).{5,6}$")
            return regex.matcher(s).find() && s.length < 2
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
            //Toast.makeText(this, "izinkan", Toast.LENGTH_LONG).show()
            val snackbar = Snackbar.make(linear,"Izinkan" , Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(resources.getColor(R.color.colorAccent))
            snackbar.show()

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                Toast.makeText(this, "kontak tidak di akses sama sekali", Toast.LENGTH_SHORT).show()

            } else {
                //request permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
                //Toast.makeText(this, "Permission di izinkan", Toast.LENGTH_LONG).show()
                val snackbaraja = Snackbar.make(linear,"Permission di izinkan" , Snackbar.LENGTH_SHORT)
                snackbaraja.setBackgroundTint(resources.getColor(R.color.colorAccent))
                snackbaraja.show()
            }
        } else {
            //for lower than masmellow version
            //to get phonebook
            getPhoneBook()
            Log.d("contact", "contact1")
        }
    }

    private fun getPhoneBook() {

            try {

                val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
                //to pas all phonebook to cursor

                val arrayList = arrayListOf<String>()
                //to fatch all the contact from cursor
                while (cursor?.moveToNext()!!) {
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    //add data into arraylist
                    arrayList.add("# $name $phone \n")
                }
                cursor.close()

                Handler().postDelayed({

                    //attact the arraylist into textview
                    textViewPhoneBook.text = arrayList.toString()
                    Log.d("contact", "$arrayList")

                }, 500)

            }catch (e: RuntimeException){

                Log.e("getContentResolver()",Log.getStackTraceString(e))

            }catch (e: SecurityException) {

                Log.e("getContentResolver()",Log.getStackTraceString(e))

            } catch (e: RemoteException) {

                Log.e("getContentResolver()", Log.getStackTraceString(e))
            }

//        val cr = contentResolver
//        val cur = cr.query(
//            ContactsContract.Contacts.CONTENT_URI,
//            null, null, null, null
//        )
//
//        if (cur?.count ?: 0 > 0) {
//            while (cur != null && cur.moveToNext()) {
//                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
//                val name = cur.getString(
//                    cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
//                )
//                if (cur.getInt(
//                        cur.getColumnIndex(
//                            ContactsContract.Contacts.HAS_PHONE_NUMBER
//                        )
//                    ) > 0
//                ) {
//                    val pCur = cr.query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                        arrayOf(id),
//                        null
//                    )
//                    while (pCur!!.moveToNext()) {
//                        val phoneNo = pCur!!.getString(
//                            pCur!!.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Phone.NUMBER
//                            )
//                        )
//                        Log.i("tes", "Name: $name")
//                        Log.i("tes", "Phone Number: $phoneNo")
//                    }
//                    pCur!!.close()
//                }
//            }
//        }
//        cur?.close()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getPhoneBook()
                    val snackbar = Snackbar.make(linear,"Phonebook Permission Granted" , Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(resources.getColor(R.color.design_default_color_primary_variant))
                    snackbar.show()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    val snackbar = Snackbar.make(linear,"Phonebook Permission Denied" , Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(resources.getColor(R.color.design_default_color_error))
                    snackbar.show()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
                val snackbar = Snackbar.make(linear,"Phonebook Permission di tolak" , Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(resources.getColor(R.color.design_default_color_error))
                snackbar.show()
            }
        }
    }
}
