package wishpool.sketch


import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.*


class GlobalActivity : Application() {


    companion object {
        val TAG = "GlobalActivity"
        private var admobInterstitialAd: InterstitialAd? = null
        private var retryAttempt = 0
        lateinit var callback: (Boolean) -> Unit


        fun loadAdmobAd(mcon: Context) {
            if (admobInterstitialAd != null) {
                Log.d(TAG, "loadAdmobAd: already loaded")
                return
            }

            val adRequest = AdRequest.Builder().build()
            val admobInerstitialAdId = mcon.getString(R.string.admobInterstitialAdId)

            InterstitialAd.load(

                mcon, admobInerstitialAdId, adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d(TAG, adError.message)
                        admobInterstitialAd = null
                        val error =
                            "domain: ${adError.domain}, code: ${adError.code}, message: ${adError.message}"

                        Log.e(TAG, "onAdFailedToLoad: error= $error")
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d(TAG, "Ad was loaded.")
                        admobInterstitialAd = interstitialAd
                    }
                }
            )
        }

        fun showAdmobInterstitialAd(mcon: Activity, callback: (Boolean) -> Unit) {

            loadAdmobAd(mcon)

            if (admobInterstitialAd != null) {
                Log.d(TAG, "showAdmobInterstitialAd: showing adob ad")
                admobInterstitialAd?.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Ad was dismissed.")
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            admobInterstitialAd = null
                            callback.invoke(true)
                            loadAdmobAd(mcon)

                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.d(TAG, "Ad failed to show.")
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            admobInterstitialAd = null
                            callback.invoke(false)
                        }

                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "Ad showed fullscreen content.")

                            // Called when ad is dismissed.
                        }
                    }
                admobInterstitialAd?.show(mcon)
            } else {
                Log.d(TAG, "showAdmobInterstitialAd: no show admob ad")
                callback.invoke(false)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        //MR: fb ads initiatialize

        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(Arrays.asList("0959DC79C96A59F082362BA0037615E9")).build()
        MobileAds.setRequestConfiguration(configuration)

        MobileAds.initialize(this) {
            Log.d(TAG, "onCreate: MobileAds.initialize(this) ")
        }

        loadAdmobAd(this)

    }
}