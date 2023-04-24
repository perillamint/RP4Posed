package moe.silicon.android.rp4posed.hook

import android.util.Log
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

const val TAG = "RP4PosedHookEntry"

@InjectYukiHookWithXposed
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        // Your code here.
    }

    override fun onHook() = encase {
        // Your code here.
        loadSystem {
            Log.d(TAG, "Got in to the system framework!")
        }

        loadApp("com.eroum.service.RIDI") {
            Log.d(TAG, "Got in to the RIDIDeviceManager!")

            // RIDI Device Manager enables ADB in engineering build...
            // so fake it to enable ADB.
            "android.os.Build".toClass().field {
                name = "TYPE"
            }.get().set("eng")
        }
    }
}