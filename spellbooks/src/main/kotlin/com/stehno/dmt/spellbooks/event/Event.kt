package com.stehno.dmt.spellbooks.event

import com.stehno.dmt.spellbooks.data.Events

data class Event(val id: String, val payload: Map<String, *> = emptyMap<String, Any>()) {

    constructor(ident: Events, attrs: Map<String, *> = emptyMap<String, Any>()) : this(ident.id, attrs)
}