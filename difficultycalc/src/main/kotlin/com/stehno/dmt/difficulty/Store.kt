package com.stehno.dmt.difficulty

import com.stehno.dmt.difficulty.model.Challenger
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.FXCollections.unmodifiableObservableList
import javafx.collections.ObservableList

class Store {

    private val challengerList = observableArrayList<Challenger>()

    fun challengers(): ObservableList<Challenger> {
        return unmodifiableObservableList(challengerList).sorted()
    }

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