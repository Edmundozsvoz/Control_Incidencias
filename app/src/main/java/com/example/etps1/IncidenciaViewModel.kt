package com.example.etps1

import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * Contenedor de estado sencillo para la pantalla de Registro de Incidencias (Semana 16).
 * Mantiene los datos del formulario y responde a los eventos de la UI sobreviviendo
 * a cambios de configuracion (como la rotacion de pantalla).
 */
class IncidenciaViewModel : ViewModel() {

    var titulo: String = ""
        private set

    var descripcion: String = ""
        private set

    var prioridad: String = PRIORITY_MEDIUM
        private set

    var reportePreparado: Boolean = false
        private set

    init {
        Log.d(TAG_VIEWMODEL, "IncidenciaViewModel creado (instancia=${hashCode()})")
    }

    fun actualizarTitulo(valor: String) {
        if (titulo != valor) {
            titulo = valor
            if (reportePreparado) {
                reportePreparado = false
            }
            Log.d(TAG_VIEWMODEL, "Evento actualizarTitulo -> '$titulo'")
        }
    }

    fun actualizarDescripcion(valor: String) {
        if (descripcion != valor) {
            descripcion = valor
            if (reportePreparado) {
                reportePreparado = false
            }
            Log.d(TAG_VIEWMODEL, "Evento actualizarDescripcion -> '$descripcion'")
        }
    }

    fun seleccionarPrioridad(nuevaPrioridad: String): Boolean {
        if (prioridad != nuevaPrioridad) {
            prioridad = nuevaPrioridad
            if (reportePreparado) {
                reportePreparado = false
            }
            Log.d(TAG_VIEWMODEL, "Evento seleccionarPrioridad -> '$prioridad'")
            return true
        }
        return false
    }

    fun prepararReporte(): Boolean {
        titulo = titulo.trim()
        descripcion = descripcion.trim()
        val valido = titulo.isNotEmpty() && descripcion.isNotEmpty()
        reportePreparado = valido
        Log.d(
            TAG_VIEWMODEL,
            "Evento prepararReporte -> valido=$valido, titulo='$titulo', prioridad='$prioridad'"
        )
        return valido
    }

    fun limpiarFormulario() {
        titulo = ""
        descripcion = ""
        prioridad = PRIORITY_MEDIUM
        reportePreparado = false
        Log.d(TAG_VIEWMODEL, "Evento limpiarFormulario -> Estado reiniciado")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG_VIEWMODEL, "IncidenciaViewModel destruido (onCleared, instancia=${hashCode()})")
    }

    companion object {
        private const val TAG_VIEWMODEL = "TPIS1_VIEWMODEL"
        const val PRIORITY_LOW = "Baja"
        const val PRIORITY_MEDIUM = "Media"
        const val PRIORITY_HIGH = "Alta"
    }
}
