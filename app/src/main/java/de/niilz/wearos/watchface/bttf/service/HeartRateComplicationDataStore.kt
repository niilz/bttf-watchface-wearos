package de.niilz.wearos.watchface.bttf.service

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "HeartRateComplicationData")
val HEART_RATE_PREF_KEY = intPreferencesKey("data_source_heart_rate")