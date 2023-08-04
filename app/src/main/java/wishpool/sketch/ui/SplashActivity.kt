package wishpool.sketch.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import wishpool.sketch.databinding.ActivitySplashBinding
import wishpool.sketch.utils.openNextActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            openNextActivity(DashboardActivity::class.java)
            finish()
        }, 2000)
    }
}