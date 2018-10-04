package com.stehno.encounter

import com.stehno.encounter.controller.MainController
import com.stehno.jfx.Context

class ApplicationContext : Context() {

    init {

        register(MainController())
    }
}
