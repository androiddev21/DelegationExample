package com.example.delegationexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity(),
    AnalyticsLogger by AnalyticsLoggerImpl(),
    DeepLinkHandler by DeepLinkHandlerImpl() {

    private val ibj by MyLazy {
        println("hello world")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerLifecycleOwner(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(this, intent)
    }
}

interface AnalyticsLogger {
    fun registerLifecycleOwner(owner: LifecycleOwner)
}

class AnalyticsLoggerImpl : AnalyticsLogger, LifecycleEventObserver {

    override fun registerLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> println("User opens the screen")
            Lifecycle.Event.ON_PAUSE -> println("User leaves the screen")
            else -> Unit
        }
    }
}

interface DeepLinkHandler {
    fun handleDeepLink(activity: AppCompatActivity, intent: Intent?)
}

class DeepLinkHandlerImpl : DeepLinkHandler {

    override fun handleDeepLink(activity: AppCompatActivity, intent: Intent?) {
        TODO("Not yet implemented")
    }
}

class MyLazy<out T : Any>(private val initialize: () -> T) {

    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (value == null) {
            value = initialize()
            value!!
        } else value!!
    }
}