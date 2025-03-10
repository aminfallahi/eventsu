package com.aminfallahi.eventsu.general.event

import com.aminfallahi.eventsu.general.R
import com.aminfallahi.eventsu.general.data.Resource
import com.aminfallahi.eventsu.general.utils.nullToEmpty
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

object EventUtils {

    @JvmStatic
    private val sharedResource by lazy {
        Resource()
    }

    private val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    private val frontendUrl: String = "https://open-event-frontend-dev.herokuapp.com/e/"

    fun getSharableInfo(event: Event, resource: Resource = sharedResource): String {
        val description = event.description.nullToEmpty()
        val identifier = event.identifier
        val eventUrl = frontendUrl + identifier

        val message = StringBuilder()

        val startsAt = getLocalizedDateTime(event.startsAt)
        val endsAt = getLocalizedDateTime(event.endsAt)

        message.append(resource.getString(R.string.event_name)).append(event.name).append("\n\n")
        if (!description.isEmpty()) message.append(resource.getString(R.string.event_description))
                .append(event.description.nullToEmpty()).append("\n\n")
        message.append(resource.getString(R.string.starts_on))
                .append(startsAt.format(dateFormat)).append(" ")
                .append(startsAt.format(timeFormat)).append("\n")
        message.append(resource.getString(R.string.ends_on))
                .append(endsAt.format(dateFormat)).append(" ")
                .append(endsAt.format(timeFormat))
        if (!eventUrl.isEmpty()) message.append("\n")
                .append(resource.getString(R.string.event_link))
                .append(eventUrl)

        return message.toString()
    }

    fun getLocalizedDateTime(dateString: String): ZonedDateTime = ZonedDateTime.parse(dateString)
            .toOffsetDateTime()
            .atZoneSameInstant(ZoneId.systemDefault())

    fun getTimeInMilliSeconds(dateString: String): Long {
        return getLocalizedDateTime(dateString).toInstant().toEpochMilli()
    }

    fun getFormattedDate(date: ZonedDateTime): String {
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d, y")
        try {
            return dateFormat.format(date)
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting Date")
            return ""
        }
    }

    fun getFormattedDateShort(date: ZonedDateTime): String {
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d")
        try {
            return dateFormat.format(date)
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting Date")
            return ""
        }
    }

    fun getFormattedDateWithoutYear(date: ZonedDateTime): String {
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d")
        try {
            return dateFormat.format(date)
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting Date")
            return ""
        }
    }

    fun getFormattedTime(date: ZonedDateTime): String {
        val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        try {
            return timeFormat.format(date)
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting time")
            return ""
        }
    }

    fun getFormattedTimeZone(date: ZonedDateTime): String {
        val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("z")
        try {
            return timeFormat.format(date)
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting time")
            return ""
        }
    }

    fun getFormattedEventDateTimeRange(startsAt: ZonedDateTime, endsAt: ZonedDateTime): String {
        val startingDate = getFormattedDate(startsAt)
        val endingDate = getFormattedDate(endsAt)
        try {
            if (startingDate != endingDate)
                return "${getFormattedDateShort(startsAt)}, ${getFormattedTime(startsAt)}"
            else
                return "${getFormattedDateWithoutYear(startsAt)}"
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting time")
            return ""
        }
    }

    fun getFormattedEventDateTimeRangeSecond(startsAt: ZonedDateTime, endsAt: ZonedDateTime): String {
        val startingDate = getFormattedDate(startsAt)
        val endingDate = getFormattedDate(endsAt)
        try {
            if (startingDate != endingDate)
                return "- ${getFormattedDateShort(endsAt)}, ${getFormattedTime(endsAt)} ${getFormattedTimeZone(endsAt)}"
            else
                return "${getFormattedTime(startsAt)} - ${getFormattedTime(endsAt)} ${getFormattedTimeZone(endsAt)}"
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting time")
            return ""
        }
    }

    fun getFormattedDateTimeRangeDetailed(startsAt: ZonedDateTime, endsAt: ZonedDateTime): String {
        val startingDate = getFormattedDate(startsAt)
        val endingDate = getFormattedDate(endsAt)
        try {
            if (startingDate != endingDate)
                return "$startingDate at ${getFormattedTime(startsAt)} - $endingDate at ${getFormattedTime(endsAt)} (${getFormattedTimeZone(endsAt)})"
            else
                return "$startingDate from ${getFormattedTime(startsAt)} to ${getFormattedTime(endsAt)} (${getFormattedTimeZone(endsAt)})"
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting time")
            return ""
        }
    }

    fun getFormattedDateTimeRangeBulleted(startsAt: ZonedDateTime, endsAt: ZonedDateTime): String {
        val startingDate = getFormattedDateShort(startsAt)
        val endingDate = getFormattedDateShort(endsAt)
        try {
            if (startingDate != endingDate)
                return "$startingDate - $endingDate • ${getFormattedTime(startsAt)} ${getFormattedTimeZone(startsAt)}"
            else
                return "$startingDate • ${getFormattedTime(startsAt)} ${getFormattedTimeZone(startsAt)}"
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error formatting time")
            return ""
        }
    }
}
