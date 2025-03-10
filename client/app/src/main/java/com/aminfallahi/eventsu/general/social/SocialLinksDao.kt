package com.aminfallahi.eventsu.general.social

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface SocialLinksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSocialLinks(socialLinks: List<SocialLink>)

    @Query("DELETE FROM SocialLink")
    fun deleteAll()

    @Query("SELECT * from SocialLink WHERE event = :eventId")
    fun getAllSocialLinks(eventId: Long): Flowable<List<SocialLink>>
}
