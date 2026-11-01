# Registro de Incidencias

Aplicación Android académica en Kotlin que permite capturar el título, la descripción y la prioridad de una incidencia, preparar un reporte reactivo, monitorizar sensores en tiempo real y auditar el ciclo de vida completo de la `Activity` mediante bitácora en **Logcat**.

Este avance corresponde a la **Semana 14: Trabajo Individual Evaluado — Bitácora del ciclo de vida en `RegistroIncidencias`** (40% del cuarto período) de la asignatura **Técnicas de Producción Industrial de Software I** (Ciclo 02-2026).

---

## Bitácora del Ciclo de Vida de `MainActivity` (Semana 14)

`MainActivity` está instrumentada con la etiqueta `TPIS1_LIFECYCLE` mediante `Log.d("TPIS1_LIFECYCLE", ...)` para registrar las transiciones de estado reales ante acciones del usuario y del sistema operativo:

- **`onCreate(savedInstanceState)`**: Creación inicial de la instancia (`savedInstanceState=false`) o recreación tras cambio de configuración (`savedInstanceState=true`), inflado de vistas y restauración de formulario.
- **`onStart()`**: La ventana de la actividad se hace visible en pantalla.
- **`onRestart()`**: Transición intermedia cuando la actividad regresa a primer plano tras haber estado detenida (`onStop`).
- **`onResume()`**: La actividad entra en estado interactivo (`RESUMED`) y registra los sensores (`registerSensors()`).
- **`onPause()`**: Pérdida de foco interactivo; libera de inmediato los sensores de hardware (`unregisterSensors()`).
- **`onStop()`**: La actividad deja de ser visible por completo (ej. al ir a *Home*).
- **`onSaveInstanceState(outState)`**: Respaldo del estado del formulario (`título`, `descripción`, `prioridad` y bandera de reporte) en el `Bundle`.
- **`onDestroy()`**: Destrucción de la instancia, distinguiendo recreación por configuración (`isChangingConfigurations=true`) frente a cierre definitivo con *Back* (`isChangingConfigurations=false`).

### Resumen de Escenarios Verificados en Emulador (Pixel 7 · Android 14 / API 34)

| Escenario | Secuencia observada en Logcat (`TPIS1_LIFECYCLE`) | Estado final | Interpretación técnica |
| :--- | :--- | :--- | :--- |
| **1. Apertura inicial** | `onCreate (savedInstanceState=false)` → `onStart` → `onResume` | `RESUMED` | Arranque en frío: Android instancia `MainActivity`, vincula vistas, activa sensores y entrega el foco al usuario. |
| **2. Ir a Home y regresar** | Salida: `onPause` → `onStop` → `onSaveInstanceState`<br>Regreso: `onRestart` → `onStart` → `onResume` | `RESUMED` | Al ir a *Home* la actividad se oculta sin destruirse, liberando sensores y respaldando el estado; al volver, reutiliza la instancia vía `onRestart`. |
| **3A. Rotación de pantalla** | `onPause` → `onStop` → `onSaveInstanceState` → `onDestroy (isChangingConfigurations=true)` → `onCreate (savedInstanceState=true)` → `onStart` → `onResume` | `RESUMED` | El cambio de configuración destruye y recrea `MainActivity`, restaurando los datos del formulario desde el `Bundle`. |
| **3B. Cierre con Back** | `onPause` → `onStop` → `onDestroy (isChangingConfigurations=false)` | `DESTROYED` | Finalización voluntaria: Android omite `onSaveInstanceState` y destruye definitivamente la instancia. |

### Cómo filtrar la bitácora en Logcat

En la ventana **Logcat** de Android Studio:
```text
package:com.example.etps1 tag:TPIS1_LIFECYCLE
```
O desde consola con ADB:
```powershell
adb logcat -v threadtime -s TPIS1_LIFECYCLE:D
```

---

## Funcionalidades Acumuladas del Proyecto

- **Formulario reactivo de incidencias:** Validación contextual de campos obligatorios, teclado configurado (`textCapSentences`, `actionNext`, `actionDone`) y selector táctil de prioridad (**Baja**, **Media**, **Alta**) con `MaterialCardView`.
- **Tablero de diagnóstico del dispositivo:** Monitoreo en tiempo real de **Acelerómetro** (`Sensor.TYPE_ACCELEROMETER`) y **Proximidad** (`Sensor.TYPE_PROXIMITY`), con chips de disponibilidad e interpretación semántica.
- **Persistencia ante recreación:** Conservación íntegra del formulario mediante `onSaveInstanceState`.

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
