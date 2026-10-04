# Registro de Incidencias

Aplicación Android académica que permite capturar el título, la descripción y la prioridad de una incidencia y preparar un reporte con retroalimentación inmediata. Este avance corresponde a la actividad evaluada de la **Semana 10: Teclado y pantalla táctil en RegistroIncidencias** de Técnicas de Producción Industrial de Software I (Ciclo 02-2026).

## Funcionalidades implementadas

- Pantalla con título, instrucción y formulario accesible.
- Campo de título con teclado contextual configurado (`textCapSentences` y acción IME `actionNext`).
- Campo multilínea para descripción breve (`textMultiLine` y acción IME `actionDone`).
- Interacción táctil de alto nivel mediante tarjetas clicables (`MaterialCardView`) para selección de prioridad (**Baja**, **Media**, **Alta**).
- Estado local reactivo que captura texto y prioridad seleccionada.
- Validación de campos obligatorios con errores contextuales.
- Botón principal **Crear reporte** accesible por toque y desde el teclado.
- Retroalimentación visible inmediata con título, descripción y prioridad capturados.
- Conservación íntegra de campos y prioridad ante la recreación de la actividad (`onSaveInstanceState`).
- Diseño adaptable mediante desplazamiento vertical para diversas pantallas.

> Este prototipo prepara el reporte en memoria; todavía no guarda información en una base de datos.

## Decisiones técnicas y nivel de abstracción

El proyecto utiliza Kotlin, AppCompat, Material Components y layouts XML.
- **Teclado contextual:** Se configuró `inputType="textCapSentences"` para garantizar mayúsculas automáticas en cada oración y `imeOptions="actionNext"` en el título para transferir el foco al campo de descripción sin requerir toques adicionales.
- **Interacción táctil de alto nivel:** Se seleccionaron componentes `MaterialCardView` táctiles en lugar de eventos táctiles de bajo nivel (`onTouchListener` o gestos personalizados). Esta abstracción nativa provee automáticamente elevación, esquinas redondeadas, accesibilidad y efecto *ripple* (onda al tacto), conectando directamente el evento `setOnClickListener` con el estado `priorityState`.
- **Manejo de estado:** `MainActivity` mantiene las variables reactivas en memoria y persiste tanto los textos como la prioridad seleccionada en el `Bundle` del sistema con `onSaveInstanceState()`.
- **Dificultad resuelta:** La principal dificultad fue coordinar la acción IME entre campos de entrada con la selección táctil de prioridad sin perder la consistencia visual ante rotaciones de pantalla. Se resolvió centralizando la función `updatePriorityUI()` y ligando el listener de acción IME del teclado virtual.

## Cómo ejecutar

1. Clona o descarga este repositorio.
2. Abre la carpeta raíz en Android Studio.
3. Espera a que Gradle sincronice las dependencias.
4. Selecciona un emulador o dispositivo con Android API 24 o posterior.
5. Ejecuta la configuración `app`.
6. Escribe un título y una descripción, y presiona **Crear reporte**.

También puedes compilar desde PowerShell, usando el JDK incluido con Android Studio:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug
```

## Evidencia esperada para Blackboard

- Enlace público del repositorio: <https://github.com/Edmundozsvoz/Control_Incidencias>
- Captura del repositorio con el commit de la semana.
- Captura de Android Studio con el proyecto abierto.
- Captura de la app ejecutándose con ambos campos completos y la retroalimentación visible.
- Breve explicación de los cambios, el manejo de estado y la dificultad resuelta (incluida arriba).

## Próximos pasos

- Persistir incidencias en una base de datos local.
- Mostrar un historial de reportes.
- Incorporar edición y eliminación de incidencias.

## Autor
- **Estudiante:** Henry Edmundo Rodríguez Ávalos
- **Carnet:** 2908092023
- **Sección:** 01
- **GitHub:** [Edmundozsvoz](https://github.com/Edmundozsvoz)

