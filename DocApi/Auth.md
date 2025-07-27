## 1. Autentikasi Pengguna (`/api/v1/auth`)

Bagian ini menyediakan *endpoint* untuk pendaftaran (registrasi) dan login pengguna.

### 1.1. Registrasi Pengguna (`POST /api/v1/auth/register`)

Membuat akun pengguna baru di QuizLab.

* **Request Method:** `POST`
* **URL:** `http://localhost:8090/api/v1/auth/register`
* **Headers:**
    * `Content-Type: application/json`
* **Request Body (JSON):**

    ```json
    {
      "username": "string",  
      "password": "string"   
    }
    ```

* **Respons Sukses (`201 Created`):**

    ```json
    {
      "id": "uuid-dari-user-yang-dibuat",
      "username": "username_yang_didaftarkan",
      "email": null,
      "displayName": null,
      "bio": null,
      "profilePictureUrl": null,
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": null
    }
    ```

* **Respons Error (`400 Bad Request`):**
    * Jika validasi input gagal (misal: username terlalu pendek, password kosong).
    * Jika `username` yang diberikan sudah terdaftar.

### 1.2. Login Pengguna (`POST /api/v1/auth/login`)

Mengotentikasi pengguna dan mengembalikan informasi pengguna. Di masa depan, akan mengembalikan JWT (JSON Web Token) untuk akses ke *endpoint* yang dilindungi.

* **Request Method:** `POST`
* **URL:** `http://localhost:8090/api/v1/auth/login`
* **Headers:**
    * `Content-Type: application/json`
* **Request Body (JSON):**

    ```json
    {
      "username": "string",
      "password": "string"
    }
    ```

* **Respons Sukses (`200 OK`):**

    ```json
    {
      "id": "uuid-user-yang-login",
      "username": "username_yang_login",
      "email": null,
      "displayName": null,
      "bio": null,
      "profilePictureUrl": null,
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": "YYYY-MM-DDTHH:MM:SS.SSSZ"
      
    }
    ```

* **Respons Error (`401 Unauthorized`):**
    * Jika `username` tidak ditemukan atau `password` salah.