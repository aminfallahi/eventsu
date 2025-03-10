package com.aminfallahi.eventsu.general.attendees.forms

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.github.jasminb.jsonapi.IntegerIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import com.aminfallahi.eventsu.general.event.Event
import com.aminfallahi.eventsu.general.event.EventId

@Type("custom-form")
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy::class)
@Entity(foreignKeys = [(ForeignKey(entity = Event::class, parentColumns = ["id"], childColumns = ["event"], onDelete = ForeignKey.CASCADE))])
data class CustomForm(
    @Id(IntegerIdHandler::class)
    @PrimaryKey
    val id: Long,
    val form: String,
    val fieldIdentifier: String,
    val type: String,
    val isRequired: Boolean? = false,
    val isIncluded: Boolean? = false,
    val isFixed: Boolean? = false,
    val ticketsNumber: Int? = null,
    @Relationship("event")
    var event: EventId? = null
)
