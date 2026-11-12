package com.example.packinglistmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    // --- Parallel Arrays to store data ---
    // We use MutableList to allow dynamic resizing, but treat them as arrays
    private val itemNames = mutableListOf<String>()
    private val categories = mutableListOf<String>()
    private val quantities = mutableListOf<Int>()
    private val comments = mutableListOf<String>()

    // UI References for Screen One
    private lateinit var etItemName: EditText
    private lateinit var etCategory: EditText
    private lateinit var etQuantity: EditText
    private lateinit var etComments: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load Screen One by default
        showScreenOne()
    }

    // ==========================================
    // SCREEN ONE: Input & Navigation
    // ==========================================
    private fun showScreenOne() {
        setContentView(R.layout.activity_main)

        // Initialize UI components
        etItemName = findViewById(R.id.etItemName)
        etCategory = findViewById(R.id.etCategory)
        etQuantity = findViewById(R.id.etQuantity)
        etComments = findViewById(R.id.etComments)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnViewList = findViewById<Button>(R.id.btnViewList)
        val btnExit = findViewById<Button>(R.id.btnExit)

        // Add Item Logic
        btnAdd.setOnClickListener {
            addPackingItem()
        }

        // Navigation to Screen Two
        btnViewList.setOnClickListener {
            showScreenTwo()
        }

        // Exit App Logic
        btnExit.setOnClickListener {
            finish() // Closes the activity and exits app
        }
    }

    private fun addPackingItem() {
        val name = etItemName.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val quantityStr = etQuantity.text.toString().trim()
        val comment = etComments.text.toString().trim()

        // --- ERROR HANDLING ---
        if (name.isEmpty() || category.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill in Name, Category, and Quantity", Toast.LENGTH_SHORT).show()
            Log.d("PackingApp", "Error: Empty fields detected.")
            return
        }

        val quantity = quantityStr.toIntOrNull()
        if (quantity == null) {
            Toast.makeText(this, "Quantity must be a valid number", Toast.LENGTH_SHORT).show()
            Log.d("PackingApp", "Error: Invalid quantity input.")
            return
        }

        // Add to Parallel Arrays
        itemNames.add(name)
        categories.add(category)
        quantities.add(quantity)
        comments.add(comment)

        Log.d("PackingApp", "Added: $name, Qty: $quantity")

        // Clear inputs for next entry
        etItemName.text.clear()
        etCategory.text.clear()
        etQuantity.text.clear()
        etComments.text.clear()
        Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show()
    }

    // ==========================================
    // SCREEN TWO: Display & Filtering
    // ==========================================
    private fun showScreenTwo() {
        setContentView(R.layout.screen_two)

        val btnShowAll = findViewById<Button>(R.id.btnShowAll)
        val btnShowFiltered = findViewById<Button>(R.id.btnShowFiltered)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val tvDisplay = findViewById<TextView>(R.id.tvDisplay)

        // 1. Button: Display All
        btnShowAll.setOnClickListener {
            tvDisplay.text = generateFullList()
        }

        // 2. Button: Display Items with Qty >= 2
        btnShowFiltered.setOnClickListener {
            tvDisplay.text = generateFilteredList()
        }

        // 3. Button: Return to Main Screen
        btnBack.setOnClickListener {
            showScreenOne()
        }
    }

    // Loop to display all items
    private fun generateFullList(): String {
        if (itemNames.isEmpty()) return "No items in list."

        val sb = StringBuilder()
        // Using a 'for' loop with indices to access parallel arrays
        for (i in itemNames.indices) {
            sb.append("Name: ${itemNames[i]}\n")
            sb.append("Category: ${categories[i]}\n")
            sb.append("Qty: ${quantities[i]}\n")
            sb.append("Comment: ${comments[i]}\n")
            sb.append("-------------------\n")
        }
        return sb.toString()
    }

    // Loop to filter items
    private fun generateFilteredList(): String {
        if (itemNames.isEmpty()) return "No items to filter."

        val sb = StringBuilder()
        sb.append("Items with Quantity >= 2:\n\n")
        var found = false

        for (i in itemNames.indices) {
            if (quantities[i] >= 2) {
                sb.append("- ${itemNames[i]} (Qty: ${quantities[i]})\n")
                found = true
            }
        }

        return if (found) sb.toString() else "No items found with quantity 2 or more."
    }
}
