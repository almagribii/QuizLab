# Fitur Autentikasi Pengguna (User Authentication)

Fitur autentikasi pengguna pada QuizLab API memungkinkan pengguna untuk mendaftar akun baru dan login ke sistem. Ini adalah fondasi untuk fitur-fitur personalisasi seperti pelacakan skor dan riwayat kuis.

## Komponen Utama

Fitur ini diimplementasikan dengan mengikuti arsitektur berlapis (Model-Repository-Service-DTO-Controller) dan menggunakan Spring Security untuk keamanan password.

### 1. Entitas (Model): `User`

* **File:** `src/main/java/com/quizlab/model/User.java`
* **Peran:** Merepresentasikan data pengguna di database (`users` table).
* **Atribut Kunci:**
    * `id` (UUID, Primary Key)
    * `username` (String, unik, tidak boleh kosong)
    * `passwordHash` (String, tersimpan sebagai hash bcrypt, tidak boleh kosong)
    * `email` (String, opsional, unik)
    * `displayName`, `bio`, `profilePictureUrl` (untuk profil pengguna)
    * `createdAt`, `updatedAt` (Timestamp otomatis)

### 2. Repositori: `UserRepository`

* **File:** `src/main/java/com/quizlab/repository/UserRepository.java`
* **Peran:** Antarmuka untuk berinteraksi langsung dengan tabel `users` di database. Menyediakan metode CRUD dasar secara otomatis melalui `JpaRepository`.
* **Metode Kustom Kunci:**
    * `findByUsername(String username)`: Mencari pengguna berdasarkan username.
    * `findByEmail(String email)`: Mencari pengguna berdasarkan email.

### 3. DTOs (Data Transfer Objects)

DTO digunakan untuk mengontrol format data yang masuk dan keluar dari API, serta untuk validasi input.

* **`UserRegisterRequest.java`**
    * **File:** `src/main/java/com/quizlab/dto/UserRegisterRequest.java`
    * **Peran:** Menerima data input dari klien saat **registrasi**.
    * **Atribut:** `username`, `password`.
    * **Validasi:** Menggunakan `@NotBlank` dan `@Size` (minimal 3-20 karakter untuk username, minimal 6 karakter untuk password).

* **`UserLoginRequest.java`**
    * **File:** `src/main/java/com/quizlab/dto/UserLoginRequest.java`
    * **Peran:** Menerima data input dari klien saat **login**.
    * **Atribut:** `username`, `password`.
    * **Validasi:** Menggunakan `@NotBlank`.

* **`UserResponse.java`**
    * **File:** `src/main/java/com/quizlab/dto/UserResponse.java`
    * **Peran:** Mengirim data pengguna sebagai respons ke klien.
    * **Atribut:** `id`, `username`, `email`, `displayName`, `bio`, `profilePictureUrl`, `createdAt`, `updatedAt`.
    * **Penting:** **TIDAK menyertakan `passwordHash`** untuk keamanan. Menggunakan `@Builder` untuk pembuatan objek yang bersih.

### 4. Service: `UserService`

* **File:** `src/main/java/com/quizlab/service/UserService.java`
* **Peran:** Menampung logika bisnis inti untuk manajemen pengguna. Berinteraksi dengan `UserRepository`.
* **Dependensi Kunci:** `UserRepository` dan `BCryptPasswordEncoder` (untuk hashing password).
* **Metode Kunci:**
    * `registerUser(UserRegisterRequest request)`:
        * Memeriksa keunikan username (dan email jika ditambahkan).
        * Melakukan hashing password menggunakan `BCryptPasswordEncoder.encode()`.
        * Menyimpan `User` baru ke database.
        * Mengembalikan `UserResponse`.
    * `authenticateUser(UserLoginRequest request)`:
        * Mencari pengguna berdasarkan username.
        * Membandingkan password yang diberikan dengan hash yang tersimpan menggunakan `BCryptPasswordEncoder.matches()`.
        * Mengembalikan `Optional<UserResponse>` jika otentikasi berhasil, `Optional.empty()` jika gagal.

### 5. Konfigurasi Keamanan: `SecurityConfig`

* **File:** `src/main/java/com/quizlab/config/SecurityConfig.java`
* **Peran:** Mengatur Spring Security, khususnya untuk otentikasi password dan otorisasi *endpoint*.
* **Komponen Kunci:**
    * `BCryptPasswordEncoder` (`@Bean`): Disediakan sebagai *bean* agar bisa di-*inject* ke `UserService`.
    * `SecurityFilterChain` (`@Bean`): Mengonfigurasi aturan HTTP Security:
        * `csrf(AbstractHttpConfigurer::disable)`: Menonaktifkan CSRF untuk REST API *stateless*.
        * `requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()`: **Mengizinkan akses tanpa otentikasi** untuk *endpoint* registrasi dan login.
        * `requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()`: Mengizinkan akses ke Swagger UI.
        * `requestMatchers("/api/health/**").permitAll()`: Mengizinkan akses ke *endpoint* health check.
        * `anyRequest().authenticated()`: **Semua *endpoint* lainnya memerlukan otentikasi.**

### 6. Controller: `AuthController`

* **File:** `src/main/java/com/quizlab/controller/AuthController.java`
* **Peran:** Menyediakan *endpoint* API RESTful untuk operasi otentikasi.
* **Base Path:** `@RequestMapping("/api/v1/auth")`.
* **Endpoint Kunci:**
    * `POST /api/v1/auth/register`:
        * Menerima `UserRegisterRequest` (dengan validasi `@Valid`).
        * Mengembalikan `201 Created` dengan `UserResponse` jika berhasil.
        * Mengembalikan `400 Bad Request` jika ada masalah (validasi gagal, username sudah terdaftar).
    * `POST /api/v1/auth/login`:
        * Menerima `UserLoginRequest` (dengan validasi `@Valid`).
        * Mengembalikan `200 OK` dengan `UserResponse` jika otentikasi berhasil.
        * Mengembalikan `401 Unauthorized` jika otentikasi gagal (username tidak ditemukan atau password salah).

## Cara Menguji (Postman)

Setelah aplikasi berjalan dengan Docker Compose (`docker compose up -d --build`):

* **Register User:**
    * `POST` `http://localhost:8090/api/v1/auth/register`
    * Body (JSON): `{ "username": "testuser", "password": "Password123!" }`
    * Expected: `201 Created`
* **Login User:**
    * `POST` `http://localhost:8090/api/v1/auth/login`
    * Body (JSON): `{ "username": "testuser", "password": "Password123!" }`
    * Expected: `200 OK`

---