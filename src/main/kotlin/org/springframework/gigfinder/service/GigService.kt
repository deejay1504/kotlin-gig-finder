package org.springframework.gigfinder.service

import Json4Kotlin_Base
import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.gigfinder.gig.GigDetails
import org.springframework.gigfinder.gig.GigDetailsForm
import org.springframework.gigfinder.model.location.Json4Kotlin_LocationBase
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class GigService {

    @Autowired
    lateinit var appProperties: AppProperties

    // Create this as a singleton
    private val okHttpClient = OkHttpClient()


    fun createGigDetails(): GigDetailsForm {

        return GigDetailsForm()
    }

    fun getGigs(gigDetailsForm: GigDetailsForm): GigDetailsForm {

        var metroAreaId = gigDetailsForm.metroAreaId

        if (metroAreaId == 0) {
            metroAreaId = getMetroAreaIdFromCurrentLocation(gigDetailsForm.gigLocation)
        }

        var returnedGigDetailsForm = getGigsFromMetroAreaId(metroAreaId, gigDetailsForm)

        return returnedGigDetailsForm

    }

    fun getMetroAreaIdFromCurrentLocation(gigLocation: String): Int {
        var locationUrl = appProperties.songkickLocationUrl
        locationUrl = locationUrl?.replace("param1", gigLocation)
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
        if (jsonObj.resultsPage.totalEntries > 0) {
            for (location in jsonObj.resultsPage.results.location) {
                if ("UK".equals(location.city.country.displayName)) {
                    metroAreaId = location.metroArea.id
                    break
                }
            }
        }

        return metroAreaId
    }


    fun getGigsFromMetroAreaId(metroAreaId: Int, gigDetailsForm: GigDetailsForm): GigDetailsForm {
        var todaysDate = java.time.LocalDate.now().toString();
        var gigStartDate = gigDetailsForm.gigStartDate
        var gigEndDate = gigDetailsForm.gigEndDate
        var startDate = gigStartDate.substring(6) + "-" + gigStartDate.substring(3,5) + "-" + gigStartDate.substring(0,2)
        var endDate   = gigEndDate.substring(6) + "-" + gigEndDate.substring(3,5) + "-" + gigEndDate.substring(0,2)
        var songkickMetroAreaUrl  = appProperties.songkickMetroAreaUrl
        songkickMetroAreaUrl = songkickMetroAreaUrl!!.replace("metro_area_id", metroAreaId.toString())

        val metroAreaUrl = songkickMetroAreaUrl.toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("apikey", appProperties.songkickApiKey)
                .addQueryParameter("min_date", startDate)
                .addQueryParameter("max_date", endDate)
                .addQueryParameter("page", gigDetailsForm.currentPage.toString())
                .addQueryParameter("per_page", gigDetailsForm.resultsPerPage.toString())
                .build()

        val request = Request.Builder()
                .get()
                .url(metroAreaUrl)
                .build()

        var jsonAsString = ""

        val response = okHttpClient.newCall(request).execute()
        response.use {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            jsonAsString = response.body!!.string()
        }

        var gigList: ArrayList<GigDetails> = ArrayList<GigDetails>()
        var sortedGigList: List<GigDetails> = ArrayList<GigDetails>()

        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_Base::class.java)
        val df = DateTimeFormatter.ofPattern("dd-MMM-yyyy")

        if (jsonObj.resultsPage.totalEntries > 0) {
            gigDetailsForm.totalEntries = jsonObj.resultsPage.totalEntries
            gigDetailsForm.currentPage = jsonObj.resultsPage.page
            gigDetailsForm.numberOfPages = gigDetailsForm.totalEntries / gigDetailsForm.resultsPerPage
            var extraPage = gigDetailsForm.totalEntries % gigDetailsForm.resultsPerPage
            if (extraPage > 0) {
                gigDetailsForm.numberOfPages = gigDetailsForm.numberOfPages + 1
            }
            for (event in jsonObj.resultsPage.results.event) {
                var gigDetails: GigDetails = GigDetails()

                val artistName = StringBuilder()
                val artistNameArray = event.displayName.split(" ")
                var wordIndex = artistNameArray.indexOf("at") - 1
                for (i in 0..wordIndex) {
                    artistName.append(artistNameArray[i]).append(" ")
                }

                gigDetails.artist = artistName.toString().trim()
                gigDetails.venue = event.venue.displayName
                gigDetails.location = event.location.city
                gigDetails.startDate = LocalDate.parse(event.start.date).format(df).toString()
                gigDetails.songkickUrl = event.uri
                gigDetails.gigToday = if (todaysDate.equals(event.start.date)) true else false
                if (!event.start.time.isNullOrBlank()) {
                    gigDetails.startTime = event.start.time.substring(0, 5)
                }

                gigList.add(gigDetails)
            }
            var pageNumbers: ArrayList<Int> = ArrayList<Int>()
            for (pageNo in 1 ..gigDetailsForm.numberOfPages) {
                pageNumbers.add(pageNo)
            }
            gigDetailsForm.pageNumbers = pageNumbers

            sortedGigList = gigList.sortedWith(compareBy({it.startDate}, {it.artist}))
        }

        gigDetailsForm.gigList = sortedGigList
        gigDetailsForm.metroAreaId = metroAreaId

        return gigDetailsForm

    }

}
