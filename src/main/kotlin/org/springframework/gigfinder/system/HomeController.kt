package org.springframework.gigfinder.system

import org.springframework.gigfinder.gig.GigDetailsForm
import org.springframework.gigfinder.service.GigService
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Controller
class HomeController(private val gigService: GigService) {

    val VIEWS_GIG_DETAILS_FORM = "gig/gigDetailsForm"

    @GetMapping("/")
    fun welcome(model: MutableMap<String, Any>): String {

        model["gigDetailsForm"] = gigService.createGigDetails()

        return VIEWS_GIG_DETAILS_FORM
    }

    @PostMapping("/gigfinder")
    fun getGigs(@Valid gigDetailsForm: GigDetailsForm, result: BindingResult, model: MutableMap<String, Any>): String {
//    fun getGigs(gigDetailsForm: GigDetailsForm, model: MutableMap<String, Any>): String {

        model["gigDetailsForm"] = gigService.getGigs(gigDetailsForm)

        model["noRecordsFound"] = (gigDetailsForm.totalEntries == 0)

        return VIEWS_GIG_DETAILS_FORM
    }

}
