package wishpool.sketch.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import wishpool.sketch.databinding.ActivitySplashBinding
import wishpool.sketch.utils.openNextActivity

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            openNextActivity(DashboardActivity::class.java)
            finish()
        }, 300)
    }
}