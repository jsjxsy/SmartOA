package com.gx.smart.smartoa.activity.ui.messages

import android.os.Bundle
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity

class MessageActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
    }

    companion object {
        const val INTENT_MESSAGE = "toAction"
        const val INTENT_KEY = "type"
    }
}
