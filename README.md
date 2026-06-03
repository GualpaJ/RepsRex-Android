# 💪 RepsRex App – Android Kotlin

**Descripción:**  
Aplicación nativa de Android desarrollada en Kotlin para la gestión de rutinas de entrenamiento y ejercicios (Fitness App). Permite crear rutinas personalizadas y añadir ejercicios con series y repeticiones.

---

## 📌 Funcionalidades

### 🔹 v1.0
- Gestión de ejercicios (catálogo predefinido)
- Creación de rutinas de entrenamiento personalizadas
- Añadir ejercicios a rutinas con series y repeticiones
- Visualización de detalle de rutina
- Persistencia local (Room Database)
- Diseño moderno con **Material Design**

### 🔹 v1.1
- ✏️ Edición de rutinas y ejercicios
- 📊 Registro de progreso (fecha, peso, series realizadas)
- 📈 Historial de entrenamientos

---

## 🛠 Tecnologías utilizadas

- **Kotlin**
- **Android Studio**
- **RecyclerView** + Adaptadores personalizados
- **Room Database** (Persistencia local)
- **LiveData / ViewModel** (Arquitectura MVVM)
- **Material Design Components**
- **ConstraintLayout**
- **Coroutines** (para operaciones asíncronas)
- **Navigation Component**

---

## 📷 Capturas de pantalla

### 🟢 v1.0 – Lista de rutinas y ejercicios
<p align="center">
  <img src="screenshots/routines_list.png" width="250">
  <img src="screenshots/add_exercise.png" width="250">
</p>

### 🔵 v1.1 – Detalle de rutina y progreso
<p align="center">
  <img src="screenshots/routine_detail.png" width="250">
  <img src="screenshots/progress.png" width="250">
</p>

---

## 📌 Estado del proyecto

| Versión | Estado | Funcionalidades |
|---------|--------|-----------------|
| v1.0 | ✅ Completado | Gestión de ejercicios + Creación de rutinas + Persistencia |
| v1.1 | ✅ Completado | Edición + Registro de progreso + Historial |

---

## 📝 Lo que aprendí

- Diseño de esquema de base de datos relacional (Ejercicios - Rutinas - EjerciciosRutina)
- Implementación de **Room Database** con relaciones (Foreign Keys)
- Uso de **LiveData** y **ViewModel** para arquitectura MVVM
- Gestión de estado complejo (rutinas + ejercicios + series)
- Creación de interfaces de usuario para formularios dinámicos
- Navegación entre fragments con **Navigation Component**
- Persistencia de datos locales con Coroutines
- Manejo de listas anidadas (RecyclerView dentro de otro RecyclerView)

---

## 🚀 Cómo ejecutar

```bash
git clone https://github.com/GualpaJ/RepsRex-Android.git
