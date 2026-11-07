# Registro de Incidencias

Aplicación Android académica en Kotlin que permite capturar el título, la descripción y la prioridad de una incidencia, preparar un reporte reactivo, monitorizar sensores en tiempo real, auditar el ciclo de vida completo de la `Activity` y conservar el estado del formulario ante recreaciones por rotación de pantalla.

Este avance corresponde a la **Semana 15: Práctica Individual con Entrega — Conserva el estado de `RegistroIncidencias`** de la asignatura **Técnicas de Producción Industrial de Software I** (Ciclo 02-2026).

---

## Conservación del Estado ante Recreación (Semana 15)

Se implementó y comparó el comportamiento del estado de la interfaz en `MainActivity` frente a la recreación de la `Activity` provocada por la rotación de pantalla (`onDestroy(isChangingConfigurations=true)` → `onCreate(savedInstanceState=true)`):

1. **Escenario con `remember` (Estado volátil en memoria de instancia):**
   - El estado (`titleState`, `descriptionState`, `priorityState`, `reportPrepared`) vive únicamente en memoria mientras la instancia actual existe (`var titulo by remember { mutableStateOf("") }`).
   - Al rotar el dispositivo, Android destruye y recrea `MainActivity`, reiniciando los campos a sus valores por defecto y perdiendo los datos ingresados por el usuario.

2. **Escenario con `rememberSaveable` / `savedInstanceState` (Estado persistente en `Bundle`):**
   - El estado se serializa automáticamente en el `Bundle` del sistema durante `onSaveInstanceState(outState)` (`var titulo by rememberSaveable { mutableStateOf("") }`) y se restaura en `onCreate(savedInstanceState)` mediante `restoreState(savedInstanceState)`.
   - Al rotar la pantalla, el título, la descripción, la prioridad seleccionada y el estado del reporte preparado se conservan intactos.

| Mecanismo evaluado | Sobrevive a recomposición / vista activa | Sobrevive a rotación (`onDestroy` → `onCreate`) | Resultado observado en `RegistroIncidencias` |
| :--- | :---: | :---: | :--- |
| **`remember` (Memoria volátil)** | Sí | No | Al girar el emulador a modo horizontal, el título, la descripción y la prioridad regresan a vacío / `Media`. |
| **`rememberSaveable` / `Bundle`** | Sí | Sí | Al girar el emulador a modo horizontal, se restauran íntegramente el título, la descripción, la prioridad `Alta` y el reporte. |

---

## Bitácora del Ciclo de Vida de `MainActivity` (Semana 14)

`MainActivity` está instrumentada con las etiquetas `TPIS1_LIFECYCLE` y `TPIS1_STATE` mediante `Log.d(...)` para registrar las transiciones de estado reales y la preservación del formulario:

- **`onCreate(savedInstanceState)`**: Creación inicial (`savedInstanceState=false`) o recreación tras cambio de configuración (`savedInstanceState=true`), inflado de vistas y llamada a `restoreState(savedInstanceState)`.
- **`onStart()` / `onRestart()` / `onResume()`**: Activación de ventana, retorno desde segundo plano y registro de sensores (`registerSensors()`).
- **`onPause()` / `onStop()`**: Liberación inmediata de sensores (`unregisterSensors()`) y paso a segundo plano.
- **`onSaveInstanceState(outState)`**: Respaldo del estado del formulario (`KEY_TITLE`, `KEY_DESCRIPTION`, `KEY_PRIORITY`, `KEY_REPORT_PREPARED`) en el `Bundle`.
- **`onDestroy()`**: Destrucción de la instancia (`isChangingConfigurations=true` al rotar vs. `false` al cerrar con *Back*).

### Cómo filtrar la bitácora en Logcat

```text
package:com.example.etps1 tag:TPIS1_LIFECYCLE tag:TPIS1_STATE
```

---

## Funcionalidades Acumuladas del Proyecto

- **Formulario reactivo de incidencias:** Validación contextual de campos obligatorios, teclado configurado (`textCapSentences`, `actionNext`, `actionDone`) y selector táctil de prioridad (**Baja**, **Media**, **Alta**) con `MaterialCardView`.
- **Tablero de diagnóstico del dispositivo:** Monitoreo en tiempo real de **Acelerómetro** (`Sensor.TYPE_ACCELEROMETER`) y **Proximidad** (`Sensor.TYPE_PROXIMITY`).
- **Persistencia ante recreación:** Conservación íntegra del formulario frente a rotación de pantalla (`rememberSaveable` / `savedInstanceState`).

---

## Cómo Compilar y Ejecutar

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug
```

---

## Datos del Estudiante

- **Estudiante:** HENRY EDMUNDO RODRÍGUEZ ÁVALOS
- **Carnet:** 2908092023
- **Sección:** 01
- **Repositorio:** [Control_Incidencias en GitHub](https://github.com/Edmundozsvoz/Control_Incidencias)
- **Rama:** `main`
