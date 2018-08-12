package com.stehno.dmt.spellbooks.event

import com.stehno.dmt.spellbooks.data.Events

data class Event(val id: String) {

    constructor(ident: Events) : this(ident.id)
}