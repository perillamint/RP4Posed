package moe.silicon.android.rp4posed.hook

import android.app.AndroidAppHelper
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.android.ViewClass
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

        // SystemUI customization.
        loadApp("com.android.systemui") {
            Log.d(TAG, "Running in SystemUI");
            val ridiHomeButton = "com.android.systemui.R\$id".toClass().field {
                name = "ridi_status_bar_button_home"
            }.get().int()
            val ridiSettingsButton = "com.android.systemui.R\$id".toClass().field {
                name = "ridi_status_bar_button_settings"
            }.get().int()
            val ridiBrightnessButton = "com.android.systemui.R\$id".toClass().field {
                name = "ridi_status_bar_button_brightness"
            }.get().int()

            "android.view.View".toClass().hook {
                injectMember {
                    method {
                        name = "setOnClickListener"
                    }
                    beforeHook {
                        val view: View = instance as View;

                        Log.d(TAG, "setOnClickListener called " + Integer.toHexString(view.id))
                    }
                }
            }

            "com.android.systemui.statusbar.ridi.RidiStatusBarFragment".toClass().hook {
                injectMember {
                    method {
                        name = "onViewCreated"
                        param(ViewClass, BundleClass)
                    }
                    beforeHook {
                        Log.d(TAG, "Entering onViewCreated")
                    }
                    afterHook {
                        Log.d(TAG, "onViewCreated executed.")
                        val view: View = args[0] as View

                        val homeButton: ImageButton = view.findViewById(ridiHomeButton)
                        val settingsButton: ImageButton = view.findViewById(ridiSettingsButton)
                        val brightnessButton: ImageButton = view.findViewById(ridiBrightnessButton)

                        homeButton.setOnClickListener {
                            Log.d(TAG, "Home button clicked!!!!")

                            val intent = Intent();
                            intent.component = ComponentName(
                                "cn.modificator.launcher",
                                "cn.modificator.launcher.Launcher"
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            AndroidAppHelper.currentApplication().startActivity(intent)
                        }
                    }
                }
            }
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