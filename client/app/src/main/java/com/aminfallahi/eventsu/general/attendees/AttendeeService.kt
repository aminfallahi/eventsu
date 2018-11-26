package com.aminfallahi.eventsu.general.attendees

import io.reactivex.Completable
import io.reactivex.Single
import com.aminfallahi.eventsu.general.attendees.forms.CustomForm
import com.aminfallahi.eventsu.general.auth.User
import com.aminfallahi.eventsu.general.auth.UserDao

class AttendeeService(
    private val attendeeApi: AttendeeApi,
    private val attendeeDao: AttendeeDao,
    private val userDao: UserDao
) {
    fun postAttendee(attendee: Attendee): Single<Attendee> {
        return attendeeApi.postAttendee(attendee)
                .map {
                    attendeeDao.insertAttendee(it)
                    it
                }
    }

    fun getAttendeeDetails(id: Long): Single<User> {
        return userDao.getUser(id)
    }

    fun deleteAttendee(id: Long): Completable {
        return attendeeApi.deleteAttendee(id)
    }

    fun getCustomFormsForAttendees(id: Long, filter: String): Single<List<CustomForm>> {
        val formsSingle = attendeeDao.getCustomFormsForId(id)
        return formsSingle.flatMap {
            if (it.isNotEmpty())
                formsSingle
            else
                attendeeApi.getCustomFormsForAttendees(id, filter)
                        .map {
                            attendeeDao.insertCustomForms(it)
                        }
                        .flatMap {
                            formsSingle
                        }
        }
    }
}
