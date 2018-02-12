package pistalix.sadvideostatus.sadvideosong

import android.app.Fragment
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.home.view.*
import com.facebook.ads.AdView
import android.Manifest.permission.RECEIVE_BOOT_COMPLETED
import android.app.Activity
import android.support.v4.content.ContextCompat




class HomeFragment : Fragment() {
    lateinit var rootView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.home, container, false)
        rootView.icon4.setOnClickListener{

            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=Video+Status+%26+Photo+Editor+(Photo+Grid+%26+Collage)")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Video+Status+%26+Photo+Editor+(Photo+Grid+%26+Collage)")))
            }
        }

        rootView.icon5.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain*"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Bal Geet");
            intent.putExtra(Intent.EXTRA_TEXT, "https://goo.gl/DShthp");
            startActivity(Intent.createChooser(intent, "via"))
        }

        rootView.icon6.setOnClickListener{

            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pistalix.sadvideostatus.sadvideosong")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=pistalix.sadvideostatus.sadvideosong")))
            }
        }
        rootView.love.setOnClickListener{

            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pistalix.lovevideosongstatus.lovevideosong")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=pistalix.lovevideosongstatus.lovevideosong")))
            }
        }

        rootView.roma.setOnClickListener{

            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pistalix.romanticvideostatus.romanticvideosong")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=pistalix.romanticvideostatus.romanticvideosong")))
            }
        }
        rootView.trending.setOnClickListener{

            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pistalix.Videostatus.videosongstatus")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=pistalix.Videostatus.videosongstatus")))
            }
        }
        return rootView
    }

//    fun getGrantedPermissions(appPackage: String): List<String> {
//        val granted = ArrayList<String>()
//        try {
//            val pi =  rootView.context.pmackageManager.getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS)
//            for (i in pi.requestedPermissions.indices) {
//                if (pi.requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0) {
//                    granted.add(pi.requestedPermissions[i])
//                }
//            }
//        } catch (e: Exception) {
//        }
//
//        return granted
//    }
}