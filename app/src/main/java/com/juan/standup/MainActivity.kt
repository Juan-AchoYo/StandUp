package com.juan.standup

import android.app.AlarmManager
import android.app.AlarmManager.ELAPSED_REALTIME
import android.app.AlarmManager.ELAPSED_REALTIME_WAKEUP
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.juan.standup.AlarmReceiver.Companion.NOTIFICATION_ID
import com.juan.standup.AlarmReceiver.Companion.PRIMARY_CHANNEL_ID


class MainActivity : AppCompatActivity() {
    lateinit var toggleButton: ToggleButton
    lateinit var notificationChannel: NotificationChannel
    lateinit var alarmManager: AlarmManager
    lateinit var notifyPendingIntent: PendingIntent
    lateinit var mNotificationManager: NotificationManager

    companion object {
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_chanel"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toggleButton = findViewById(R.id.alarmToggle)
        initListeners()
        mNotificationManager =
            getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChanel()
        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        notifyPendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
    }

    private fun initListeners() {
        toggleButton.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            when (isChecked) {
                true -> alarmManager.setInexactRepeating(
                    ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, notifyPendingIntent
                )

                false -> {
                    alarmManager.cancel(notifyPendingIntent)
                    mNotificationManager.cancelAll()
                }
            }
        }
    }


    private fun createNotificationChanel() {
        notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = getString(R.string.notification_channel_description)
        mNotificationManager.createNotificationChannel(notificationChannel)
    }
}