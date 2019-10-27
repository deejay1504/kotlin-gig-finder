package org.springframework.gigfinder.service

import okhttp3.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.gigfinder.gig.GigDetails
import org.springframework.gigfinder.gig.GigDetailsForm
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class GigService {

    @Autowired
    lateinit var appProperties: AppProperties

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

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .get()
                .url(locationUrl)
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body
                println("body " + body)
            }
        })


        return gigDetailsForm
    }

//    fun getResponseWithGet(url: String?) : String
//    {
//
//        val request = Request.Builder()
//                .url(url)
//                .build()
//
//        return getOkHttpClient(request)
//    }

}
