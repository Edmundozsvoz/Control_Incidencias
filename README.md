# Registro de Incidencias

Aplicación Android académica en Kotlin que permite capturar el título, la descripción y la prioridad de una incidencia, preparar un reporte reactivo, monitorizar sensores en tiempo real, auditar el ciclo de vida de la `Activity` y separar el estado de la interfaz mediante un **`ViewModel`**.

Este avance corresponde a la **Semana 16: Práctica Individual con Entrega — Estado de pantalla con ViewModel en `RegistroIncidencias`** de la asignatura **Técnicas de Producción Industrial de Software I** (Ciclo 02-2026).

---

## Separación de Estado con `IncidenciaViewModel` (Semana 16)

Se separó el estado de negocio y formulario de la capa de vista creando la clase [`IncidenciaViewModel`](app/src/main/java/com/example/etps1/IncidenciaViewModel.kt) (`: ViewModel()`), mientras [`MainActivity`](app/src/main/java/com/example/etps1/MainActivity.kt) se encarga exclusivamente de renderizar la UI y reenviar los eventos del usuario.

### ¿Qué estado pertenece al `ViewModel` y qué estado pertenece a la UI?

| Capa | Responsabilidad y estado administrado | Sobrevive a rotación |
| :--- | :--- | :---: |
| **`IncidenciaViewModel`** | `titulo`, `descripcion`, `prioridad`, `reportePreparado` y las acciones de actualización (`actualizarTitulo`, `actualizarDescripcion`, `seleccionarPrioridad`, `prepararReporte`, `limpiarFormulario`). | Sí |
| **`MainActivity` (UI)** | Referencias de vistas (`TextInputEditText`, `MaterialCardView`, `TextView`), foco de entrada (`requestFocus`), errores visuales (`error`), mensajes efímeros (`Toast`) y suscripción temporal a sensores en `onResume`/`onPause`. | No (se recrea con la vista) |

---

## Bitácora del Ciclo de Vida y Logs (`Logcat`)

`MainActivity` y `IncidenciaViewModel` registran trazas mediante `TPIS1_LIFECYCLE` y `TPIS1_VIEWMODEL` para verificar cómo `IncidenciaViewModel` conserva la misma instancia mientras la `Activity` se destruye y recrea al rotar (`onDestroy(isChangingConfigurations=true)` → `onCreate(savedInstanceState=true)`):

```text
package:com.example.etps1 (tag:TPIS1_LIFECYCLE | tag:TPIS1_VIEWMODEL)
```

---

## Funcionalidades Acumuladas del Proyecto

- **Formulario reactivo con `ViewModel`:** Validación contextual de campos obligatorios, teclado configurado (`textCapSentences`, `actionNext`, `actionDone`) y selector táctil de prioridad (**Baja**, **Media**, **Alta**) conectado a `IncidenciaViewModel`.
- **Tablero de diagnóstico del dispositivo:** Monitoreo en tiempo real de **Acelerómetro** (`Sensor.TYPE_ACCELEROMETER`) y **Proximidad** (`Sensor.TYPE_PROXIMITY`).
- **Bitácora de ciclo de vida:** Trazas de callbacks (`onCreate`, `onStart`, `onRestart`, `onResume`, `onPause`, `onStop`, `onSaveInstanceState`, `onDestroy`).

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
