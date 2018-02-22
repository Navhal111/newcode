package mitpi.sadvideostatus.sadvideosong

import android.os.Environment
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.ArrayList


class SingelFunction {

    fun DounloadVideos(): JSONArray {
        var VideoId = JSONArray()
        val externalDirectory = Environment.getExternalStorageDirectory().toString()
        val files = File(externalDirectory+ "/SadStatusNew/")
        val inFiles = ArrayList<File>()
        val fileslist = files.listFiles()

        if(fileslist != null){
            for (file in fileslist) {
                if(file.length()>0 && file.extension == "mp4"){
                    inFiles.add(file)
                    var TestId = JSONObject()
                    var Teatarray = file.name.split(" $ ")
                    if(Teatarray.size > 1 && Teatarray[1]!= "mp3"){
                        TestId.put("Id",Teatarray[1].replace(".mp4",""))
                        VideoId.put(TestId)
                    }
                }


            }
            return VideoId
        }
        return VideoId
    }

    fun DounloadVideosName(): ArrayList<File> {
        val externalDirectory = Environment.getExternalStorageDirectory().toString()
        val files = File(externalDirectory+ "/SadStatusNew/")
        val inFiles = ArrayList<File>()
        if(files.exists()){
            val fileslist = files.listFiles()
            if(fileslist != null){
                for (file in fileslist){
                    var Teatarray = file.name.split(" $ ")
                    if(file.length()>0 && file.extension == "mp4" && Teatarray[1]!= "mp3"){
                        inFiles.add(file)
                    }

                }
                return inFiles
            }
            return inFiles
        }
        return inFiles
    }


}