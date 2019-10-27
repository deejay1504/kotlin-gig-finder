package org.springframework.gigfinder.service

import org.springframework.stereotype.Service
import org.springframework.gigfinder.gig.GigDetails
import org.springframework.gigfinder.gig.GigDetailsForm
import java.util.*

@Service
class GigService {

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
        return gigDetailsForm
    }

}
