package com.example.etps1

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var titleInputLayout: TextInputLayout
    private lateinit var descriptionInputLayout: TextInputLayout
    private lateinit var titleInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var statusText: TextView
    private lateinit var statusDetailText: TextView

    private var titleState = ""
    private var descriptionState = ""
    private var reportPrepared = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        restoreState(savedInstanceState)
        observeFormState()

        findViewById<Button>(R.id.createReportButton).setOnClickListener {
            prepareReport()
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

    private fun bindViews() {
        titleInputLayout = findViewById(R.id.titleInputLayout)
        descriptionInputLayout = findViewById(R.id.descriptionInputLayout)
        titleInput = findViewById(R.id.titleInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        statusText = findViewById(R.id.statusText)
        statusDetailText = findViewById(R.id.statusDetailText)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        titleState = savedInstanceState?.getString(KEY_TITLE).orEmpty()
        descriptionState = savedInstanceState?.getString(KEY_DESCRIPTION).orEmpty()
        reportPrepared = savedInstanceState?.getBoolean(KEY_REPORT_PREPARED) ?: false

        titleInput.setText(titleState)
        descriptionInput.setText(descriptionState)
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
            statusText.setTextColor(getColor(R.color.success_green))
            statusDetailText.text = getString(R.string.prepared_report_detail, descriptionState)
            statusDetailText.visibility = View.VISIBLE
        } else {
            statusText.setText(R.string.empty_report_status)
            statusText.setTextColor(getColor(R.color.body_text))
            statusDetailText.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_TITLE, titleState)
        outState.putString(KEY_DESCRIPTION, descriptionState)
        outState.putBoolean(KEY_REPORT_PREPARED, reportPrepared)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_TITLE = "incident_title"
        private const val KEY_DESCRIPTION = "incident_description"
        private const val KEY_REPORT_PREPARED = "report_prepared"
    }
}
