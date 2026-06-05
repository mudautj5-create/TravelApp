package com.example.travelapp

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ScreenTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_screen_two)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnShowAll      = findViewById<Button>(R.id.btnShowAll)
        val btnShowMultiple = findViewById<Button>(R.id.btnShowMultiple)
        val btnBack         = findViewById<Button>(R.id.btnBack)

        displayItems(showAll = true)

        btnShowAll.setOnClickListener      { displayItems(showAll = true)  }
        btnShowMultiple.setOnClickListener { displayItems(showAll = false) }
        btnBack.setOnClickListener         { finish() }
    }

    private fun displayItems(showAll: Boolean) {
        val container = findViewById<LinearLayout>(R.id.llItemContainer)
        container.removeAllViews()

        var found = false

        for (i in PackingData.itemNames.indices) {
            if (!showAll && PackingData.quantities[i] < 2) continue

            found = true
            val card = layoutInflater.inflate(R.layout.item_card, container, false)

            card.findViewById<TextView>(R.id.tvName).text = PackingData.itemNames[i]
            
            card.findViewById<TextView>(R.id.tvCategory).text = 
                getString(R.string.item_category, PackingData.categories[i])
            
            card.findViewById<TextView>(R.id.tvQuantity).text = 
                getString(R.string.item_quantity, PackingData.quantities[i])
            
            val comments = PackingData.comments[i]
            // Fixed warning: reduced call on not-null type
            card.findViewById<TextView>(R.id.tvComments).text = 
                if (comments.isBlank()) getString(R.string.no_comments) else comments

            container.addView(card)
        }

        if (!found) {
            val tv = TextView(this).apply {
                text    = if (showAll) getString(R.string.no_items)
                          else getString(R.string.no_multiple_items)
                gravity = Gravity.CENTER
                setPadding(16, 40, 16, 16)
            }
            container.addView(tv)
        }
    }
}
