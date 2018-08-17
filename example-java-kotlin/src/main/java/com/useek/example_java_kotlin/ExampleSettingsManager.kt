package com.useek.example_java_kotlin

class ExampleSettingsManager {

    private object Holder { val INSTANCE = ExampleSettingsManager() }

    companion object {
        val sharedInstance: ExampleSettingsManager by lazy { Holder.INSTANCE }
    }

    var publisherId: String? = null
    var gameId: String? = null
    var userId: String? = null
    var loadingText: String? = null
    var isShowCloseButton: Boolean = false

    init {
        this.publisherId = "60d95e35d89800b0ee499e60d0735fb8"
        this.gameId = "122"
        this.userId = "496953"
        this.loadingText = "Please wait while loading..."
        this.isShowCloseButton = true
    }

}