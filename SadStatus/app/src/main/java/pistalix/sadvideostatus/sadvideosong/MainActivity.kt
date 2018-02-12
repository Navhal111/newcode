package pistalix.sadvideostatus.sadvideosong

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import com.ss.bottomnavigation.BottomNavigation
import gcm.RegistrationIntentService
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    var fragmentTab4: Fragment = FregmentStatus()
    var fragmentTab1: Fragment = VideosList()
    var fragmentTab3: Fragment = DownloadVideos()
    var fragmentTab2: Fragment = HomeFragment()
    lateinit var dailog: DialogPlus
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private val REQUEST_WRITE_EXTERNAL_STORAGE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val p = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
//        if (p == PackageManager.PERMISSION_GRANTED) {
//            toast("Pwemission")
//        }
//        val CALC_PACKAGE_NAME = "com.miui.securitycenter"
//        val CALC_PACKAGE_ACITIVITY = "com.miui.permcenter.autostart.AutoStartManagementActivity"
//        toast(android.os.Build.MANUFACTURER)
//        if (android.os.Build.MANUFACTURER.toLowerCase() == "xiaomi") {
//            var intent = Intent()
//            intent.component = ComponentName(CALC_PACKAGE_NAME, CALC_PACKAGE_ACITIVITY);
//            intent.putExtra("extra_pkgname", "pistalix.sadvideostatus.sadvideosong");
//            startActivity(intent)
//        }

        val check = (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        if (!check) {
            ActivityCompat.requestPermissions(this as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE)
        }

        try{
            var check_update = intent.getStringExtra("update");
            var msg = intent.getStringExtra("msg")
        if(check_update == "1"){
            update_app(msg)
        }
        }catch (e:NullPointerException){

        }catch (e:IllegalArgumentException){

        }catch (e:Exception){
        }

        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigation
        bottomNavigation.defaultItem =1
        bottomNavigation.setOnSelectedItemChangeListener { itemId ->
            when (itemId) {
                R.id.tab_home -> {
                    var transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container,fragmentTab2)
                    transaction.addToBackStack(null)
                    transaction.commit()

                }
                R.id.tab_images -> {
                    var transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container,fragmentTab1)
                    transaction.addToBackStack(null)
                    transaction.commit()

                }
                R.id.tab_camera -> {

                    var transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container,fragmentTab4)
                    transaction.addToBackStack(null)
                    transaction.commit()

                }
                R.id.tab_products -> {

                    var transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container,fragmentTab3)
                    transaction.addToBackStack(null)
                    transaction.commit()

                }

            }
        }
        if (checkPlayServices()) {
                val intent = Intent(this, RegistrationIntentService::class.java)
                startService(intent)
         }

    }

    override fun onBackPressed(){

            startActivity(Intent(this@MainActivity, ListApp::class.java))
    }

    fun update_app(str:String){

        dailog = DialogPlus.newDialog(this).setGravity(Gravity.CENTER).setContentHolder(ViewHolder(R.layout.update_app)).setInAnimation(R.anim.abc_fade_in).create()
        try{

            var yes = dailog.findViewById(R.id.yes_button)
            var no = dailog.findViewById(R.id.no_button)
            var msg :TextView  = dailog.findViewById(R.id.update_msg) as TextView
            msg.text =str
            yes.setOnClickListener{
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pistalix.sadvideostatus.sadvideosong")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=pistalix.sadvideostatus.sadvideosong")))
                }
            }
            no.setOnClickListener{
                try{
                    dailog.dismiss()
                }catch (e :NullPointerException){

                    toast("error")
                }catch (e:IllegalArgumentException){
                    toast("error")
                }


            }
            dailog.show()
        }catch (e :NullPointerException){

        toast("error")
        }catch (e:IllegalArgumentException){
            toast("error")
        }

    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show()
            } else {
                //                Log.i(TAG, "This device is not supported.");
                finish()
            }
            return false
        }
        return true
    }




}