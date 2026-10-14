# Registro de Incidencias

Aplicación Android académica que permite capturar el título, la descripción y la prioridad de una incidencia, preparar un reporte reactivo y monitorizar en tiempo real el estado de los sensores del teléfono.

Este avance corresponde a la **Semana 12: Práctica integradora: tablero de diagnóstico del dispositivo** de la asignatura **Técnicas de Producción Industrial de Software I** (Ciclo 02-2026).

---

## Tablero de Diagnóstico del Dispositivo (Semana 12)

La pantalla principal integra una tarjeta de diagnóstico de hardware en tiempo real que monitoriza múltiples sensores esenciales del dispositivo:

1. **Acelerómetro (`Sensor.TYPE_ACCELEROMETER`)**:
   - Monitoreo continuo de los ejes espaciales **X**, **Y**, **Z** (en m/s²).
   - Chip de estado dinámico (**Activo** / **No disponible**).
   - Cálculo de la magnitud vectorial de aceleración inercial ($M = \sqrt{X^2 + Y^2 + Z^2}$).
   - Interpretación reactiva contextual:
     - **Dispositivo estable** (verde `#15803D`): $M \le 12.0\text{ m/s}^2$.
     - **¡Movimiento detectado!** (rojo `#DC2626`): $M > 12.0\text{ m/s}^2$ (detecta sacudidas o traslados).

2. **Sensor de Proximidad (`Sensor.TYPE_PROXIMITY`)**:
   - Monitoreo de distancia frontal en centímetros.
   - Chip de estado (**Activo** / **No disponible**).
   - Interpretación reactiva contextual:
     - **Pantalla despejada (Lejos)**: Sin obstáculos frente a la pantalla.
     - **Objeto próximo detectado (Cerca)**: Presencia de objetos, mano o rostro a corta distancia.

3. **Manejo de Casos No Disponibles**:
   - Si el dispositivo físico o emulador carece de alguno de los sensores, se presenta el chip en color gris neutro con texto **No disponible** y lecturas **N/A**, previniendo fallos por `NullPointerException`.

---

## Control del Ciclo de Vida y Eficiencia Energética

Para cumplir con las pautas de rendimiento y buenas prácticas de Android:
- **`onResume()`**: Se registran los listeners de ambos sensores (`SensorManager.SENSOR_DELAY_NORMAL`), iniciando la lectura cuando la interfaz de usuario está en primer plano.
- **`onPause()`**: Se desregistran los listeners mediante `sensorManager.unregisterListener(this)`. Esto libera inmediatamente el hardware, detiene el procesamiento en segundo plano y evita fugas de memoria (*memory leaks*) o descargas de batería cuando la aplicación no está visible o la pantalla está bloqueada.

---

## Funcionalidades del Formulario de Incidencias

- Formulario accesible con validación contextual de campos requeridos.
- Teclado inteligente con `textCapSentences` y salto fluido de campos con `actionNext` y `actionDone`.
- Selector táctil interactivo de prioridad (**Baja**, **Media**, **Alta**) con tarjetas `MaterialCardView` y respuesta táctil.
- Botón **Crear reporte** con retroalimentación visual inmediata.
- Persistencia integral del estado del formulario ante rotación y recreación de pantalla con `onSaveInstanceState`.

---

## Preparación para Evaluación / Parcial

**¿Qué pregunta del parcial ayuda a preparar esta práctica?**  
> *«¿Por qué es fundamental registrar los sensores en `onResume()` y desregistrarlos en `onPause()`, y qué consecuencias arquitectónicas y de experiencia de usuario ocurren si no se realiza este ciclo de liberación de recursos?»*
>
> **Respuesta técnica:** Los sensores de hardware mantienen hilos de escucha y conversiones analógico-digitales continuas en el chip del dispositivo. Si no se liberan en `onPause()`, la aplicación continuará consumiendo ciclos de CPU y batería en segundo plano (*battery drain*), además de retener referencias de la `Activity` en el subsistema del sistema operativo, provocando una fuga de memoria (*memory leak*). Liberarlos en `onPause()` y reconectarlos en `onResume()` garantiza el uso de recursos exclusivamente durante la interacción activa.

---

## Cómo Ejecutar y Verificar

1. Abrir el proyecto en Android Studio.
2. Ejecutar sobre emulador o dispositivo físico (Android 7.0 / API 24 o superior).
3. Desplazarse al final de la pantalla para observar el **Tablero de diagnóstico**.
4. **Para simular en emulador:**
   - **Movimiento:** Ejecutar `adb emu sensor set acceleration 7:7:15` para activar *"¡Movimiento detectado!"*.
   - **Reposo:** Ejecutar `adb emu sensor set acceleration 0:9.8:0` para retornar a *"Dispositivo estable"*.
   - **Proximidad cerca:** Ejecutar `adb emu sensor set proximity 0` para activar *"Objeto próximo detectado (Cerca)"*.
   - **Proximidad lejos:** Ejecutar `adb emu sensor set proximity 5` para retornar a *"Pantalla despejada (Lejos)"*.

---

## Datos de la Entrega

- **Estudiante:** HENRY EDMUNDO RODRÍGUEZ ÁVALOS
- **Carnet:** 2908092023
- **Sección:** 01
- **Repositorio:** [Control_Incidencias en GitHub](https://github.com/Edmundozsvoz/Control_Incidencias)
- **Rama:** `main`
