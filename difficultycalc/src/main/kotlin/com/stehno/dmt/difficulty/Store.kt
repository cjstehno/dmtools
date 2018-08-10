package com.stehno.dmt.difficulty

import com.stehno.dmt.difficulty.model.Challenger
import com.stehno.dmt.difficulty.model.Difficulty
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.FXCollections.unmodifiableObservableList
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList

class Store {

    private val challengerList = observableArrayList<Challenger>()
    private val difficulties = observableArrayList(
        Difficulty("Tier 1", ""),
        Difficulty("1", "Easy"),
        Difficulty("2", "Easy"),
        Difficulty("3", "Easy"),
        Difficulty("4", "Easy"),
        Difficulty("Tier 2", ""),
        Difficulty("5", "Easy"),
        Difficulty("6", "Easy"),
        Difficulty("7", "Easy"),
        Difficulty("8", "Easy"),
        Difficulty("9", "Easy"),
        Difficulty("10", "Easy"),
        Difficulty("Tier 3", ""),
        Difficulty("11", "Easy"),
        Difficulty("12", "Easy"),
        Difficulty("13", "Easy"),
        Difficulty("14", "Easy"),
        Difficulty("15", "Easy"),
        Difficulty("16", "Easy"),
        Difficulty("Tier 4", ""),
        Difficulty("17", "Easy"),
        Difficulty("18", "Easy"),
        Difficulty("19", "Easy"),
        Difficulty("20", "Easy")
    )

    init {
        challengerList.addListener(ListChangeListener<Challenger> { updateDifficulties() })
    }

    private fun updateDifficulties() {
        difficulties.replaceAll { dif: Difficulty ->
            when {
                dif.level.startsWith("Tier") -> dif
                else -> Difficulty(dif.level, DifficultyCalculation.difficulty(dif.level.toInt(), challengerList.toList()).name)
            }
        }
    }

    fun challengers(): ObservableList<Challenger> = unmodifiableObservableList(challengerList).sorted()

    fun difficulties(): ObservableList<Difficulty> = unmodifiableObservableList(difficulties)

    fun addChallenger(challenger: Challenger) {
        val existing = challengerList.find { ch -> ch.challenge == challenger.challenge }

        if (existing != null) {
            challengerList.remove(existing)
            challengerList.add(Challenger(challenger.challenge, challenger.count + existing.count))
        } else {
            challengerList.add(challenger)
        }
    }

    fun removeChallenger(challenge: String) {
        val existing = challengerList.find { ch -> ch.challenge == challenge }
        if (existing != null) {
            challengerList.remove(existing)
        }
    }

    fun updateChallenger(challenger: Challenger) {
        val existing = challengerList.find { ch -> ch.challenge == challenger.challenge }

        challengerList.remove(existing)
        challengerList.add(Challenger(challenger.challenge, challenger.count))
    }
}
