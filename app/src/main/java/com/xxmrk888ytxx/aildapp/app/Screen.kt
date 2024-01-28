package com.xxmrk888ytxx.aildapp.app

sealed class Screen(val route:String) {
    data object RecordScreen: Screen("RecordScreen")
}
