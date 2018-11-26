package com.aminfallahi.eventsu.general.di

import android.arch.persistence.room.Room
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jasminb.jsonapi.retrofit.JSONAPIConverterFactory
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.aminfallahi.eventsu.general.BuildConfig
import com.aminfallahi.eventsu.general.OpenEventDatabase
import com.aminfallahi.eventsu.general.about.AboutEventViewModel
import com.aminfallahi.eventsu.general.attendees.*
import com.aminfallahi.eventsu.general.attendees.forms.CustomForm
import com.aminfallahi.eventsu.general.auth.*
import com.aminfallahi.eventsu.general.data.Network
import com.aminfallahi.eventsu.general.data.Preference
import com.aminfallahi.eventsu.general.event.*
import com.aminfallahi.eventsu.general.event.topic.EventTopic
import com.aminfallahi.eventsu.general.event.topic.EventTopicApi
import com.aminfallahi.eventsu.general.event.topic.SimilarEventsViewModel
import com.aminfallahi.eventsu.general.favorite.FavouriteEventsViewModel
import com.aminfallahi.eventsu.general.order.*
import com.aminfallahi.eventsu.general.paypal.Paypal
import com.aminfallahi.eventsu.general.paypal.PaypalApi
import com.aminfallahi.eventsu.general.search.SearchLocationViewModel
import com.aminfallahi.eventsu.general.search.SearchTimeViewModel
import com.aminfallahi.eventsu.general.search.SearchViewModel
import com.aminfallahi.eventsu.general.settings.SettingsFragmentViewModel
import com.aminfallahi.eventsu.general.social.SocialLink
import com.aminfallahi.eventsu.general.social.SocialLinkApi
import com.aminfallahi.eventsu.general.social.SocialLinksService
import com.aminfallahi.eventsu.general.social.SocialLinksViewModel
import com.aminfallahi.eventsu.general.ticket.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

val commonModule = applicationContext {
    bean { Preference() }
    bean { Network() }
}

val apiModule = applicationContext {
    bean {
        val retrofit: Retrofit = get()
        retrofit.create(EventApi::class.java)
    }
    bean {
        val retrofit: Retrofit = get()
        retrofit.create(AuthApi::class.java)
    }
    bean {
        val retrofit: Retrofit = get()
        retrofit.create(TicketApi::class.java)
    }
    bean {
        val retrofit: Retrofit = get()
        retrofit.create(SocialLinkApi::class.java)
    }
    bean {
        val retrofit: Retrofit = get()
        retrofit.create(EventTopicApi::class.java)
    }
    bean {
        val retrofit: Retrofit = get()
        retrofit.create(AttendeeApi::class.java)
    }
    bean {
        val retrofit: Retrofit = get()
        retrofit.create(OrderApi::class.java)
    }
    bean {
        val retrofit: Retrofit = get()
        retrofit.create(PaypalApi::class.java)
    }

    factory { AuthHolder(get()) }
    factory { AuthService(get(), get(), get()) }

    factory { EventService(get(), get(), get(), get()) }
    factory { TicketService(get(), get()) }
    factory { SocialLinksService(get(), get()) }
    factory { AttendeeService(get(), get(), get()) }
    factory { OrderService(get(), get(), get()) }
}

val viewModelModule = applicationContext {
    viewModel { LoginFragmentViewModel(get(), get()) }
    viewModel { EventsViewModel(get(), get()) }
    viewModel { ProfileFragmentViewModel(get()) }
    viewModel { SignUpFragmentViewModel(get(), get()) }
    viewModel { EventDetailsViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { AttendeeViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { SearchLocationViewModel(get()) }
    viewModel { SearchTimeViewModel(get()) }
    viewModel { TicketsViewModel(get(), get()) }
    viewModel { AboutEventViewModel(get()) }
    viewModel { SocialLinksViewModel(get()) }
    viewModel { FavouriteEventsViewModel(get()) }
    viewModel { SettingsFragmentViewModel(get()) }
    viewModel { SimilarEventsViewModel(get()) }
    viewModel { OrderCompletedViewModel(get()) }
    viewModel { OrdersUnderUserVM(get(), get(), get()) }
    viewModel { OrderDetailsViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
}

val networkModule = applicationContext {

    bean {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper
    }

    bean { RequestAuthenticator(get()) as Authenticator }

    bean {
        val connectTimeout = 15 // 15s
        val readTimeout = 15 // 15s

        OkHttpClient().newBuilder()
                .connectTimeout(connectTimeout.toLong(), TimeUnit.SECONDS)
                .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .authenticator(get())
                .build()
    }

    bean {
        val baseUrl = BuildConfig.DEFAULT_BASE_URL
        val objectMapper: ObjectMapper = get()

        Retrofit.Builder()
                .client(get())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JSONAPIConverterFactory(objectMapper, Event::class.java, User::class.java, SignUp::class.java, Ticket::class.java, SocialLink::class.java, EventId::class.java, EventTopic::class.java, Attendee::class.java, TicketId::class.java, Order::class.java, AttendeeId::class.java, Charge::class.java, Paypal::class.java, ConfirmOrder::class.java, CustomForm::class.java))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(baseUrl)
                .build()
    }
}

val databaseModule = applicationContext {

    bean {
        Room.databaseBuilder(androidApplication(),
                OpenEventDatabase::class.java, "open_event_database")
                .fallbackToDestructiveMigration()
                .build()
    }

    factory {
        val database: OpenEventDatabase = get()
        database.eventDao()
    }

    factory {
        val database: OpenEventDatabase = get()
        database.userDao()
    }

    factory {
        val database: OpenEventDatabase = get()
        database.ticketDao()
    }

    factory {
        val database: OpenEventDatabase = get()
        database.socialLinksDao()
    }

    factory {
        val database: OpenEventDatabase = get()
        database.attendeeDao()
    }

    factory {
        val database: OpenEventDatabase = get()
        database.eventTopicsDao()
    }

    factory {
        val database: OpenEventDatabase = get()
        database.orderDao()
    }
}
