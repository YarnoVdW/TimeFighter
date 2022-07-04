package com.icapps.yarno.firefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var timer: TextView
    private lateinit var scoreView: TextView
    private lateinit var tapMeButton: Button
    private var score = 0
    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDownTimerInMilis = 10000L
    private val countDownIntervalInMillis = 1000L
    private var gameStarted = false
    private var timeLeft = initialCountDownTimerInMilis
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var animation: Animation
    private lateinit var animationBlink: Animation

    companion object { //statische variabele
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // link leggen met code en view
        timer = findViewById(R.id.timer)
        scoreView = findViewById(R.id.score)
        tapMeButton = findViewById(R.id.tapMe)

        animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        animationBlink = AnimationUtils.loadAnimation(this, R.anim.blink)

        tapMeButton.setOnClickListener {
            tapMeButton.startAnimation(animation)
            incrementScore()
            if (!gameStarted) {
                startGame()
            }
        }
        scoreView.text = getString(R.string.score_view, score)
        gameStarted = true
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
            Log.d(TAG, "OnRestoreInstanceState: $score timeLeft: $timeLeft")
        } else {
            resetGame()
        }
    }

    private fun restoreGame() {
        scoreView.text = getString(R.string.score_view, score)
        timer.text = getString(R.string.time_view, timeLeft)
        initCountDownTimer(timeLeft)
        countDownTimer.start()
    }


    private fun incrementScore() {
        score++
        var newScore = getString(R.string.score_view, score)



        scoreView.text = newScore
        scoreView.startAnimation(animationBlink)

    }

    private fun resetGame() {
        score = 0
        scoreView.text = getString(R.string.score_view, score)
        timer.text = getString(R.string.time_view, initialCountDownTimerInMilis / 1000)

        initCountDownTimer(initialCountDownTimerInMilis)

        gameStarted = false

    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, "Times up! Your score was $score", Toast.LENGTH_LONG).show()
        resetGame()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        super.onSaveInstanceState(outState)
    }

    private fun initCountDownTimer(timerLeft: Long = initialCountDownTimerInMilis) {
        countDownTimer = object : CountDownTimer(timerLeft, countDownIntervalInMillis) {
            override fun onTick(time_left: Long) {
                timeLeft = time_left
                val timeLeftOnTimer = time_left / 1000
                timer.text = getString(R.string.time_view, timeLeftOnTimer)
            }

            override fun onFinish() {
                endGame()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.menu_about) {
            showAbout()
        }
        return true
    }

    private fun showAbout() {
        AlertDialog.Builder(this)
            .setTitle("About")
            .setMessage("Made by Yakke")
            .create()
            .show()
    }

}