package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private var canAddOpp = false
    private var isDecimal = true
    private lateinit var tvMain: TextView
    private lateinit var tvResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvMain = findViewById(R.id.calculateScreen)
        tvResults = findViewById(R.id.resultScreen)

        val secondActButton = findViewById<Button>(R.id.next)
        secondActButton.setOnClickListener {
            val Intent = Intent(this,SecondActivity::class.java)
            startActivity(Intent)
        }
    }


    fun clearAction(view: View) {
        tvMain.text = ""
        tvResults.text = ""
        isDecimal = true
    }

    fun backspaceAction(view: View) {
        val values = tvMain.text.length
        if (values > 0) {
            tvMain.text = tvMain.text.subSequence(0, values - 1)
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOpp) {
            tvMain.append(view.text)
            canAddOpp = false
            isDecimal = true
        }
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if(tvMain.length() < 15) {
                if (view.text == ".") {
                    if (isDecimal) {
                        tvMain.append(view.text)
                        isDecimal = false
                    }
                } else {
                    tvMain.append(view.text)
                    canAddOpp = true
                }
            }
        }
    }

    fun equalsAction(view: View) {
        tvResults.text = calculate()
    }

    fun calculate(): String {
        val digits = digitOperators()
        if (digits.isEmpty()) return ""

        val result = everyCalculate(digits)
        return result.firstOrNull()?.toString() ?: ""
    }

    private fun everyCalculate(passedList: MutableList<Any>): MutableList<Float> {
        val resultList = mutableListOf<Float>()
        var index = 0
        while (index < passedList.size) {
            val item = passedList[index]
            if (item is Float) {
                resultList.add(item)
            } else if (item is Char) {
                val operator = item
                val nextIndex = index + 1
                if (nextIndex < passedList.size && passedList[nextIndex] is Float) {
                    val nextDigit = passedList[nextIndex] as Float
                    val prevDigit = resultList.removeAt(resultList.size - 1)
                    when (operator) {
                        'x' -> resultList.add(prevDigit * nextDigit)
                        '/' -> resultList.add(prevDigit / nextDigit)
                        '+' -> resultList.add(prevDigit + nextDigit)
                        '-' -> resultList.add(prevDigit - nextDigit)
                    }
                    index = nextIndex
                }
            }
            index++
        }
        return resultList
    }

    private fun digitOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        var digitCount = 0
        for (char in tvMain.text) {
            if (char.isDigit() || char == '.') {
                if (digitCount < 15) { // Limit to 10 digits
                    currentDigit += char
                    digitCount++
                }
            } else {
                if (currentDigit.isNotEmpty()) {
                    list.add(currentDigit.toFloat())
                    currentDigit = ""
                }
                if (char != ' ') {
                    list.add(char)
                }
            }
            if (currentDigit.length > 15) break
        }
        if (currentDigit.isNotEmpty())
            list.add(currentDigit.toFloat())

        return list
    }
}
