package com.shatokhin.multithreadingcoroutine.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shatokhin.multithreadingcoroutine.domain.MyTimer
import com.shatokhin.multithreadingcoroutine.domain.NumberPiObj
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelMain: ViewModel() {

    private lateinit var timer: MyTimer
    private lateinit var numberPiObj: NumberPiObj

    var isEnable = false
    private set

    init {
        initTimer()
        initNumberPiObj()
    }

    private val mTimeInSecond = MutableLiveData<Int>()
    val timeInSecond: LiveData<Int>
        get() = mTimeInSecond

    private val mNumberPi = MutableLiveData<String>()
    val numberPi: LiveData<String>
        get() = mNumberPi



    private fun initNumberPiObj() {
        val listenerChangingNumberPi = object : NumberPiObj.ChangeListener{
            override fun changingNumberPi(numberPi: String) {
                viewModelScope.launch(Dispatchers.Main) {
                    mNumberPi.value = numberPi
                }
            }
        }

        numberPiObj = NumberPiObj(listenerChangingNumberPi)
    }

    private fun initTimer() {
        val listenerChangingTime = object : MyTimer.ChangeListener{
            override fun changingTime(newTimeInSecond: Int) {
                viewModelScope.launch(Dispatchers.Main) {
                    mTimeInSecond.value = newTimeInSecond
                }
            }

        }

        timer = MyTimer(listenerChangingTime)
    }


    fun start(){
        isEnable = true

        viewModelScope.launch(Dispatchers.Default) {
            numberPiObj.start()
        }
        viewModelScope.launch(Dispatchers.Default) {
            timer.start()
        }

    }

    fun pause(){
        isEnable = false
        numberPiObj.pause()
        timer.pause()
    }

    fun reset(){
        isEnable = true

        viewModelScope.launch(Dispatchers.Default) {
            numberPiObj.reset()
        }
        viewModelScope.launch(Dispatchers.Default) {
            timer.reset()
        }
    }

    fun pauseForRestartActivity(){
        numberPiObj.pause()
        timer.pause()
    }
}