# Registro de Incidencias

Aplicación Android académica que presenta un incidente de ejemplo y permite registrar su estado desde una pantalla inicial. El proyecto sirve como base para continuar incorporando interfaz, eventos, ciclo de vida y persistencia durante el desarrollo de la asignatura.

## Estado actual

La versión actual incluye:

- Pantalla inicial con el título de la aplicación.
- Tarjeta con un incidente de ejemplo por falla de conexión.
- Estado inicial pendiente.
- Botón para registrar la incidencia.
- Cambio visual del estado a registrado y mensaje de confirmación.
- Compilación `debug` verificada correctamente.

Todavía falta implementar:

- Formulario para registrar incidencias reales.
- Validación de los datos ingresados.
- Almacenamiento persistente de incidencias.
- Consulta o historial de registros.
- Manejo ampliado del ciclo de vida de la aplicación.

## Herramientas

- Android Studio
- Kotlin
- Android SDK y AppCompat
- XML para la interfaz de la versión actual
- Git y GitHub
- Jetpack Compose como tecnología contemplada para la evolución de la interfaz; aún no está integrado en este prototipo

## Cómo abrir el proyecto

1. Clona o descarga este repositorio.
2. Abre Android Studio.
3. Selecciona **Open** y elige la carpeta raíz del proyecto.
4. Espera a que Gradle sincronice las dependencias.
5. Selecciona un emulador o dispositivo Android con API 24 o posterior.
6. Ejecuta la configuración de la aplicación.

## Compilación

Desde una terminal ubicada en la raíz del proyecto:

```powershell
.\gradlew.bat assembleDebug
```

## Autor

Edmundozsvoz — cuenta académica de GitHub.
