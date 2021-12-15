package com.library

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_edit_text.view.*

class WordsCountView : LinearLayout {

    private var filter: InputFilter? = null
    private var wordLimit = 10

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initViews(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews(context, attrs)
    }


    private fun initViews(context: Context, attrs: AttributeSet?) {
        val view = inflate(context, R.layout.view_edit_text, null)
        addView(view)
        setCustomAttrs(context, attrs)
        setWordsLeftText(wordLimit)
        setListeners()
    }

    @SuppressLint("CustomViewStyleable")
    private fun setCustomAttrs(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WordCountEDitTextView)
            wordLimit = typedArray.getInteger(R.styleable.WordCountEDitTextView_word_limit, 10)
            typedArray.recycle()
        }
    }

    private fun setListeners() {
        et_word.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                val wordsLength = countWords(s.toString())
                if (wordsLength > wordLimit) {
                    val finalText = findFirstNNoOfWordsFromString(s.toString())
                    et_word.setText(finalText)
                    et_word.setSelection(finalText.length)
                } else {
                    if (s.isNotEmpty()) {
                        if (Character.isWhitespace(s.toString()[s.length - 1]) && wordsLength >= wordLimit) {
                            setCharLimit(et_word.text!!.length - 1)
                        } else {
                            removeFilter()
                        }
                    }
                    setWordsLeftText(wordLimit - wordsLength)
                }
            }
        })
    }


    @SuppressLint("SetTextI18n")
    private fun setWordsLeftText(length: Int) {
        tv_word.text = "$length Words"
    }

    private fun countWords(str: String): Int {
        val trim = str.trim { it <= ' ' }
        return if (trim.isEmpty()) 0 else trim.split("\\s".toRegex()).toTypedArray().size
    }

    private fun findFirstNNoOfWordsFromString(text: String): String {
        var nWords = ""
        val arr = text.split("\\s".toRegex())
        if (arr.isNotEmpty()) {
            nWords = arr[0]
            for (item in 1 until wordLimit) {
                    nWords = nWords + " " + arr[item]
            }
        }
        return nWords
    }

    private fun setCharLimit(max: Int) {
        filter = InputFilter.LengthFilter(max)
        et_word.filters = arrayOf(filter)
    }

    private fun removeFilter() {
        if (filter != null) {
            et_word.filters = arrayOfNulls(0)
            filter = null
        }

    }
}