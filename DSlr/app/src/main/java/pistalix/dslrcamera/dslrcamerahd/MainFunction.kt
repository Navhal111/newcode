package pistalix.dslrcamera.dslrcamerahd
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.widget.TextView
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import org.jetbrains.anko.toast


class MainFunction {
    lateinit var dailog: DialogPlus
    fun update_app(str:String,contxt:Context){

        dailog = DialogPlus.newDialog(contxt).setGravity(Gravity.CENTER).setContentHolder(ViewHolder(R.layout.update_app)).setInAnimation(R.anim.abc_fade_in).create()
        try{

            var yes = dailog.findViewById(R.id.yes_button)
            var no = dailog.findViewById(R.id.no_button)
            var msg : TextView = dailog.findViewById(R.id.update_msg) as TextView
            msg.text =str
            yes.setOnClickListener{
                try {
                    contxt.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pistalix.dslrcamera.dslrcamerahd")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    contxt.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=pistalix.dslrcamera.dslrcamerahd")))
                }
            }
            no.setOnClickListener{
                try{
                    dailog.dismiss()
                }catch (e :NullPointerException){

                    contxt.toast("error")
                }catch (e:IllegalArgumentException){
                    contxt.toast("error")
                }


            }
            dailog.show()
        }catch (e :NullPointerException){

            contxt.toast("error")
        }catch (e:IllegalArgumentException){
            contxt.toast("error")
        }

    }
}