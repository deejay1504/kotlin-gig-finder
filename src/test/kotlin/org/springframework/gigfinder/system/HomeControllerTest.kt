package org.springframework.gigfinder.system


import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.gigfinder.gig.GigDetails
import org.springframework.gigfinder.gig.GigDetailsForm
import org.springframework.gigfinder.service.GigService
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(HomeController::class)
class HomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var gigService: GigService

    private lateinit var gigDetailsForm: GigDetailsForm

    val VIEWS_GIG_DETAILS_FORM = "gig/gigDetailsForm"

    @BeforeEach
    fun setup() {
        gigDetailsForm = GigDetailsForm()
        gigDetailsForm.totalEntries = 1
        gigDetailsForm.resultsPerPage= 20
        gigDetailsForm.metroAreaId = 24555
        gigDetailsForm.gigLocation = "Brighton"
        gigDetailsForm.gigStartDate = "02-11-2019"
        gigDetailsForm.gigEndDate = "02-11-2019"
        gigDetailsForm.gigList = ArrayList<GigDetails>()
        given(gigService.createGigDetails()).willReturn(gigDetailsForm)
    }

    @Test
    fun shouldCreateGigDetailsForm() {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("gigDetailsForm"))
                .andExpect(view().name(VIEWS_GIG_DETAILS_FORM))
    }

    /**
    @Test
    fun shouldReturnGigListInForm() {

        mockMvc.perform(post("/gigfinder")
                        .param("totalEntries", "1")
                        .param("gigLocation", "Brighton")
                        .param("metroAreaId", "24555")
                        .param("gigStartDate", "02-11-2019")
                        .param("gigEndDate", "02-11-2019")
                        .param("currentPage", "02-11-2019")
                        .param("resultsPerPage", "20")
                        .param("numberOfPages", "0")
                )
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("gigDetailsForm"))
                .andExpect(model().attribute("gigDetailsForm", Matchers.hasProperty<Any>("gigStartDate", Matchers.`is`("Franklin"))))
                .andExpect(model().attribute("gigDetailsForm", Matchers.hasProperty<Any>("gigEndDate", Matchers.`is`("Franklin"))))
                .andExpect(view().name(VIEWS_GIG_DETAILS_FORM))
    }
    */

}
