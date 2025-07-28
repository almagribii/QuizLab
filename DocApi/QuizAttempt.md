## 5. Manajemen Percobaan Kuis (Quiz Attempts) (`/api/v1/quiz-attempts`)

Bagian ini menyediakan *endpoint* untuk **memulai** sesi kuis, dan mengambil riwayat percobaan kuis. Pencatatan jawaban per pertanyaan dan perhitungan skor akan dilakukan terpisah (dengan modul `USER_Youtube`), yang kemudian akan **memperbarui** data `QuizAttempt`.

### 5.1. Memulai Sesi Kuis Baru (`POST /api/v1/quiz-attempts`)

Memulai sebuah sesi kuis baru untuk pengguna. Skor dan total pertanyaan akan dihitung dan diperbarui setelah kuis selesai.

* **Request Method:** `POST`
* **URL:** `http://localhost:8090/api/v1/quiz-attempts`
* **Headers:**
  * `Content-Type: application/json`
  * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, *endpoint* ini akan dilindungi dan memerlukan token otentikasi dari pengguna yang berwenang.)
* **Request Body (JSON):**
    ```json
    {
      "userId": "uuid-string",            
      "categoryId": "uuid-string",        
      "difficultyLevel": "EASY|MEDIUM|HARD", 
      "startTime": "YYYY-MM-DDTHH:MM:SS.SSSZ" 
    }
    ```
* **Respons Sukses (**201 Created**):**
    ```json
    {
      "id": "uuid-dari-percobaan-kuis-yang-dibuat",
      "userId": "uuid-user-yang-bermain",
      "username": "username_user_yang_bermain",
      "categoryId": "uuid-kategori-kuis",
      "categoryName": "Nama Kategori Kuis",
      "score": 0,                           
      "totalQuestions": 0,                  
      "difficultyLevel": "MEDIUM",
      "startTime": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "endTime": null                      
    }
    ```
* **Respons Error (**400 Bad Request** / **404 Not Found**):**
  * **400 Bad Request**: Jika validasi input gagal (misal: format waktu salah, ID kosong).
  * **404 Not Found**: Jika `userId` atau `categoryId` yang diberikan tidak ditemukan.

### 5.2. Dapatkan Semua Percobaan Kuis (`GET /api/v1/quiz-attempts`)

Mengambil daftar semua percobaan kuis yang tercatat. Dapat difilter berdasarkan ID pengguna atau ID kategori.

* **Request Method:** `GET`
* **URL:** `http://localhost:8090/api/v1/quiz-attempts`
* **Query Parameters (Opsional):**
  * `userId`: UUID dari pengguna untuk memfilter percobaan kuis.
  * `categoryId`: UUID dari kategori untuk memfilter percobaan kuis.
  * **Contoh URL dengan Filter:**
    * `http://localhost:8090/api/v1/quiz-attempts?userId=uuid-user-1`
    * `http://localhost:8090/api/v1/quiz-attempts?categoryId=uuid-cat-sains`
    * `http://localhost:8090/api/v1/quiz-attempts?userId=uuid-user-1&categoryId=uuid-cat-sains`
* **Headers:**
  * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* **Request Body:** (Tidak ada)
* **Respons Sukses (**200 OK**):**
    ```json
    [
      {
        "id": "uuid-percobaan-1",
        "userId": "uuid-user-1",
        "username": "user_pertama",
        "categoryId": "uuid-cat-sains",
        "categoryName": "Sains",
        "score": 90,
        "totalQuestions": 10,
        "difficultyLevel": "HARD",
        "startTime": "YYYY-MM-DDTHH:MM:SS.SSSZ",
        "endTime": "YYYY-MM-DDTHH:MM:SS.SSSZ" 
      },
      {
        "id": "uuid-percobaan-2",
        "userId": "uuid-user-2",
        "username": "user_kedua",
        "categoryId": "uuid-cat-sejarah",
        "categoryName": "Sejarah",
        "score": 75,
        "totalQuestions": 10,
        "difficultyLevel": "MEDIUM",
        "startTime": "YYYY-MM-DDTHH:MM:SS.SSSZ",
        "endTime": "YYYY-MM-DDTHH:MM:SS.SSSZ"
      }
    ]
    ```
* **Respons Error (**404 Not Found**):**
  * Jika `userId` atau `categoryId` yang digunakan untuk filter tidak ditemukan.

### 5.3. Dapatkan Percobaan Kuis Berdasarkan ID (`GET /api/v1/quiz-attempts/{id}`)

Mengambil detail percobaan kuis berdasarkan ID uniknya.

* **Request Method:** `GET`
* **URL:** `http://localhost:8090/api/v1/quiz-attempts/{id}` (Ganti `{id}` dengan UUID percobaan kuis yang sebenarnya)
* **Headers:**
  * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* **Request Body:** (Tidak ada)
* **Respons Sukses (**200 OK**):**
    ```json
    {
      "id": "uuid-percobaan-kuis-yang-dicari",
      "userId": "uuid-user-terkait",
      "username": "username_terkait",
      "categoryId": "uuid-kategori-terkait",
      "categoryName": "Nama Kategori",
      "score": 85,
      "totalQuestions": 10,
      "difficultyLevel": "EASY",
      "startTime": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "endTime": "YYYY-MM-DDTHH:MM:SS.SSSZ"
    }
    ```
* **Respons Error (**404 Not Found**):**
  * Jika percobaan kuis dengan ID yang diberikan tidak ditemukan.

*(Catatan: Operasi `PUT` dan `DELETE` untuk `QuizAttempt` biasanya tidak umum karena ini adalah catatan historis yang idealnya imutabel (tidak berubah). Operasi untuk memperbarui skor dan `endTime` akan menjadi *endpoint* terpisah yang khusus untuk "menyelesaikan kuis".)*