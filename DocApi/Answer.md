## 4. Manajemen Pilihan Jawaban (`/api/v1/answer-options`)

Bagian ini menyediakan *endpoint* CRUD (Create, Read, Update, Delete) untuk mengelola pilihan-pilihan jawaban yang terkait dengan pertanyaan kuis.

### 4.1. Buat Pilihan Jawaban Baru (`POST /api/v1/answer-options`)

Membuat pilihan jawaban baru untuk sebuah pertanyaan kuis yang sudah ada.

* Request Method: `POST`
* URL: `http://localhost:8090/api/v1/answer-options`
* Headers:
    * `Content-Type: application/json`
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, *endpoint* ini akan dilindungi dan memerlukan token otentikasi dari pengguna yang berwenang.)
* Request Body (JSON):
    ```json
    {
      "questionId": "uuid-string",  
      "text": "string",            
      "isCorrect": true       
    }
    ```
* Respons Sukses (**201 Created**):
    ```json
    {
      "id": "uuid-dari-pilihan-jawaban-yang-dibuat",
      "text": "teks_pilihan_jawaban",
      "questionId": "uuid-pertanyaan-terkait",
      "questionText": "Teks Pertanyaan Terkait",
      "isCorrect": true,
      "displayOrder": 1
    }
    ```
* Respons Error (**400 Bad Request** / **404 Not Found**):
    * **400 Bad Request**: Jika validasi input gagal (misal: teks pilihan jawaban kosong, `isCorrect` tidak valid).
    * **404 Not Found**: Jika `questionId` yang diberikan tidak ditemukan.

### 4.2. Dapatkan Semua Pilihan Jawaban (`GET /api/v1/answer-options`)

Mengambil daftar semua pilihan jawaban. Dapat difilter berdasarkan ID pertanyaan.

* Request Method: `GET`
* URL: `http://localhost:8090/api/v1/answer-options`
* Query Parameters (Opsional):
    * `questionId`: UUID dari pertanyaan untuk memfilter pilihan jawaban.
    * Contoh URL dengan Filter:
        * `http://localhost:8090/api/v1/answer-options?questionId=a1b2c3d4-e5f6-7890-1234-567890abcdef`
* Headers:
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body: (Tidak ada)

* Respons Sukses (**200 OK**):
    ```json
    [
      {
        "id": "uuid-pilihan-jawaban-1",
        "text": "Pilihan A",
        "questionId": "uuid-pertanyaan-terkait",
        "questionText": "Teks Pertanyaan",
        "isCorrect": false,
        "displayOrder": 1
      },
      {
        "id": "uuid-pilihan-jawaban-2",
        "text": "Pilihan B (Benar)",
        "questionId": "uuid-pertanyaan-terkait",
        "questionText": "Teks Pertanyaan",
        "isCorrect": true,
        "displayOrder": 2
      }
    ]
    ```

* Respons Error (**404 Not Found**):
    * Jika `questionId` yang diberikan di *query parameter* tidak ditemukan.

### 4.3. Dapatkan Pilihan Jawaban Berdasarkan ID (`GET /api/v1/answer-options/{id}`)

Mengambil detail pilihan jawaban berdasarkan ID uniknya.

* Request Method: `GET`
* URL: `http://localhost:8090/api/v1/answer-options/{id}` (Ganti `{id}` dengan UUID pilihan jawaban yang sebenarnya)
* Headers:
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body: (Tidak ada)

* Respons Sukses (**200 OK**):
    ```json
    {
      "id": "uuid-pilihan-jawaban-yang-dicari",
      "text": "Teks Pilihan Jawaban",
      "questionId": "uuid-pertanyaan-terkait",
      "questionText": "Teks Pertanyaan",
      "isCorrect": false,
      "displayOrder": 3
    }
    ```

* Respons Error (**404 Not Found**):
    * Jika pilihan jawaban dengan ID yang diberikan tidak ditemukan.

### 4.4. Perbarui Pilihan Jawaban (`PUT /api/v1/answer-options/{id}`)

Memperbarui informasi pilihan jawaban yang sudah ada di sistem.

* Request Method: `PUT`
* URL: `http://localhost:8090/api/v1/answer-options/{id}` (Ganti `{id}` dengan UUID pilihan jawaban yang akan diperbarui)
* Headers:
    * `Content-Type: application/json`
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body (JSON):
    ```json
    {
      "questionId": "uuid-string",  
      "text": "string",             
      "isCorrect": true,            
      "displayOrder": 1             
    }
    ```
* Respons Sukses (**200 OK**):
    ```json
    {
      "id": "uuid-pilihan-jawaban-yang-diperbarui",
      "text": "Teks Pilihan Jawaban Baru",
      "questionId": "uuid-pertanyaan-terkait",
      "questionText": "Teks Pertanyaan",
      "isCorrect": true,
      "displayOrder": 1
    }
    ```
* Respons Error (**400 Bad Request** / **404 Not Found**):
    * **404 Not Found**: Jika pilihan jawaban dengan ID yang diberikan tidak ditemukan, atau jika `questionId` yang baru tidak ditemukan.
    * **400 Bad Request**: Jika validasi input gagal.

### 4.5. Hapus Pilihan Jawaban (`DELETE /api/v1/answer-options/{id}`)

Menghapus pilihan jawaban dari sistem berdasarkan ID uniknya.

* Request Method: `DELETE`
* URL: `http://localhost:8090/api/v1/answer-options/{id}` (Ganti `{id}` dengan UUID pilihan jawaban yang akan dihapus)
* Headers:
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body: (Tidak ada)

* Respons Sukses (**204 No Content**):
    * Status `204 No Content` (Tidak ada konten yang dikembalikan, karena berhasil dihapus.)

* Respons Error (**404 Not Found**):
    * Jika pilihan jawaban dengan ID yang diberikan tidak ditemukan.