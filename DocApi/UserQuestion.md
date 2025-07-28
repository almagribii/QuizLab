## 6. Manajemen Jawaban Pengguna per Pertanyaan (User Question Answers) (`/api/v1/user-answers`)

Bagian ini menyediakan *endpoint* untuk mencatat setiap jawaban spesifik yang diberikan pengguna untuk satu pertanyaan dalam satu sesi kuis, serta untuk mengambil riwayat jawaban tersebut.

### 6.1. Mencatat Jawaban Pengguna (`POST /api/v1/user-answers`)

Mencatat satu jawaban yang diberikan oleh pengguna untuk sebuah pertanyaan spesifik dalam sebuah sesi kuis.

* **Request Method:** `POST`
* **URL:** `http://localhost:8090/api/v1/user-answers`
* **Headers:**
    * `Content-Type: application/json`
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, *endpoint* ini akan dilindungi dan memerlukan token otentikasi dari pengguna yang berwenang.)
* **Request Body (JSON):**
    ```json
    {
      "quizAttemptId": "uuid-string",           
      "questionId": "uuid-string",              
      "selectedAnswerOptionId": "uuid-string",  
      "isCorrect": true,                        
      "timeTakenSeconds": 15                    
    }
    ```
* **Respons Sukses (**201 Created**):**
    ```json
    {
      "id": "uuid-dari-jawaban-yang-dicatat",
      "quizAttemptId": "uuid-sesi-kuis",
      "questionId": "uuid-pertanyaan-terkait",
      "questionText": "Teks Pertanyaan Terkait",
      "selectedAnswerOptionId": "uuid-pilihan-jawaban-dipilih",
      "selectedAnswerOptionText": "Teks Pilihan Jawaban",
      "isCorrect": true,
      "timeTakenSeconds": 15
    }
    ```
* **Respons Error (**400 Bad Request** / **404 Not Found**):**
    * **400 Bad Request**: Jika validasi input gagal (misal: `isCorrect` kosong, `timeTakenSeconds` negatif).
    * **400 Bad Request**: Jika pilihan jawaban yang dipilih (`selectedAnswerOptionId`) bukan milik pertanyaan (`questionId`) yang benar.
    * **404 Not Found**: Jika `quizAttemptId`, `questionId`, atau `selectedAnswerOptionId` yang diberikan tidak ditemukan.

### 6.2. Dapatkan Jawaban Pengguna Berdasarkan ID (`GET /api/v1/user-answers/{id}`)

Mengambil detail satu jawaban pengguna berdasarkan ID uniknya.

* **Request Method:** `GET`
* **URL:** `http://localhost:8090/api/v1/user-answers/{id}` (Ganti `{id}` dengan UUID jawaban yang sebenarnya)
* **Headers:**
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* **Request Body:** (Tidak ada)
* **Respons Sukses (**200 OK**):**
    ```json
    {
      "id": "uuid-jawaban-yang-dicari",
      "quizAttemptId": "uuid-sesi-kuis",
      "questionId": "uuid-pertanyaan-terkait",
      "questionText": "Teks Pertanyaan Terkait",
      "selectedAnswerOptionId": "uuid-pilihan-jawaban-dipilih",
      "selectedAnswerOptionText": "Teks Pilihan Jawaban",
      "isCorrect": true,
      "timeTakenSeconds": 15
    }
    ```
* **Respons Error (**404 Not Found**):**
    * Jika jawaban pengguna dengan ID yang diberikan tidak ditemukan.

### 6.3. Dapatkan Semua Jawaban untuk Sesi Kuis (`GET /api/v1/user-answers/by-attempt/{quizAttemptId}`)

Mengambil daftar semua jawaban yang diberikan pengguna dalam sebuah sesi kuis tertentu.

* **Request Method:** `GET`
* **URL:** `http://localhost:8090/api/v1/user-answers/by-attempt/{quizAttemptId}` (Ganti `{quizAttemptId}` dengan UUID sesi kuis)
* **Headers:**
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* **Request Body:** (Tidak ada)
* **Respons Sukses (**200 OK**):**
    ```json
    [
      {
        "id": "uuid-jawaban-1",
        "quizAttemptId": "uuid-sesi-kuis",
        "questionId": "uuid-pertanyaan-1",
        "questionText": "Teks Pertanyaan 1",
        "selectedAnswerOptionId": "uuid-opsi-1A",
        "selectedAnswerOptionText": "Opsi A",
        "isCorrect": true,
        "timeTakenSeconds": 10
      },
      {
        "id": "uuid-jawaban-2",
        "quizAttemptId": "uuid-sesi-kuis",
        "questionId": "uuid-pertanyaan-2",
        "questionText": "Teks Pertanyaan 2",
        "selectedAnswerOptionId": "uuid-opsi-2C",
        "selectedAnswerOptionText": "Opsi C",
        "isCorrect": false,
        "timeTakenSeconds": 12
      }
    ]
    ```
* **Respons Error (**404 Not Found**):**
    * Jika `quizAttemptId` yang diberikan tidak ditemukan.

### 6.4. Dapatkan Semua Jawaban untuk Pertanyaan Tertentu (`GET /api/v1/user-answers/by-question/{questionId}`)

Mengambil daftar semua jawaban yang pernah diberikan pengguna untuk sebuah pertanyaan spesifik (dari berbagai sesi kuis).

* **Request Method:** `GET`
* **URL:** `http://localhost:8090/api/v1/user-answers/by-question/{questionId}` (Ganti `{questionId}` dengan UUID pertanyaan)
* **Headers:**
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* **Request Body:** (Tidak ada)
* **Respons Sukses (**200 OK**):**
    ```json
    [
      {
        "id": "uuid-jawaban-dari-sesi-1",
        "quizAttemptId": "uuid-sesi-kuis-1",
        "questionId": "uuid-pertanyaan",
        "questionText": "Teks Pertanyaan",
        "selectedAnswerOptionId": "uuid-opsi-dipilih-1",
        "selectedAnswerOptionText": "Teks Opsi 1",
        "isCorrect": true,
        "timeTakenSeconds": 8
      },
      {
        "id": "uuid-jawaban-dari-sesi-2",
        "quizAttemptId": "uuid-sesi-kuis-2",
        "questionId": "uuid-pertanyaan",
        "questionText": "Teks Pertanyaan",
        "selectedAnswerOptionId": "uuid-opsi-dipilih-2",
        "selectedAnswerOptionText": "Teks Opsi 2",
        "isCorrect": false,
        "timeTakenSeconds": 10
      }
    ]
    ```
* **Respons Error (**404 Not Found**):**
    * Jika `questionId` yang diberikan tidak ditemukan.

*(Catatan: Operasi `PUT` dan `DELETE` untuk `UserQuestionAnswer` biasanya tidak umum karena ini adalah catatan historis/audit yang imutabel (tidak berubah). Jika ada kebutuhan spesifik untuk koreksi data, dapat dipertimbangkan.)*