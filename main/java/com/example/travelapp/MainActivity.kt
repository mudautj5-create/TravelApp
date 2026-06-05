package com.example.travelapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnAddItem    = findViewById<Button>(R.id.btnAddItem)
        val btnViewList   = findViewById<Button>(R.id.btnViewList)
        val btnExit       = findViewById<Button>(R.id.btnExit)
        val tvItemCount   = findViewById<TextView>(R.id.tvItemCount)

        updateItemCount(tvItemCount)

        btnAddItem.setOnClickListener {
            showAddItemDialog(tvItemCount)
        }

        btnViewList.setOnClickListener {
            val intent = Intent(this, ScreenTwo::class.java)
            startActivity(intent)
        }

        btnExit.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        val tvItemCount = findViewById<TextView>(R.id.tvItemCount)
        updateItemCount(tvItemCount)
    }

    private fun updateItemCount(tv: TextView) {
        tv.text = getString(R.string.item_count_label, PackingData.itemNames.size)
    }

    private fun showAddItemDialog(tvItemCount: TextView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_item, null)

        val etName     = dialogView.findViewById<EditText>(R.id.etItemName)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val etQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)
        val etComments = dialogView.findViewById<EditText>(R.id.etComments)

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_add_title)
            .setView(dialogView)
            .setPositiveButton(R.string.btn_add) { _, _ ->
                val name     = etName.text.toString().trim()
                val category = etCategory.text.toString().trim()
                val qtyText  = etQuantity.text.toString().trim()
                val comments = etComments.text.toString().trim()

                if (name.isEmpty() || category.isEmpty() || qtyText.isEmpty()) {
                    Toast.makeText(this, R.string.msg_fill_fields, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val qty = qtyText.toIntOrNull()
                if (qty == null || qty < 1) {
                    Toast.makeText(this, R.string.msg_invalid_qty, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                PackingData.itemNames.add(name)
                PackingData.categories.add(category)
                PackingData.quantities.add(qty)
                PackingData.comments.add(comments)

                updateItemCount(tvItemCount)
                Toast.makeText(this, getString(R.string.msg_added_format, name), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }
}
