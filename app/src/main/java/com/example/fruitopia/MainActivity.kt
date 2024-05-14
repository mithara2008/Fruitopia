package com.example.fruitopia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.fruitopia.UIUtil.OnSwipeListener
import com.example.fruitopia.view.GameOverActivity
import java.util.Arrays.asList

class MainActivity : AppCompatActivity() {

    var fruits = intArrayOf(
        R.drawable.apple,
        R.drawable.blueberries,
        R.drawable.mango,
        R.drawable.strawberry,
        R.drawable.orange,
        R.drawable.watermelon,
        R.drawable.dragonfruit,


    )

    private var mediaPlayer: MediaPlayer? = null
    var widthOfBlock: Int = 0
    var noOfBLock : Int = 8
    var widthOfScreen : Int =0
    lateinit var fruit :ArrayList<ImageView>
    var fruitToBeDragged : Int = 0
    var fruitToBeReplaced : Int = 0
    var notFruit : Int =R.drawable.transparent

    lateinit var mHandler : Handler
    private lateinit var scoreResult:TextView
    var score = 0 // Starting score set to 0
    var interval = 100L
    var remainingMoves = 20// move limit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        score = 0

        scoreResult = findViewById(R.id.score)

        val displayMetrics = DisplayMetrics()

        windowManager.defaultDisplay.getMetrics(displayMetrics)

        widthOfScreen = displayMetrics.widthPixels

        var heightOfScreen = displayMetrics.heightPixels

        widthOfBlock =widthOfScreen / noOfBLock

        fruit = ArrayList()
        createBoard()


        mediaPlayer?.start()


