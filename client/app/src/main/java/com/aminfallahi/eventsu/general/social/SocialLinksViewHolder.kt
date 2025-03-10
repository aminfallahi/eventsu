package com.aminfallahi.eventsu.general.social

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_social_link.view.*
import com.aminfallahi.eventsu.general.utils.Utils
import com.aminfallahi.eventsu.general.R

class SocialLinksViewHolder(itemView: View, private var context: Context) : RecyclerView.ViewHolder(itemView) {

    fun bind(socialLink: SocialLink) {
        val drawableId = getSocialLinkDrawableId(socialLink.name)
        if (drawableId != -1) {
            val imageDrawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
            imageDrawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.greyMore), PorterDuff.Mode.SRC_IN)

            itemView.imgSocialLink.setImageDrawable(imageDrawable)
        }

        itemView.setOnClickListener {
            Utils.openUrl(context, socialLink.link)
        }
    }

    private fun getSocialLinkDrawableId(name: String): Int {
        return when (name) {
            "Github Url" -> R.drawable.ic_github_24dp
            "Twitter Url" -> R.drawable.ic_twitter_24dp
            "Facebook Url" -> R.drawable.ic_facebook_24dp
            "LinkedIn Url" -> R.drawable.ic_linkedin_24dp
            "Youtube Url" -> R.drawable.ic_youtube_24dp
            "Google Url" -> R.drawable.ic_google_plus_24dp
            else -> -1
        }
    }
}
