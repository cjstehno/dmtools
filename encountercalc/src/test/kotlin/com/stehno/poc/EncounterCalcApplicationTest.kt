package com.stehno.poc

import com.stehno.encounter.ApplicationContext
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxAssert.verifyThat
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
import org.testfx.matcher.control.LabeledMatchers.hasText

@Tag("ui") @ExtendWith(ApplicationExtension::class)
internal class EncounterCalcApplicationTest {

    @Start fun start(stage: Stage) {
        val context = ApplicationContext()
        val parent = context.view<Parent>("/ui/main.fxml")
        val scene = Scene(parent)
        stage.scene = scene
        stage.show()
        stage.toFront()
    }

    @Test fun `should say hello`() {
        verifyThat(".label", hasText("Hello"))
    }
}