        for(imageView in fruit){
            imageView.setOnTouchListener(object :OnSwipeListener(this){
                override fun onSwipeRight() {
                    super.onSwipeRight()
                    fruitToBeDragged = imageView.id
                    fruitToBeReplaced = fruitToBeDragged + 1
                    fruitInterChange()
                    decrementMoves()
                }

                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    fruitToBeDragged = imageView.id
                    fruitToBeReplaced = fruitToBeDragged - 1
                    fruitInterChange()
                    decrementMoves()
                }

                override fun onSwipeTop() {
                    super.onSwipeTop()
                    fruitToBeDragged = imageView.id
                    fruitToBeReplaced = fruitToBeDragged - noOfBLock
                    fruitInterChange()
                    decrementMoves()
                }

                override fun onSwipeBottom() {
                    super.onSwipeBottom()
                    fruitToBeDragged = imageView.id
                    fruitToBeReplaced = fruitToBeDragged + noOfBLock
                    fruitInterChange()
                    decrementMoves()
                }
            })
        }

        mHandler = Handler()
        startRepeat()


    }

    private fun fruitInterChange() {
        var background :Int = fruit.get(fruitToBeReplaced).tag as Int
        var background1 :Int = fruit.get(fruitToBeDragged).tag as Int

        fruit.get(fruitToBeDragged).setImageResource(background)
        fruit.get(fruitToBeReplaced).setImageResource(background1)

        fruit.get(fruitToBeDragged).setTag(background)
        fruit.get(fruitToBeReplaced).setTag(background1)
    }

    private fun checkRowForThree(){
        for ( i in 0..61){
            var chosedFruit = fruit.get(i).tag
            var isBlank :Boolean = fruit.get(i).tag == notFruit
            val notValid = arrayOf(6,7,14,15,22,23,30,31,38,39,46,47,54,55)
            val list = asList(*notValid)
            if (!list.contains(i)){
                var x = i

                if (fruit.get(x++).tag as Int == chosedFruit
                    && !isBlank
                    && fruit.get(x++).tag as Int == chosedFruit
                    && fruit.get(x).tag as Int == chosedFruit
                ){
                    score = score + 3
                    scoreResult.text = "$score"
                    playSoundEffect()
                    fruit.get(x).setImageResource(notFruit)
                    fruit.get(x).setTag(notFruit)
                    x--
                    fruit.get(x).setImageResource(notFruit)
                    fruit.get(x).setTag(notFruit)
                    x--
                    fruit.get(x).setImageResource(notFruit)
                    fruit.get(x).setTag(notFruit)
                }
            }
        }

        moveDownFruits()
    }

    private fun checkColumnForThree(){
        for ( i in 0..47){
            var chosedFruit = fruit.get(i).tag
            var isBlank :Boolean = fruit.get(i).tag == notFruit
            var x = i

            if (fruit.get(x).tag as Int == chosedFruit
                && !isBlank
                && fruit.get(x+noOfBLock).tag as Int == chosedFruit
                && fruit.get(x+2*noOfBLock).tag as Int == chosedFruit
            ){
                score = score + 3
                scoreResult.text = "$score"
                playSoundEffect()
                fruit.get(x).setImageResource(notFruit)
                fruit.get(x).setTag(notFruit)
                x = x + noOfBLock
                fruit.get(x).setImageResource(notFruit)
                fruit.get(x).setTag(notFruit)
                x = x + noOfBLock
                fruit.get(x).setImageResource(notFruit)
                fruit.get(x).setTag(notFruit)
            }

        }

        moveDownFruits()
    }

    private fun playSoundEffect() {

        mediaPlayer?.release()


        mediaPlayer = MediaPlayer.create(this, R.raw.ef2)


        mediaPlayer?.setOnPreparedListener { player ->

            player.start()
        }
    }



    private fun moveDownFruits() {

        val firstRow = arrayOf(1,2,3,4,5,6,7)
        val list = asList(*firstRow)
        for (i in 55 downTo 0){
            if(fruit.get(i+noOfBLock).tag as Int == notFruit){
                fruit.get(i+noOfBLock).setImageResource(fruit.get(i).tag as Int)
                fruit.get(i+noOfBLock).setTag(fruit.get(i).tag as Int)

                fruit.get(i).setImageResource(notFruit)
                fruit.get(i).setTag(notFruit)
                if (list.contains(i) && fruit.get(i).tag == notFruit){
                    var randomColor : Int = Math.abs(Math.random() * fruits.size).toInt()
                    fruit.get(i).setImageResource(fruits[randomColor])
                    fruit.get(i).setTag(fruits[randomColor])
                }
            }
        }
        for (i in 0..7){
            if (fruit.get(i).tag as Int == notFruit){

                var randomColor : Int = Math.abs(Math.random() * fruits.size).toInt()
                fruit.get(i).setImageResource(fruits[randomColor])
                fruit.get(i).setTag(fruits[randomColor])
            }
        }
    }

    val repeatChecker :Runnable = object :Runnable{
        override fun run() {
            try{
                checkRowForThree()
                checkColumnForThree()
                moveDownFruits()
            }
            finally {
                mHandler.postDelayed(this,interval)
            }
        }
    }

    private fun startRepeat() {
        repeatChecker.run()
    }

    private fun createBoard() {
        val gridLayout = findViewById<GridLayout>(R.id.board)
        gridLayout.rowCount = noOfBLock
        gridLayout.columnCount = noOfBLock
        gridLayout.layoutParams.width = widthOfScreen
        gridLayout.layoutParams.height = widthOfScreen

        for (i in 0 until noOfBLock * noOfBLock){
            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = ViewGroup
                .LayoutParams(widthOfBlock,widthOfBlock)

            imageView.maxHeight = widthOfBlock
            imageView.maxWidth = widthOfBlock

            var random : Int = Math.floor(Math.random() * fruits.size).toInt()

            imageView.setImageResource(fruits[random])
            imageView.setTag(fruits[random])

            fruit.add(imageView)
            gridLayout.addView(imageView)

        }
    }

    private fun decrementMoves() {
        remainingMoves--
        if (remainingMoves == 0) {
            gameOver()
        }
    }

    private fun loadHighestScore(): Int {

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("highestScore", 0)
    }

    private fun saveScore(score: Int) {

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("score", score)
            apply()
        }


        val highestScore = loadHighestScore()
        if (score > highestScore) {
            with(sharedPref.edit()) {
                putInt("highestScore", score)
                apply()
            }
        }
    }

    private fun gameOver() {
        // Stop the game by removing the repeatChecker runnable
        mHandler.removeCallbacks(repeatChecker)

        saveScore(score)



        val highestScore = loadHighestScore()
        //Toast.makeText(this, "Highest Score: $highestScore", Toast.LENGTH_SHORT).show()

        // Save the score using SharedPreferences
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("score", score)
            apply()
        }

        // Move to another activity only if the game is over
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("score", score)
        startActivity(intent)
        finish() // Finish the current activity to prevent going back to the game
    }






}





