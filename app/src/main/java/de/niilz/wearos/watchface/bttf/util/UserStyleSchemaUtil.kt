package de.niilz.wearos.watchface.bttf.util

import android.content.Context
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting

// FIXME: We probably need to add a ConfigStateHolder aswell, use it in the editor and listen for updates
fun createUserStyleSchema(applicationContext: Context): UserStyleSchema {
    val complicationSettings = UserStyleSetting.ListUserStyleSetting(
        UserStyleSetting.Id("complication_count"),
        "set complication",
        "allows to set a complication",
        null,
        listOf(),
        listOf()
    )
    return UserStyleSchema(listOf(complicationSettings))

}