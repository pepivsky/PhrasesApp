package com.example.stage4phrases

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stage4phrases.Utili.myAdapter
import com.example.stage4phrases.Utili.phraseDao
import com.example.stage4phrases.data.PhraseDao
import com.example.stage4phrases.data.model.Phrase
import com.example.stage4phrases.databinding.ActivityMainBinding
import java.util.*

const val CHANNEL_ID = "org.hyperskill.phrases"


object Utili {
    lateinit var phraseDao: PhraseDao
    lateinit var myAdapter: MyRecyclerViewAdapter

}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appDatabase = (application as PhrasesApplication).database
        phraseDao = appDatabase.getPhraseDao()
        myAdapter = MyRecyclerViewAdapter(
            data = appDatabase.getPhraseDao().getAll().toMutableList(),
            phraseDao
        )

        // configure notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createChannel()
            registerChanel(channel)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = myAdapter

        }

        binding.reminderTextView.setOnClickListener {
            // launch timePicker
            Log.d("click", "reminderTextView: ")
            showTimePickerDialog()
        }

        binding.addButton.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val contentView = LayoutInflater.from(this).inflate(R.layout.add_dialog, null, false)
        AlertDialog.Builder(this)
            .setTitle("Add phrase")
            .setView(contentView)
            .setPositiveButton("Add") { _, _ ->
                val editText = contentView.findViewById<EditText>(R.id.editText)
                val text = editText.text.toString()
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                // guarda la frase
                val phrase = Phrase(phrase = text)
                phraseDao.insert(phrase)
                myAdapter.data = phraseDao.getAll()

            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }


    private fun scheduleNotification(hour: Int, minute: Int) {
        Log.d("triggered", "scheduleNotification")
        val intent = Intent(applicationContext, AlarmNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)


        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }


    private fun onTimeSelected(time: String) {
        val (hour, minute) = time.split(":").map { it.toInt() }
        val tiempoString: String = String.format("%02d:%02d", hour, minute)
        Log.d("onTimeSelected", tiempoString)

        if (!phraseDao.hasRecords()) {
            Toast.makeText(this, "Error, prhases empty", Toast.LENGTH_SHORT).show()
            return
        }
        // set reminder
        binding.reminderTextView.text = "Reminder set for $tiempoString"
        scheduleNotification(hour, minute)
    }


    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(supportFragmentManager, "timePicker")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(): NotificationChannel {
        val name = "Delivery status"
        val descriptionText = "Your delivery status"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerChanel(channel: NotificationChannel) {
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}