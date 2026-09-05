# Registro de Incidencias

Aplicación Android académica que permite capturar el título y la descripción de una incidencia y preparar un reporte con retroalimentación inmediata. Este avance corresponde a la actividad evaluada de la semana 6 de Técnicas de Producción Industrial de Software I.

## Funcionalidades implementadas

- Pantalla con título, instrucción y formulario accesible.
- Campo para el título de la incidencia.
- Campo multilínea para una descripción breve.
- Estado local que se actualiza con lo que escribe el usuario.
- Validación de campos obligatorios.
- Botón **Crear reporte** y acción equivalente desde el teclado.
- Retroalimentación visible con el título y la descripción capturados.
- Conservación del formulario y del reporte preparado ante la recreación de la actividad.
- Diseño adaptable mediante desplazamiento vertical para pantallas pequeñas.

> Este prototipo prepara el reporte en memoria; todavía no guarda información en una base de datos.

## Decisiones técnicas

El proyecto utiliza Kotlin, AppCompat, Material Components y layouts XML. `MainActivity` mantiene el título, la descripción y el estado del reporte en variables locales, observa los cambios de ambos campos y vuelve a renderizar el mensaje de estado. `onSaveInstanceState` conserva esa información cuando Android recrea la actividad, por ejemplo después de un cambio de configuración.

La principal dificultad fue evolucionar una pantalla estática de demostración a un formulario reactivo sin incorporar persistencia antes de la etapa indicada. Se resolvió separando la captura, la validación y la presentación de la retroalimentación, y dejando explícito que el reporte solo se prepara en memoria.

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

Edmundozsvoz — cuenta académica de GitHub.
