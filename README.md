# Registro de Incidencias

Aplicación Android académica que permite capturar el título, la descripción y la prioridad de una incidencia y preparar un reporte con retroalimentación inmediata. Este avance corresponde a la actividad evaluada de la **Semana 11: Segundo avance de investigación y evidencia técnica con sensor** de Técnicas de Producción Industrial de Software I (Ciclo 02-2026).

## Funcionalidades implementadas

- Pantalla con título, instrucción y formulario accesible.
- Campo de título con teclado contextual configurado (`textCapSentences` y acción IME `actionNext`).
- Campo multilínea para descripción breve (`textMultiLine` y acción IME `actionDone`).
- Interacción táctil de alto nivel para selección de prioridad (**Baja**, **Media**, **Alta**) con `MaterialCardView`.
- Estado local reactivo que captura texto y prioridad seleccionada.
- Validación de campos obligatorios con errores contextuales.
- Botón principal **Crear reporte** accesible por toque y desde el teclado.
- Retroalimentación visible inmediata con título, descripción y prioridad capturados.
- Conservación íntegra del estado ante recreación de la actividad (`onSaveInstanceState`).
- Diseño adaptable mediante desplazamiento vertical para diversas pantallas.
- **[Semana 11] Integración del acelerómetro** mediante `SensorManager` y `SensorEventListener`:
  - Lectura en tiempo real de los ejes **X**, **Y**, **Z** del acelerómetro.
  - Chip de estado del sensor (**Activo** / **No disponible**).
  - Mensaje interpretativo dinámico: **"Dispositivo estable"** o **"¡Movimiento detectado!"** según la magnitud de la aceleración.
  - Registro del sensor en `onResume()` y desregistro en `onPause()` para conservar batería.
  - Manejo explícito del caso donde el sensor no está disponible en el dispositivo.

> Sensor verificado: **TYPE_ACCELEROMETER** (acelerómetro de 3 ejes).
> El reporte se prepara en memoria; todavía no guarda información en una base de datos.

## Decisiones técnicas y nivel de abstracción

El proyecto utiliza Kotlin, AppCompat, Material Components y layouts XML.
- **Sensor elegido:** `Sensor.TYPE_ACCELEROMETER` porque permite detectar movimiento, sacudida y la inclinación aproximada del dispositivo — información útil para detectar condiciones de uso inestables al momento de registrar una incidencia.
- **Integración técnica:** `MainActivity` implementa la interfaz `SensorEventListener` directamente. El sensor se registra en `onResume()` y se desregistra en `onPause()` siguiendo las buenas prácticas del ciclo de vida de Android para minimizar consumo de batería.
- **Retroalimentación visual:** La magnitud vectorial (√(x²+y²+z²)) se compara contra un umbral de 12 m/s² (por encima de la gravedad estándar de ≈9.8 m/s²) para determinar si hay movimiento activo.
- **Teclado contextual:** `inputType="textCapSentences"` e `imeOptions="actionNext"` en el título para una experiencia fluida entre campos.
- **Interacción táctil:** `MaterialCardView` con `setOnClickListener` para el selector de prioridad — abstracción de alto nivel con *ripple effect*, accesibilidad y gestión de estado integradas.
- **Dificultad resuelta:** Integrar el ciclo de vida del sensor con el ciclo de vida del formulario (`onSaveInstanceState`) sin conflictos. Se resolvió separando la lógica del sensor en métodos propios (`setupSensor`, `updateSensorUI`, `renderSensorUnavailable`) e implementando `SensorEventListener` en la `Activity`.

## Cómo ejecutar

1. Clona o descarga este repositorio.
2. Abre la carpeta raíz en Android Studio.
3. Espera a que Gradle sincronice las dependencias.
4. Selecciona un emulador o dispositivo con Android API 24 o posterior.
5. Ejecuta la configuración `app`.
6. Observa las lecturas del acelerómetro en la tarjeta inferior. Mueve el dispositivo para ver el mensaje de movimiento.
7. Escribe un título y una descripción, selecciona la prioridad, y presiona **Crear reporte**.

También puedes compilar desde PowerShell, usando el JDK incluido con Android Studio:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug
```

## Evidencia esperada para Blackboard

- Enlace público del repositorio: <https://github.com/Edmundozsvoz/Control_Incidencias>
- Captura de Android Studio con el proyecto abierto y los cambios de la semana.
- Captura de la app ejecutándose con las lecturas del acelerómetro visibles (ejes X, Y, Z y mensaje de estado).
- Captura del repositorio con el commit de la semana 11.
- Breve explicación del sensor usado, propósito, limitaciones y próxima mejora.

## Próximos pasos

- Persistir incidencias en una base de datos local (Room).
- Utilizar la detección de movimiento del acelerómetro para disparar automáticamente un alerta de incidencia.
- Mostrar un historial de reportes.

## Autor
- **Estudiante:** Henry Edmundo Rodríguez Ávalos
- **Carnet:** 2908092023
- **Sección:** 01
- **GitHub:** [Edmundozsvoz](https://github.com/Edmundozsvoz)
