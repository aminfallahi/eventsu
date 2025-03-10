package com.aminfallahi.eventsu.general.order

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
import io.reactivex.annotations.NonNull
import com.aminfallahi.eventsu.general.attendees.Attendee
import com.aminfallahi.eventsu.general.attendees.AttendeeId
import com.aminfallahi.eventsu.general.event.Event
import com.aminfallahi.eventsu.general.event.EventId

@Type("order")
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy::class)
@Entity(foreignKeys = [(ForeignKey(entity = Event::class, parentColumns = ["id"], childColumns = ["event"], onDelete = ForeignKey.CASCADE)), (ForeignKey(entity = Attendee::class, parentColumns = ["id"], childColumns = ["attendees"], onDelete = ForeignKey.CASCADE))])
data class Order(
    @Id(IntegerIdHandler::class)
    @PrimaryKey
    @NonNull
    val id: Long,
    val paymentMode: String? = null,
    val country: String? = null,
    val status: String? = null,
    val amount: Float? = null,
    val identifier: String? = null,
    val orderNotes: String? = null,
    val completedAt: String? = null,
    val city: String? = null,
    val address: String? = null,
    val createdAt: String? = null,
    val zipcode: String? = null,
    val paidVia: String? = null,
    val discountCodeId: String? = null,
    val ticketsPdfUrl: String? = null,
    val transactionId: String? = null,
    @ColumnInfo(index = true)
    @Relationship("event")
    var event: EventId? = null,
    @Relationship("attendees")
    var attendees: List<AttendeeId>? = null
)
