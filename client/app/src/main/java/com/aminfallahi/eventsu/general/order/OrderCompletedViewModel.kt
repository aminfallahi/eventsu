package com.aminfallahi.eventsu.general.order

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import com.aminfallahi.eventsu.general.common.SingleLiveEvent
import com.aminfallahi.eventsu.general.event.Event
import com.aminfallahi.eventsu.general.event.EventService
import timber.log.Timber

class OrderCompletedViewModel(private val eventService: EventService) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val message = SingleLiveEvent<String>()
    val event = MutableLiveData<Event>()

    fun loadEvent(id: Long) {
        if (id.equals(-1)) {
            throw IllegalStateException("ID should never be -1")
        }

        compositeDisposable.add(eventService.getEvent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    event.value = it
                }, {
                    Timber.e(it, "Error fetching event %d", id)
                    message.value = "Error fetching event"
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
