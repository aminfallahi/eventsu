package com.aminfallahi.eventsu.general.attendees

import android.arch.persistence.room.ColumnInfo
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
import com.aminfallahi.eventsu.general.ticket.Ticket
import com.aminfallahi.eventsu.general.ticket.TicketId

@Type("attendee")
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy::class)
@Entity(foreignKeys = [(ForeignKey(entity = Event::class, parentColumns = ["id"], childColumns = ["event"], onDelete = ForeignKey.CASCADE)), (ForeignKey(entity = Ticket::class, parentColumns = ["id"], childColumns = ["ticket"], onDelete = ForeignKey.CASCADE))])
data class Attendee(
    @Id(IntegerIdHandler::class)
    @PrimaryKey
    val id: Long,
    val firstname: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val isCheckedIn: Boolean? = false,
    val pdfUrl: String? = null,
    val ticketId: String? = null,
    @ColumnInfo(index = true)
    @Relationship("event")
    var event: EventId? = null,
    @Relationship("ticket")
    var ticket: TicketId? = null
)
