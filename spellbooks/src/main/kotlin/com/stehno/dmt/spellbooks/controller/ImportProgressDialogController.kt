package com.stehno.dmt.spellbooks.controller

import com.stehno.dmt.spellbooks.data.StoreService
import com.stehno.dmt.spellbooks.dsl.Spellbook
import com.stehno.dmt.spellbooks.dsl.SpellbookLoader
import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextArea
import javafx.scene.image.ImageView
import java.io.File
import java.util.concurrent.CountDownLatch

class ImportProgressDialogController(private val storeService: StoreService) {

    lateinit var importProgressDialog: Dialog<Void>
    lateinit var progressBar: ProgressBar
    lateinit var progressText: TextArea

    fun initialize() {
        importProgressDialog.graphic = ImageView(javaClass.getResource("/img/book-cover-64.png").toString())
        importProgressDialog.dialogPane.lookupButton(ButtonType.CLOSE).isDisable = true
    }

    fun load(spellbookFile: File) {
        SpellbookLoader.load(spellbookFile).thenAccept { book: Spellbook ->
            progressText.appendText("Spellbook \"${book.title}\" (${spellbookFile.name}) loaded...\nStoring ${book.spells.size} spells...\n")

            val latch = CountDownLatch(book.spells.size)

            book.spells.forEach { sp ->
                Platform.runLater {
                    storeService.importSpell(sp)
                    progressText.appendText("Loaded \"${sp.name}\"..\n")
                    progressBar.progress = (book.spells.size / latch.count).toDouble()
                    latch.countDown()
                }
            }

            latch.await()

            Platform.runLater {
                progressText.appendText("Done.")
                progressBar.progress = 1.0
                importProgressDialog.dialogPane.lookupButton(ButtonType.CLOSE).isDisable = false
                storeService.importComplete()
            }
        }
    }
}