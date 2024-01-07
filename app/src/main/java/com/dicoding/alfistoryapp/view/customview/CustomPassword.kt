package com.dicoding.alfistoryapp.view.customview

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomPassword : TextInputLayout, View.OnTouchListener {

    private lateinit var password: TextInputEditText

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
        password = parentLayout.getChildAt(0) as TextInputEditText

        password.doAfterTextChanged { pass: Editable?->
            if (pass != null) {
                if (pass.toString().length < 8) {
                    this.error = "Password must be at least 8 characters"
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