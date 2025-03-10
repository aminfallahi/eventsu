package com.aminfallahi.eventsu.general.order

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import com.aminfallahi.eventsu.general.attendees.Attendee
import com.aminfallahi.eventsu.general.common.SingleLiveEvent
import com.aminfallahi.eventsu.general.event.Event
import com.aminfallahi.eventsu.general.event.EventService
import timber.log.Timber

class OrderDetailsViewModel(
    private val eventService: EventService,
    private val orderService: OrderService
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val message = SingleLiveEvent<String>()
    val event = MutableLiveData<Event>()
    val attendees = MutableLiveData<List<Attendee>>()
    val progress = MutableLiveData<Boolean>()

    fun loadEvent(id: Long) {
        if (id.equals(-1)) {
            throw IllegalStateException("ID should never be -1")
        }

        compositeDisposable.add(eventService.getEventFromApi(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    event.value = it
                }, {
                    Timber.e(it, "Error fetching event %d", id)
                    message.value = "Error fetching event"
                }))
    }

    fun loadAttendeeDetails(id: String) {
        if (id.equals(-1)) {
            throw IllegalStateException("ID should never be -1")
        }

        compositeDisposable.add(orderService.attendeesUnderOrder(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progress.value = true
                }.doFinally {
                    progress.value = false
                }.subscribe({
                    attendees.value = it
                }, {
                    Timber.e(it, "Error fetching attendee details")
                    message.value = "Error fetching attendee details"
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
