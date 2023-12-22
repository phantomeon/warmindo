package com.android.warmindo

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TransactionActivity : AppCompatActivity() {
    class CustomTypefaceSpan(private val newType: Typeface) : TypefaceSpan("") {
        override fun updateDrawState(ds: TextPaint) {
            applyCustomTypeFace(ds, newType)
        }

        override fun updateMeasureState(paint: TextPaint) {
            applyCustomTypeFace(paint, newType)
        }

        private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            paint.typeface = tf
        }
    }
    private fun updateUIWithUserName(userName: String) {
        val welcomeTextView = findViewById<TextView>(R.id.welcome_text)
        val fullText = "Selamat bekerja,\n$userName"
        val spannable = SpannableString(fullText)

        // Dapatkan font dari resources
        val customFont: Typeface? = ResourcesCompat.getFont(this, R.font.sequel_sans_bold_head)

        // Terapkan font pada nama pengguna
        customFont?.let {
            val customTypefaceSpan = CustomTypefaceSpan(it)
            val userNameStart = fullText.indexOf(userName)
            spannable.setSpan(
                customTypefaceSpan,
                userNameStart,
                userNameStart + userName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Set teks ke TextView
        welcomeTextView.text = spannable
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        //inisialisasi nama username yg dipanggil dari fungsi pada DashboardActivity
        val userName = intent.getStringExtra("EXTRA_USER_NAME") ?: "User"

        //update UI dengan username
        updateUIWithUserName(userName)

        //inisialisasi navbar dan text view dimasak dan selesai dimasak
        val cookingTextView: TextView = findViewById(R.id.textView_cooking)
        val servedTextView: TextView = findViewById(R.id.textView_served)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected), // state: selected
                intArrayOf(-android.R.attr.state_selected), // state: not selected
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.black), // color for selected
                ContextCompat.getColor(this, R.color.white), // color for not selected
            )
        )
        cookingTextView.setTextColor(colorStateList)
        servedTextView.setTextColor(colorStateList)

        //logika navbar
        cookingTextView.setOnClickListener {
            // When cooking is clicked, set it as selected and reset served
            cookingTextView.isSelected = true
            servedTextView.isSelected = false
            // TODO: Update the UI or perform actions based on 'cooking' selection
        }

        servedTextView.setOnClickListener {
            // When served is clicked, set it as selected and reset cooking
            servedTextView.isSelected = true
            cookingTextView.isSelected = false
            // TODO: Update the UI or perform actions based on 'served' selection
        }

        //insialisasi recycler view navigation bar
        val cookingTab: TextView = findViewById(R.id.textView_cooking)
        val servedTab: TextView = findViewById(R.id.textView_served)
        val cookingRecyclerView: RecyclerView = findViewById(R.id.recyclerView_cooking)
        val servedRecyclerView: RecyclerView = findViewById(R.id.recyclerView_served)

        //logika recycler view navbar
        cookingTab.setOnClickListener {
            cookingTab.isSelected = true
            servedTab.isSelected = false
            cookingRecyclerView.visibility = View.VISIBLE
            servedRecyclerView.visibility = View.GONE
            // Optionally, refresh the cookingRecyclerView adapter
        }

        servedTab.setOnClickListener {
            servedTab.isSelected = true
            cookingTab.isSelected = false
            servedRecyclerView.visibility = View.VISIBLE
            cookingRecyclerView.visibility = View.GONE
            // Optionally, refresh the servedRecyclerView adapter
        }
    }
}