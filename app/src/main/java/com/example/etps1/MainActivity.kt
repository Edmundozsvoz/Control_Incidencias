package com.example.etps1

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    // ── Form views ──────────────────────────────────────────────────────────
    private lateinit var titleInputLayout: TextInputLayout
    private lateinit var descriptionInputLayout: TextInputLayout
    private lateinit var titleInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var statusText: TextView
    private lateinit var statusDetailText: TextView

    // ── Priority views ───────────────────────────────────────────────────────
    private lateinit var cardPriorityLow: MaterialCardView
    private lateinit var cardPriorityMedium: MaterialCardView
    private lateinit var cardPriorityHigh: MaterialCardView
    private lateinit var textPriorityLow: TextView
    private lateinit var textPriorityMedium: TextView
    private lateinit var textPriorityHigh: TextView

    // ── Sensor views ─────────────────────────────────────────────────────────
    private lateinit var sensorStatusChip: TextView
    private lateinit var sensorMotionText: TextView
    private lateinit var sensorXText: TextView
    private lateinit var sensorYText: TextView
    private lateinit var sensorZText: TextView

    // ── Form state ───────────────────────────────────────────────────────────
    private var titleState = ""
    private var descriptionState = ""
    private var priorityState = PRIORITY_MEDIUM
    private var reportPrepared = false

    // ── Sensor state ─────────────────────────────────────────────────────────
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var sensorAvailable = false

    // ─────────────────────────────────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setupKeyboardActions()
        setupTactileInteractions()
        setupSensor()
        restoreState(savedInstanceState)
        observeFormState()

        findViewById<Button>(R.id.createReportButton).setOnClickListener {
            prepareReport()
        }
    }

    // ── View binding ──────────────────────────────────────────────────────────
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

        sensorStatusChip = findViewById(R.id.sensorStatusChip)
        sensorMotionText = findViewById(R.id.sensorMotionText)
        sensorXText = findViewById(R.id.sensorX)
        sensorYText = findViewById(R.id.sensorY)
        sensorZText = findViewById(R.id.sensorZ)
    }

    // ── Keyboard IME actions ─────────────────────────────────────────────────
    private fun setupKeyboardActions() {
        titleInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                descriptionInput.requestFocus()
                true
            } else false
        }
        descriptionInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                prepareReport()
                true
            } else false
        }
    }

    // ── Priority tactile interaction ─────────────────────────────────────────
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
        val selectedStroke = ContextCompat.getColor(this, R.color.priority_selected_stroke)
        val selectedBg = ContextCompat.getColor(this, R.color.priority_selected_bg)
        val selectedText = ContextCompat.getColor(this, R.color.primary_blue)
        val unselectedStroke = ContextCompat.getColor(this, R.color.priority_unselected_stroke)
        val unselectedBg = ContextCompat.getColor(this, R.color.priority_unselected_bg)
        val unselectedText = ContextCompat.getColor(this, R.color.body_text)
        val density = resources.displayMetrics.density
        val stroke2dp = (2 * density).toInt()
        val stroke1dp = (1 * density).toInt()

        fun applyCard(card: MaterialCardView, label: TextView, selected: Boolean) {
            card.strokeColor = if (selected) selectedStroke else unselectedStroke
            card.strokeWidth = if (selected) stroke2dp else stroke1dp
            card.setCardBackgroundColor(if (selected) selectedBg else unselectedBg)
            label.setTextColor(if (selected) selectedText else unselectedText)
        }

        applyCard(cardPriorityLow, textPriorityLow, priorityState == PRIORITY_LOW)
        applyCard(cardPriorityMedium, textPriorityMedium, priorityState == PRIORITY_MEDIUM)
        applyCard(cardPriorityHigh, textPriorityHigh, priorityState == PRIORITY_HIGH)
    }

    // ── Sensor setup ─────────────────────────────────────────────────────────
    private fun setupSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorAvailable = accelerometer != null

        if (!sensorAvailable) {
            renderSensorUnavailable()
        }
    }

    override fun onResume() {
        super.onResume()
        if (sensorAvailable) {
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // ── SensorEventListener ───────────────────────────────────────────────────
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            updateSensorUI(x, y, z)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { /* no-op */ }

    private fun updateSensorUI(x: Float, y: Float, z: Float) {
        sensorXText.text = "%.3f m/s²".format(x)
        sensorYText.text = "%.3f m/s²".format(y)
        sensorZText.text = "%.3f m/s²".format(z)

        // Calculate total acceleration magnitude excluding gravity
        val magnitude = sqrt((x * x + y * y + z * z).toDouble())
        val isMoving = magnitude > 12.0 // threshold above normal gravity (≈9.8 m/s²)

        val chipBg = sensorStatusChip.background as? GradientDrawable
        if (isMoving) {
            sensorMotionText.text = getString(R.string.sensor_motion)
            sensorMotionText.setTextColor(ContextCompat.getColor(this, R.color.sensor_motion_color))
            chipBg?.setColor(ContextCompat.getColor(this, R.color.sensor_motion_color))
            sensorStatusChip.text = getString(R.string.sensor_active)
        } else {
            sensorMotionText.text = getString(R.string.sensor_stable)
            sensorMotionText.setTextColor(ContextCompat.getColor(this, R.color.sensor_active_color))
            chipBg?.setColor(ContextCompat.getColor(this, R.color.sensor_active_color))
            sensorStatusChip.text = getString(R.string.sensor_active)
        }
    }

    private fun renderSensorUnavailable() {
        val chipBg = sensorStatusChip.background as? GradientDrawable
        chipBg?.setColor(ContextCompat.getColor(this, R.color.sensor_inactive_color))
        sensorStatusChip.text = getString(R.string.sensor_unavailable)
        sensorMotionText.text = getString(R.string.sensor_unavailable)
        sensorMotionText.setTextColor(ContextCompat.getColor(this, R.color.sensor_inactive_color))
        sensorXText.text = "N/A"
        sensorYText.text = "N/A"
        sensorZText.text = "N/A"
    }

    // ── Form state ────────────────────────────────────────────────────────────
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
            val updated = it?.toString().orEmpty()
            titleInputLayout.error = null
            if (updated != titleState) {
                titleState = updated
                invalidatePreparedReport()
            }
        }
        descriptionInput.doAfterTextChanged {
            val updated = it?.toString().orEmpty()
            descriptionInputLayout.error = null
            if (updated != descriptionState) {
                descriptionState = updated
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

        val titleOk = titleState.isNotEmpty()
        val descOk = descriptionState.isNotEmpty()

        titleInputLayout.error = if (titleOk) null else getString(R.string.required_field_error)
        descriptionInputLayout.error = if (descOk) null else getString(R.string.required_field_error)

        if (!titleOk || !descOk) {
            reportPrepared = false
            renderFeedback()
            if (!titleOk) titleInput.requestFocus() else descriptionInput.requestFocus()
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
