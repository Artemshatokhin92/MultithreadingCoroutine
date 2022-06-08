package com.shatokhin.multithreadingcoroutine.domain

import kotlinx.coroutines.delay

class NumberPiObj(private val changeListener: ChangeListener) {
    private var numberPiString = "3,14"
        private set(value) {
            field = value
            changeListener.changingNumberPi(numberPiString)
        }

    private var calculator: Calculator? = null

    suspend fun start(){
        calculator?.kill()
        calculator = Calculator()
        calculator?.start()
    }

    fun pause(){
        calculator?.kill()
    }

    suspend fun reset(){
        clearNumberPi()
        start()
    }

    private fun clearNumberPi(){
        numberPiString = "3,14"
    }

    inner class Calculator {
        private var isEnable = true

        fun kill(){
            isEnable = false
        }

        suspend fun start() {
            while ( isEnable ){
                delay(500)
                if ( isEnable ) {
                    numberPiString += getNextNumber()
                }
            }
        }
    }

    private fun calculateNumberInPosition(position: Int): Int {
        return (0..9).random()
    }

    fun getNextNumber(): Int {
        return calculateNumberInPosition(numberPiString.length)
    }

    interface ChangeListener{
        fun changingNumberPi(numberPi: String)
    }

}