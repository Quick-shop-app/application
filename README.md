# **Quick Shop - Android Module**

This is the **Android Module** of Quick Shop, an e-commerce platform designed for managing an online store. The Android app provides an intuitive interface for users and admins to interact with the platform, offering functionalities such as product browsing, cart management, and admin product management.

---

## **Features**

### **For Normal Users**
- **Browse Products**: View the available products with details.
- **Cart Management**:
  - Add products to the cart.
  - Remove items from the cart.
  - Finalize the cart for purchase.
- **Account Management**:
  - View account details, such as name, email, and address.
  - Logout functionality.

### **For Admins**
- **Product Management**:
  - View a list of all products.
  - Create new products with name, brand, category, price, description, and an image.
  - Edit existing products, including updating the image or other details.
  - Delete products with a confirmation dialog.
- **Admin Navigation**:
  - Seamless access to both user functionalities and admin-specific features.

### **Google Maps Integration**
- Display the user's location and the location of nearby warehouses (or store branches).
- Allow users to navigate to a store location via Google Maps.

---

## **Implemented Activities**

### **1. LoginActivity**
- **Purpose**: Authenticate users (normal and admins) using the backend API.
- **Features**:
  - Email and password input.
  - Redirect to `MainActivity` upon successful login.
  - Save user details in `SharedPreferences`.

### **2. MainActivity**
- **Purpose**: Acts as the home page for browsing products.
- **Features**:
  - Displays a scrollable list of products fetched from the backend.
  - Allows adding products to the cart (redirects to `LoginActivity` if unauthenticated).

### **3. CartActivity**
- **Purpose**: Manage the user's cart.
- **Features**:
  - View items in the cart.
  - Update quantities or remove items.
  - Finalize the cart to confirm the purchase.

### **4. AccountActivity**
- **Purpose**: Display account details and provide logout functionality.
- **Features**:
  - Displays user details such as name, email, and address.
  - Logout button clears the session and redirects to `LoginActivity`.

### **5. MapsActivity**
- **Purpose**: Display user and store locations on a map.
- **Features**:
  - Shows nearby warehouses on Google Maps with markers.
  - Enables navigation to a selected warehouse.

### **6. Admin Activities**

#### **a. ProductManagementActivity**
- **Purpose**: Manage products (CRUD operations) as an admin.
- **Features**:
  - View a list of all products with options to edit or delete.
  - Floating Action Button (FAB) to create a new product.

#### **b. AddProductActivity**
- **Purpose**: Create new products.
- **Features**:
  - Form to input product details: name, brand, category, price, description, and an image.
  - Upload image functionality using `ActivityResultContracts`.

#### **c. EditProductActivity**
- **Purpose**: Edit existing product details.
- **Features**:
  - Pre-filled form with the current product details.
  - Option to update the product image or other fields.
  - Submit updates via a backend API call.

---

## **Technology Stack**

### **Android**
- **Language**: Kotlin
- **UI Framework**: Jetpack Components (ConstraintLayout, RecyclerView)
- **Image Handling**: Glide for loading and displaying images.
- **API Integration**: Retrofit with OkHttp for REST API communication.

---

## **Permissions**

The application requests the following permissions in the `AndroidManifest.xml`:
1. **Internet Access**:
   - Required for API calls and fetching resources.
2. **Location Access**:
   - Used in `MapsActivity` to display user and store locations.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

---

## **API Integration**

### **Retrofit Setup**
- **Base URL**: `http://localhost:8080/api/`
- **Endpoints**:
  - User Authentication (`POST /auth/login`, `POST /auth/register`)
  - Product Management (`GET /products`, `POST /admin/products`, `PUT /admin/products/{id}`, `DELETE /admin/products/{id}`)
  - Cart Management (`GET /cart`, `POST /cart/add`, `POST /cart/remove`)

### **Basic Authentication**
The app uses Basic Authentication for secure communication. Credentials are encoded in Base64:
```kotlin
val credentials = "Basic " + Base64.encodeToString("$email:$password".toByteArray(), Base64.NO_WRAP)
```

### **Postman Collection**
For detailed API usage and examples, refer to the [Quick Shop Postman Collection](https://documenter.getpostman.com/view/17235107/2sAYJ3ELxC).

### **Models**
- **Product**:
  ```kotlin
  data class Product(
      val id: Long,
      val name: String,
      val brand: String,
      val category: String,
      val price: Double,
      val description: String,
      val image: String
  )
  ```

- **User**:
  ```kotlin
  data class User(
      val id: Long,
      val firstName: String,
      val lastName: String,
      val email: String,
      val phone: String,
      val address: String
  )
  ```

- **Store**:
  ```kotlin
  data class Store(
      val name: String,
      val address: String,
      val coordinates: LatLng
  )
  ```

- **CartItem**:
  ```kotlin
  data class CartItem(
      val id: Long,
      val product: Product,
      var quantity: Long,
      var totalPrice: Double
  )
  ```
---

## **Key Dependencies**

These dependencies are used in the Quick Shop Android module for various features such as Compose UI, Retrofit for API integration, Glide for image handling, and Google Maps for location-based functionality.

#### **Gradle Dependencies**
```gradle
dependencies {
    // AndroidX Core and Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Material Design
    implementation(libs.androidx.material3)
    implementation(libs.material)

    // RecyclerView and SwipeRefreshLayout
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.swiperefreshlayout)

    // ConstraintLayout
    implementation(libs.androidx.constraintlayout)

    // Google Play Services
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Retrofit for API calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // OkHttp for network operations
    implementation(libs.okhttp3.okhttp)

    // Glide for image handling
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Android Security
    implementation(libs.androidx.security.crypto)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging Tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
```

#### **Plugin Configurations**
```gradle
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}
```

---

## **Build Settings**
1. **Min SDK**: 29  
2. **Target SDK**: 34  
3. **Compile SDK**: 35  
4. **Java Compatibility**: Java 11  

---

## **How to Run**

### **Backend Setup**
Follow the instructions in the [Quick Shop Backend Repository](https://github.com/Quick-shop-app/backend) to set up and run the backend server.

### **Prerequisites**
- Android Studio installed on your machine.
- A running backend server at `http://localhost:8080/` or a configured live backend.

### **Steps to Run**
1. Clone the repository:
   ```bash
   git clone https://github.com/username/quickshop-android.git
   cd quickshop-android
   ```

2. Open the project in Android Studio.

3. Replace the `API_KEY` in `AndroidManifest.xml` with a valid Google Maps API key.

4. Build and run the application on an emulator or physical device:
   - Ensure the backend is accessible from the device (use a live backend URL if needed).

---
