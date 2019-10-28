package org.springframework.gigfinder.service

import Json4Kotlin_Base
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.gigfinder.gig.GigDetails
import org.springframework.gigfinder.gig.GigDetailsForm
import org.springframework.gigfinder.model.location.Json4Kotlin_LocationBase
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*


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

        var metroAreaId = getMetroAreaIdFromCurrentLocation(gigDetailsForm.currentLocation)

        getGigsFromMetroAreaId(metroAreaId)

        return gigDetailsForm

    }

    fun getMetroAreaIdFromCurrentLocation(currentLocation: String): Int {
        var locationUrl = appProperties.songkickLocationUrl
        locationUrl = locationUrl?.replace("param1", currentLocation)
        locationUrl = locationUrl?.replace("param2", appProperties.songkickApiKey)

        val request = Request.Builder()
                .get()
                .url(locationUrl!!)
                .build()

        var jsonAsString = ""

        val response = okHttpClient.newCall(request).execute()
        response.use {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            jsonAsString = response.body!!.string()
        }

        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_LocationBase::class.java)

        var metroAreaId = 0
        for (location in jsonObj.resultsPage.results.location) {
            if ("UK".equals(location.city.country.displayName)) {
                metroAreaId = location.metroArea.id
                break
            }
        }

        return metroAreaId
    }


    fun getGigsFromMetroAreaId(metroAreaId: Int) {
        var metroAreaUrl = appProperties.songkickMetroAreaUrl
        metroAreaUrl = metroAreaUrl?.replace("param1", metroAreaId.toString())
        metroAreaUrl = metroAreaUrl?.replace("param2", appProperties.songkickApiKey)

        val request = Request.Builder()
                .get()
                .url(metroAreaUrl!!)
                .build()

        var jsonAsString = ""

        val response = okHttpClient.newCall(request).execute()
        response.use {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            jsonAsString = response.body!!.string()
        }

        println(jsonAsString)

        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_Base::class.java)

        for (event in jsonObj.resultsPage.results.event) {
            println(event.displayName)
        }

    }

}
