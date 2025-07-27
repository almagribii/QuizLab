## 3. Manajemen Pertanyaan (`/api/v1/questions`)

Bagian ini menyediakan *endpoint* CRUD (Create, Read, Update, Delete) untuk mengelola pertanyaan-pertanyaan kuis, serta filter tambahan dan pembaruan status.

### 3.1. Buat Pertanyaan Baru (`POST /api/v1/questions`)

Membuat pertanyaan kuis baru di sistem.

* Request Method: `POST`
* URL: `http://localhost:8090/api/v1/questions`
* Headers:
    * `Content-Type: application/json`
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, *endpoint* ini akan dilindungi dan memerlukan token otentikasi dari pengguna yang berwenang.)
* Request Body (JSON):
    ```json
    {
      "text": "string",             
      "categoryId": "uuid-string",  
      "difficultyLevel": "EASY|MEDIUM|HARD", 
      "explanation": "string"       
    }
    ```
* Respons Sukses (**201 Created**):
    ```json
    {
      "id": "uuid-dari-pertanyaan-yang-dibuat",
      "text": "teks_pertanyaan",
      "categoryId": "uuid-kategori-terkait",
      "categoryName": "Nama Kategori",
      "difficultyLevel": "EASY",
      "explanation": "penjelasan_jawaban",
      "isActive": true,
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": null
    }
    ```
* Respons Error (**400 Bad Request** / **404 Not Found**):
    * **400 Bad Request**: Jika validasi input gagal (misal: teks terlalu pendek, format `difficultyLevel` salah).
    * **404 Not Found**: Jika `categoryId` yang diberikan tidak ditemukan.

### 3.2. Dapatkan Semua Pertanyaan (`GET /api/v1/questions`)

Mengambil daftar semua pertanyaan kuis. Dapat difilter berdasarkan kategori atau tingkat kesulitan.

* Request Method: `GET`
* URL: `http://localhost:8090/api/v1/questions`
* Query Parameters (Opsional):
    * `categoryId`: UUID dari kategori untuk memfilter.
    * `difficultyLevel`: `EASY`, `MEDIUM`, atau `HARD` untuk memfilter.
    * Contoh URL dengan Filter:
        * `http://localhost:8090/api/v1/questions?categoryId=a1b2c3d4-e5f6-7890-1234-567890abcdef`
        * `http://localhost:8090/api/v1/questions?difficultyLevel=HARD`
* Headers:
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body: (Tidak ada)

* Respons Sukses (**200 OK**):
    ```json
    [
      {
        "id": "uuid-pertanyaan-1",
        "text": "Siapakah penemu bola lampu?",
        "categoryId": "uuid-kategori-sains",
        "categoryName": "Sains",
        "difficultyLevel": "EASY",
        "explanation": "Thomas Edison.",
        "isActive": true,
        "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
        "updatedAt": null
      },
      {
        "id": "uuid-pertanyaan-2",
        "text": "Ibukota Jepang adalah?",
        "categoryId": "uuid-kategori-geografi",
        "categoryName": "Geografi",
        "difficultyLevel": "MEDIUM",
        "explanation": "Tokyo.",
        "isActive": true,
        "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
        "updatedAt": "YYYY-MM-DDTHH:MM:SS.SSSZ"
      }
    ]
    ```

### 3.3. Dapatkan Pertanyaan Berdasarkan ID (`GET /api/v1/questions/{id}`)

Mengambil detail pertanyaan berdasarkan ID uniknya.

* Request Method: `GET`
* URL: `http://localhost:8090/api/v1/questions/{id}` (Ganti `{id}` dengan UUID pertanyaan yang sebenarnya)
* Headers:
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body: (Tidak ada)

* Respons Sukses (**200 OK**):
    ```json
    {
      "id": "uuid-pertanyaan-yang-dicari",
      "text": "Teks Pertanyaan",
      "categoryId": "uuid-kategori-terkait",
      "categoryName": "Nama Kategori",
      "difficultyLevel": "HARD",
      "explanation": "Penjelasan",
      "isActive": true,
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": "YYYY-MM-DDTHH:MM:SS.SSSZ"
    }
    ```

* Respons Error (**404 Not Found**):
    * Jika pertanyaan dengan ID yang diberikan tidak ditemukan.

### 3.4. Perbarui Pertanyaan (`PUT /api/v1/questions/{id}`)

Memperbarui informasi pertanyaan yang sudah ada di sistem.

* Request Method: `PUT`
* URL: `http://localhost:8090/api/v1/questions/{id}` (Ganti `{id}` dengan UUID pertanyaan yang akan diperbarui)
* Headers:
    * `Content-Type: application/json`
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body (JSON):
    ```json
    {
      "text": "string",             
      "categoryId": "uuid-string",  
      "difficultyLevel": "EASY|MEDIUM|HARD", 
      "explanation": "string"       
    }
    ```
* Respons Sukses (**200 OK**):
    ```json
    {
      "id": "uuid-pertanyaan-yang-diperbarui",
      "text": "Teks Pertanyaan yang Sudah Diubah",
      "categoryId": "uuid-kategori-baru",
      "categoryName": "Nama Kategori Baru",
      "difficultyLevel": "MEDIUM",
      "explanation": "Penjelasan baru",
      "isActive": true,
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": "YYYY-MM-DDTHH:MM:SS.SSSZ"
    }
    ```
* Respons Error (**400 Bad Request** / **404 Not Found**):
    * **404 Not Found**: Jika pertanyaan dengan ID yang diberikan tidak ditemukan, atau jika `categoryId` yang baru tidak ditemukan.
    * **400 Bad Request**: Jika validasi input gagal.

### 3.5. Hapus Pertanyaan (`DELETE /api/v1/questions/{id}`)

Menghapus pertanyaan dari sistem berdasarkan ID uniknya.

* Request Method: `DELETE`
* URL: `http://localhost:8090/api/v1/questions/{id}` (Ganti `{id}` dengan UUID pertanyaan yang akan dihapus)
* Headers:
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body: (Tidak ada)

* Respons Sukses (**204 No Content**):
    * Status `204 No Content` (Tidak ada konten yang dikembalikan, karena berhasil dihapus.)

* Respons Error (**404 Not Found**):
    * Jika pertanyaan dengan ID yang diberikan tidak ditemukan.

### 3.6. Perbarui Status Aktif Pertanyaan (`PUT /api/v1/questions/{id}/status`)

Mengubah status `isActive` (aktif/non-aktif) dari sebuah pertanyaan tanpa mengubah detail lainnya.

* Request Method: `PUT`
* URL: `http://localhost:8090/api/v1/questions/{id}/status` (Ganti `{id}` dengan UUID pertanyaan)
* Query Parameters (Wajib):
    * `isActive`: `true` atau `false` (Boolean) untuk status baru.
    * Contoh URL: `http://localhost:8090/api/v1/questions/{id}/status?isActive=false`
* Headers:
    * `Content-Type: application/json`
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan)
* Request Body: (Tidak ada)

* Respons Sukses (**200 OK**):
    ```json
    {
      "id": "uuid-pertanyaan",
      "text": "Teks Pertanyaan",
      "categoryId": "uuid-kategori-terkait",
      "categoryName": "Nama Kategori",
      "difficultyLevel": "EASY",
      "explanation": "Penjelasan",
      "isActive": false, 
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": "YYYY-MM-DDTHH:MM:SS.SSSZ"
    }
    ```

* Respons Error (**404 Not Found**):
    * Jika pertanyaan dengan ID yang diberikan tidak ditemukan.