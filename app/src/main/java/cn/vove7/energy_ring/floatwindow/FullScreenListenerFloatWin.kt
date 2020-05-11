package cn.vove7.energy_ring.floatwindow

import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import cn.vove7.energy_ring.App
import cn.vove7.energy_ring.BuildConfig
import cn.vove7.energy_ring.R
import cn.vove7.energy_ring.listener.RotationListener

/**
 * # FullScreenListenerFloatWin
 *
 * @author Vove
 * 2020/5/9
 */
object FullScreenListenerFloatWin {

    var isFullScreen = false

    private val view by lazy {
        object : View(App.INS) {

            init {
                if (BuildConfig.DEBUG) {
                    setBackgroundColor(R.color.colorPrimary)
                }
            }

            override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
                super.onLayout(changed, left, top, right, bottom)
                val ps = intArrayOf(0, 0)
                getLocationOnScreen(ps)
                when {
                    ps[1] == 0 -> {//全屏
                        isFullScreen = true
                        FloatRingWindow.hide()
                    }
                    RotationListener.canShow -> {
                        FloatRingWindow.show()
                        isFullScreen = false
                    }
                    else -> {
                        isFullScreen = false
                    }
                }
            }
        }
    }
    private val layoutParams: WindowManager.LayoutParams
        get() = WindowManager.LayoutParams(
                10, 10,
                100, 0,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                , 0
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            format = PixelFormat.RGBA_8888
            gravity = Gravity.TOP or Gravity.START
        }

    var showing = false

    fun start(wm: WindowManager) {
        if (showing) {
            return
        }
        showing = true
        wm.addView(view, layoutParams)
    }

}