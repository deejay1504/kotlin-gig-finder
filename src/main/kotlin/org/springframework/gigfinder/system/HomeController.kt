package org.springframework.gigfinder.system

import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.gigfinder.service.GigService
import org.springframework.gigfinder.gig.GigDetailsForm
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class HomeController(private val gigService: GigService) {


    val VIEWS_GIG_DETAILS_FORM = "gig/gigDetailsForm"

    @GetMapping("/")
    fun welcome(model: MutableMap<String, Any>): String {

        model["gigDetailsForm"] = gigService.createGigDetails()


        return VIEWS_GIG_DETAILS_FORM
    }

    @PostMapping("/gigfinder")
    fun getGigs(gigDetailsForm: GigDetailsForm, result: BindingResult, model: MutableMap<String, Any>): String {

        model["gigDetailsForm"] = gigService.getGigs(gigDetailsForm)

        return VIEWS_GIG_DETAILS_FORM
    }

}
