package com.dicoding.alfistoryapp.view.customview

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEmail : TextInputLayout, View.OnTouchListener {

    private lateinit var email: TextInputEditText

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        val parentLayout = getChildAt(0) as ViewGroup
        email = parentLayout.getChildAt(0) as TextInputEditText

        email.doAfterTextChanged { email : Editable? ->
            if (email != null) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.trim())
                        .matches()) {
                    this.error = "wrong email format  "
                } else {
                    this.error = null
                }
            }
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }
}