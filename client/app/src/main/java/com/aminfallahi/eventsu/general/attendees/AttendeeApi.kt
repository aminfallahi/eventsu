package com.aminfallahi.eventsu.general.attendees

import io.reactivex.Completable
import io.reactivex.Single
import com.aminfallahi.eventsu.general.attendees.forms.CustomForm
import retrofit2.http.*

interface AttendeeApi {

    @POST("attendees?include=event,ticket&fields[event]=id&fields[ticket]=id")
    fun postAttendee(@Body attendee: Attendee): Single<Attendee>

    @DELETE("attendees/{attendeeId}")
    fun deleteAttendee(@Path("attendeeId") id: Long): Completable

    @GET("events/{id}/custom-forms?include=event&fields[event]=id")
    fun getCustomFormsForAttendees(@Path("id") id: Long, @Query("filter") filter: String): Single<List<CustomForm>>
}
