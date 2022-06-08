package com.shatokhin.multithreadingcoroutine.domain

import kotlinx.coroutines.delay

class MyTimer(private val changeListener: ChangeListener) {
    private var timeInSecond = 0
    private set(value) {
        field = value
        changeListener.changingTime(timeInSecond)
    }

    private var counter: Counter? = null

    fun increment(){
        timeInSecond++
    }

    suspend fun start(){
        counter?.kill()
        counter = Counter()
        counter?.start()
    }

    fun pause(){
        counter?.kill()
    }

    private fun clearCounter(){
        timeInSecond = 0
    }

    suspend fun reset(){
        clearCounter()
        start()
    }

    interface ChangeListener{
        fun changingTime(newTimeInSecond: Int)
    }

    inner class Counter {
        private var isEnable = true

        fun kill(){
            isEnable = false
        }

        suspend fun start() {
            while ( isEnable ){
                delay(1000)
                if ( isEnable ) increment() // если во время sleep не вызвали pause(), то выполняем increment()
            }
        }

    }

}