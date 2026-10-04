package com.example.etps1

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var titleInputLayout: TextInputLayout
    private lateinit var descriptionInputLayout: TextInputLayout
    private lateinit var titleInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var statusText: TextView
    private lateinit var statusDetailText: TextView

    private lateinit var cardPriorityLow: MaterialCardView
    private lateinit var cardPriorityMedium: MaterialCardView
    private lateinit var cardPriorityHigh: MaterialCardView
    private lateinit var textPriorityLow: TextView
    private lateinit var textPriorityMedium: TextView
    private lateinit var textPriorityHigh: TextView

    private var titleState = ""
    private var descriptionState = ""
    private var priorityState = PRIORITY_MEDIUM
    private var reportPrepared = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setupKeyboardActions()
        setupTactileInteractions()
        restoreState(savedInstanceState)
        observeFormState()

        findViewById<Button>(R.id.createReportButton).setOnClickListener {
            prepareReport()
        }
    }

    private fun bindViews() {
        titleInputLayout = findViewById(R.id.titleInputLayout)
        descriptionInputLayout = findViewById(R.id.descriptionInputLayout)
        titleInput = findViewById(R.id.titleInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        statusText = findViewById(R.id.statusText)
        statusDetailText = findViewById(R.id.statusDetailText)

        cardPriorityLow = findViewById(R.id.cardPriorityLow)
        cardPriorityMedium = findViewById(R.id.cardPriorityMedium)
        cardPriorityHigh = findViewById(R.id.cardPriorityHigh)
        textPriorityLow = findViewById(R.id.textPriorityLow)
        textPriorityMedium = findViewById(R.id.textPriorityMedium)
        textPriorityHigh = findViewById(R.id.textPriorityHigh)
    }

    private fun setupKeyboardActions() {
        titleInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                descriptionInput.requestFocus()
                true
            } else {
                false
            }
        }

        descriptionInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                prepareReport()
                true
            } else {
                false
            }
        }
    }

    private fun setupTactileInteractions() {
        cardPriorityLow.setOnClickListener { selectPriority(PRIORITY_LOW) }
        cardPriorityMedium.setOnClickListener { selectPriority(PRIORITY_MEDIUM) }
        cardPriorityHigh.setOnClickListener { selectPriority(PRIORITY_HIGH) }
    }

    private fun selectPriority(priority: String) {
        if (priorityState != priority) {
            priorityState = priority
            updatePriorityUI()
            Toast.makeText(
                this,
                getString(R.string.priority_changed_feedback, priority),
                Toast.LENGTH_SHORT
            ).show()
            invalidatePreparedReport()
        }
    }

    private fun updatePriorityUI() {
        val selectedStrokeColor = ContextCompat.getColor(this, R.color.priority_selected_stroke)
        val selectedBgColor = ContextCompat.getColor(this, R.color.priority_selected_bg)
        val selectedTextColor = ContextCompat.getColor(this, R.color.primary_blue)

        val unselectedStrokeColor = ContextCompat.getColor(this, R.color.priority_unselected_stroke)
        val unselectedBgColor = ContextCompat.getColor(this, R.color.priority_unselected_bg)
        val unselectedTextColor = ContextCompat.getColor(this, R.color.body_text)

        val density = resources.displayMetrics.density
        val stroke2dp = (2 * density).toInt()
        val stroke1dp = (1 * density).toInt()

        // Low
        val isLow = priorityState == PRIORITY_LOW
        cardPriorityLow.strokeColor = if (isLow) selectedStrokeColor else unselectedStrokeColor
        cardPriorityLow.strokeWidth = if (isLow) stroke2dp else stroke1dp
        cardPriorityLow.setCardBackgroundColor(if (isLow) selectedBgColor else unselectedBgColor)
        textPriorityLow.setTextColor(if (isLow) selectedTextColor else unselectedTextColor)

        // Medium
        val isMedium = priorityState == PRIORITY_MEDIUM
        cardPriorityMedium.strokeColor = if (isMedium) selectedStrokeColor else unselectedStrokeColor
        cardPriorityMedium.strokeWidth = if (isMedium) stroke2dp else stroke1dp
        cardPriorityMedium.setCardBackgroundColor(if (isMedium) selectedBgColor else unselectedBgColor)
        textPriorityMedium.setTextColor(if (isMedium) selectedTextColor else unselectedTextColor)

        // High
        val isHigh = priorityState == PRIORITY_HIGH
        cardPriorityHigh.strokeColor = if (isHigh) selectedStrokeColor else unselectedStrokeColor
        cardPriorityHigh.strokeWidth = if (isHigh) stroke2dp else stroke1dp
        cardPriorityHigh.setCardBackgroundColor(if (isHigh) selectedBgColor else unselectedBgColor)
        textPriorityHigh.setTextColor(if (isHigh) selectedTextColor else unselectedTextColor)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        titleState = savedInstanceState?.getString(KEY_TITLE).orEmpty()
        descriptionState = savedInstanceState?.getString(KEY_DESCRIPTION).orEmpty()
        priorityState = savedInstanceState?.getString(KEY_PRIORITY) ?: PRIORITY_MEDIUM
        reportPrepared = savedInstanceState?.getBoolean(KEY_REPORT_PREPARED) ?: false

        titleInput.setText(titleState)
        descriptionInput.setText(descriptionState)
        updatePriorityUI()
        renderFeedback()
    }

    private fun observeFormState() {
        titleInput.doAfterTextChanged {
            val updatedTitle = it?.toString().orEmpty()
            titleInputLayout.error = null
            if (updatedTitle != titleState) {
                titleState = updatedTitle
                invalidatePreparedReport()
            }
        }
        descriptionInput.doAfterTextChanged {
            val updatedDescription = it?.toString().orEmpty()
            descriptionInputLayout.error = null
            if (updatedDescription != descriptionState) {
                descriptionState = updatedDescription
                invalidatePreparedReport()
            }
        }
    }

    private fun invalidatePreparedReport() {
        if (reportPrepared) {
            reportPrepared = false
            renderFeedback()
        }
    }

    private fun prepareReport() {
        titleState = titleInput.text?.toString()?.trim().orEmpty()
        descriptionState = descriptionInput.text?.toString()?.trim().orEmpty()

        val titleIsValid = titleState.isNotEmpty()
        val descriptionIsValid = descriptionState.isNotEmpty()

        titleInputLayout.error = if (titleIsValid) null else getString(R.string.required_field_error)
        descriptionInputLayout.error =
            if (descriptionIsValid) null else getString(R.string.required_field_error)

        if (!titleIsValid || !descriptionIsValid) {
            reportPrepared = false
            renderFeedback()
            if (!titleIsValid) titleInput.requestFocus() else descriptionInput.requestFocus()
            return
        }

        reportPrepared = true
        renderFeedback()
        Toast.makeText(this, R.string.report_ready_message, Toast.LENGTH_SHORT).show()
    }

    private fun renderFeedback() {
        if (reportPrepared) {
            statusText.text = getString(R.string.prepared_report_status, titleState)
            statusText.setTextColor(ContextCompat.getColor(this, R.color.success_green))
            statusDetailText.text = getString(R.string.prepared_report_detail, descriptionState, priorityState)
            statusDetailText.visibility = View.VISIBLE
        } else {
            statusText.setText(R.string.empty_report_status)
            statusText.setTextColor(ContextCompat.getColor(this, R.color.body_text))
            statusDetailText.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_TITLE, titleState)
        outState.putString(KEY_DESCRIPTION, descriptionState)
        outState.putString(KEY_PRIORITY, priorityState)
        outState.putBoolean(KEY_REPORT_PREPARED, reportPrepared)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_TITLE = "incident_title"
        private const val KEY_DESCRIPTION = "incident_description"
        private const val KEY_REPORT_PREPARED = "report_prepared"
        private const val KEY_PRIORITY = "incident_priority"
        const val PRIORITY_LOW = "Baja"
        const val PRIORITY_MEDIUM = "Media"
        const val PRIORITY_HIGH = "Alta"
    }
}
