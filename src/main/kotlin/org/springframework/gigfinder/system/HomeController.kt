/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.gigfinder.system

import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.gigfinder.service.VatService
import org.springframework.gigfinder.vat.VatDetailsForm
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class HomeController(private val vatService: VatService) {

    val VIEWS_VAT_CALC__FORM = "vat/vatCalcForm"

    @GetMapping("/")
    fun welcome(model: MutableMap<String, Any>): String {

        model["vatDetailsForm"] = vatService.createVatDetails()

        return VIEWS_VAT_CALC__FORM
    }

    @PostMapping("/gigfinder")
    fun vatCalculator(vatDetailsForm: VatDetailsForm, result: BindingResult, model: MutableMap<String, Any>): String {

        model["vatDetailsForm"] = vatService.calculateVat(vatDetailsForm)

        return VIEWS_VAT_CALC__FORM
    }
}
