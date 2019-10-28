package org.springframework.gigfinder.service

import Json4Kotlin_Base
import okhttp3.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.gigfinder.gig.GigDetails
import org.springframework.gigfinder.gig.GigDetailsForm
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.gson.Gson
import com.google.gson.JsonArray
import sun.security.jgss.GSSUtil.login


@Service
class GigService {

    @Autowired
    lateinit var appProperties: AppProperties

    // Create this as a singleton
    private val okHttpClient = OkHttpClient()


    fun createGigDetails(): GigDetailsForm {
        var gigList: ArrayList<GigDetails> = ArrayList<GigDetails>()
        gigList.add(GigDetails())
        gigList.add(GigDetails())
        gigList.add(GigDetails())

        var gigDetailsForm = GigDetailsForm()
        gigDetailsForm.gigList = gigList

        return gigDetailsForm
    }

    fun getGigs(gigDetailsForm: GigDetailsForm): GigDetailsForm {

        var gigList: ArrayList<GigDetails> = ArrayList<GigDetails>()

        var locationUrl = appProperties.songkickLocationUrl
        locationUrl = locationUrl.replace("param1", gigDetailsForm.currentLocation)
        locationUrl = locationUrl.replace("param2", appProperties.songkickApiKey)

        println("locationUrl " + locationUrl)

        val request = Request.Builder()
                .get()
                .url(locationUrl)
                .build()

        var jsonAsString = ""

        val response = okHttpClient.newCall(request).execute()
        response.use {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            jsonAsString = response.body!!.string()
        }

        println("body is " + jsonAsString)
        val topic = Gson().fromJson(jsonAsString, Json4Kotlin_Base::class.java)
        println("topic is " + topic)

        return gigDetailsForm
    }

}
