@file:Suppress("SetTextI18n")

package moe.silicon.android.rp4posed.ui.activity

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.YukiHookAPI
import moe.silicon.android.rp4posed.BuildConfig
import moe.silicon.android.rp4posed.R
import moe.silicon.android.rp4posed.databinding.ActivityMainBinding
import moe.silicon.android.rp4posed.ui.activity.base.BaseActivity

const val TAG = "RP4PosedMainActivity"

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate() {
        refreshModuleStatus()
        binding.mainTextVersion.text = getString(R.string.module_version, BuildConfig.VERSION_NAME)

        binding.mainLinStatus.setOnClickListener {
            Log.d(TAG, "Broadcasting LSPosed magic secret code!")
            var intent = Intent("android.provider.Telephony.SECRET_CODE", Uri.parse("android_secret_code://5776733"))
            sendBroadcast(intent)
        }

        binding.hijackButton.isChecked = true
        binding.hijackButton.isEnabled = false

        binding.enableAdbwireless.setOnClickListener {
            Log.d(TAG, "Configure adb wireless")

            if (binding.enableAdbwireless.isChecked) {
                Runtime.getRuntime().exec(arrayOf("su", "-c", "setprop sys.usb.config none; setprop service.adb.tcp.port 5555; setprop sys.usb.config mtp,adb"))
            } else {
                Runtime.getRuntime().exec(arrayOf("su", "-c", "setprop sys.usb.config none; setprop service.adb.tcp.port -1; setprop sys.usb.config mtp,adb"))
            }
        }
        // Your code here.
    }

    /**
     * Refresh module status
     *
     * 刷新模块状态
     */
    private fun refreshModuleStatus() {
        binding.mainLinStatus.setBackgroundResource(
            when {
                YukiHookAPI.Status.isModuleActive -> R.drawable.bg_green_round
                else -> R.drawable.bg_dark_round
            }
        )
        binding.mainImgStatus.setImageResource(
            when {
                YukiHookAPI.Status.isModuleActive -> R.mipmap.ic_success
                else -> R.mipmap.ic_warn
            }
        )
        binding.mainTextStatus.text = getString(
            when {
                YukiHookAPI.Status.isModuleActive -> R.string.module_is_activated
                else -> R.string.module_not_activated
            }
        )
        binding.mainTextApiWay.isVisible = YukiHookAPI.Status.isModuleActive
        binding.mainTextApiWay.text = if (YukiHookAPI.Status.Executor.apiLevel > 0)
            "Activated by ${YukiHookAPI.Status.Executor.name} API ${YukiHookAPI.Status.Executor.apiLevel}"
        else "Activated by ${YukiHookAPI.Status.Executor.name}"
    }
}