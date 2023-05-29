package wishpool.sketch.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import wishpool.sketch.R


const val TAG = "AdsUtil"
fun Context.loadAdmobNativeAdBig(
    adParentView: CardView, adFrame: FrameLayout, shimmerFrameLayout: ShimmerFrameLayout,

    isRatingBarView: Boolean = false, adCallBack: ((Boolean) -> Unit?)? = null
) {
    adParentView.visibility = View.VISIBLE
    adFrame.visibility = View.GONE
    shimmerFrameLayout.showShimmerEffects()

    getNativeAd { nativeAd ->
        if (nativeAd == null) {

            adCallBack?.invoke(false)
            adParentView.visibility = View.GONE
        } else {
            val adView = (this as Activity).layoutInflater.inflate(
                R.layout.native_ad_view_big, null, false
            ) as NativeAdView
            populateUnifiedNativeAdView(nativeAd, adView, isRatingBarView)

            adFrame.visibility = View.VISIBLE
            adParentView.visibility = View.VISIBLE

            adFrame.removeAllViews()
            adFrame.addView(adView)
            shimmerFrameLayout.stopShimmerEffects()
            adCallBack?.invoke(true)

        }
    }
}


private fun populateUnifiedNativeAdView(
    nativeAd: NativeAd, adView: NativeAdView, isRatingBarView: Boolean
) {
    // You must call destroy on old ads when you are done with them,
    // otherwise you will have a memory leak.
//    currentNativeAd?.destroy()
//    currentNativeAd = nativeAd
    // Set the media view.
    adView.mediaView = adView.findViewById(R.id.ad_media)

    // Set other ad assets.
    adView.headlineView = adView.findViewById(R.id.ad_headline)
    adView.bodyView = adView.findViewById(R.id.ad_body)
    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById(R.id.ad_app_icon)
//    adView.priceView = adView.findViewById(R.id.ad_price)
    adView.storeView = adView.findViewById(R.id.ad_store)
    adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

    // The headline and media content are guaranteed to be in every UnifiedNativeAd.
    (adView.headlineView as TextView).text = nativeAd.headline
    adView.mediaView?.mediaContent = nativeAd.mediaContent!!

    // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
    // check before trying to display them.
    if (nativeAd.body == null) {
        adView.bodyView?.visibility = View.GONE
    } else {
        adView.bodyView?.visibility = View.VISIBLE
        (adView.bodyView as TextView).text = nativeAd.body
    }


    if (nativeAd.advertiser == null) {
        adView.advertiserView?.visibility = View.GONE
    } else {
        adView.advertiserView?.visibility = View.GONE
        (adView.advertiserView as TextView).text = nativeAd.advertiser
    }

    if (nativeAd.callToAction == null) {
        adView.callToActionView?.visibility = View.GONE
    } else {
        adView.callToActionView?.visibility = View.VISIBLE
        (adView.callToActionView as Button).text = nativeAd.callToAction
    }


    if (nativeAd.icon == null) {
        if (nativeAd.mediaContent?.mainImage == null) adView.iconView?.visibility = View.INVISIBLE
        else (adView.iconView as ImageView).setImageDrawable(nativeAd.mediaContent?.mainImage)
    } else {
        (adView.iconView as ImageView).setImageDrawable(
            nativeAd.icon?.drawable
        )
        adView.iconView?.visibility = View.VISIBLE
    }
//
//    if (nativeAd.price == null) {
//        adView.priceView.visibility = View.GONE
//    } else {
//        adView.priceView.visibility = View.VISIBLE
//        (adView.priceView as TextView).text = nativeAd.price
//    }

    if (nativeAd.store == null) {
        adView.storeView?.visibility = View.GONE
    } else {
        adView.storeView?.visibility = View.GONE
        (adView.storeView as TextView).text = nativeAd.store
    }

    try {
        if (isRatingBarView) {
            adView.starRatingView = adView.findViewById(R.id.ad_stars)

            if (nativeAd.starRating == null) {
                adView.starRatingView?.visibility = View.GONE
            } else {
                (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                adView.starRatingView?.visibility = View.VISIBLE
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "populateUnifiedNativeAdView: ${e.message}")
    }

    // This method tells the Google Mobile Ads SDK that you have finished populating your
    // native ad view with this native ad.
    adView.setNativeAd(nativeAd)

}

fun Context.loadAdmobNativeAd_100sdp(
    adParentView: CardView,
    adFrame: FrameLayout,
    shimmerFrameLayout: ShimmerFrameLayout,
    isRatingBarView: Boolean = false,
    adCallBack: ((Boolean) -> Unit?)? = null
) {
    adParentView.visibility = View.VISIBLE
    adFrame.visibility = View.GONE
    shimmerFrameLayout.showShimmerEffects()

    getNativeAd {
        if (it == null) {

            adCallBack?.invoke(false)
            adParentView.visibility = View.GONE

        } else {
            // OnUnifiedNativeAdLoadedListener implementation.
            val adView = (this as Activity).layoutInflater.inflate(
                R.layout.native_ad_70sdp, null, false
            ) as NativeAdView
            populateUnifiedNativeAdView(it, adView, isRatingBarView)

            shimmerFrameLayout.stopShimmerEffects()
            adFrame.visibility = View.VISIBLE
            adParentView.visibility = View.VISIBLE

            adFrame.removeAllViews()
            adFrame.addView(adView)

            adCallBack?.invoke(true)
        }
    }
}

fun ShimmerFrameLayout.stopShimmerEffects() {
    this.stopShimmerAnimation()
    this.visibility = View.GONE
    Log.d(TAG, "stopShimmerEffects: ")
}

fun ShimmerFrameLayout.showShimmerEffects() {
    this.startShimmerAnimation()
    this.visibility = View.VISIBLE
    Log.d(TAG, "showShimmerEffects: ")
}

fun Context.getNativeAd(native: (NativeAd?) -> Unit) {

    val nativeAdId = this.getString(R.string.admobNativeAdId)
    val builder = AdLoader.Builder(this, nativeAdId).forNativeAd { }

    builder.forNativeAd { nativeAd ->

        native.invoke(nativeAd)
    }

    val videoOptions = VideoOptions.Builder().setStartMuted(true).build()

    val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdLoaded() {
            Log.d(TAG, "onAdLoaded: ")
            super.onAdLoaded()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            Log.e(TAG, "onAdFailedToLoad:  ${loadAdError}")

            native.invoke(null)
        }

    }).build()

    adLoader.loadAd(AdRequest.Builder().build())
}