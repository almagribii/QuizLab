## 2. Manajemen Kategori (`/api/v1/categories`)

Bagian ini menyediakan *endpoint* CRUD (Create, Read, Update, Delete) untuk mengelola kategori-kategori kuis.

### 2.1. Buat Kategori Baru (`POST /api/v1/categories`)

Membuat kategori baru di sistem QuizLab.

* **Request Method:** `POST`
* **URL:** `http://localhost:8090/api/v1/categories`
* **Headers:**
    * `Content-Type: application/json`
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, setelah JWT diimplementasikan, *endpoint* ini akan dilindungi dan memerlukan token otentikasi dari pengguna yang berwenang, misalnya admin.)
* **Request Body (JSON):**

    ```json
    {
      "name": "string",        
      "description": "string"  
    }
    ```

* **Respons Sukses (`201 Created`):**

    ```json
    {
      "id": "uuid-dari-kategori-yang-dibuat",
      "name": "nama_kategori",
      "description": "deskripsi_kategori",
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": null
    }
    ```

* **Respons Error (`400 Bad Request`):**
    * Jika validasi input gagal (misal: nama terlalu pendek, nama kosong).
    * Jika nama kategori yang diberikan sudah ada (duplikat).

### 2.2. Dapatkan Semua Kategori (`GET /api/v1/categories`)

Mengambil daftar semua kategori yang tersedia di sistem QuizLab.

* **Request Method:** `GET`
* **URL:** `http://localhost:8090/api/v1/categories`
* **Headers:**
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, *endpoint* ini mungkin akan dilindungi dan memerlukan token otentikasi.)
* **Request Body:** (Tidak ada)

* **Respons Sukses (`200 OK`):**

    ```json
    [
      {
        "id": "uuid-kategori-1",
        "name": "Sains",
        "description": "Pertanyaan seputar ilmu pengetahuan alam.",
        "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
        "updatedAt": "YYYY-MM-DDTHH:MM:SS.SSSZ"
      },
      {
        "id": "uuid-kategori-2",
        "name": "Sejarah",
        "description": "Kuis tentang peristiwa dan tokoh sejarah.",
        "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
        "updatedAt": null
      }
    ]
    ```

### 2.3. Dapatkan Kategori Berdasarkan ID (`GET /api/v1/categories/{id}`)

Mengambil detail kategori berdasarkan ID uniknya.

* **Request Method:** `GET`
* **URL:** `http://localhost:8090/api/v1/categories/{id}` (Ganti `{id}` dengan UUID kategori yang sebenarnya)
* **Headers:**
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, *endpoint* ini mungkin akan dilindungi dan memerlukan token otentikasi.)
* **Request Body:** (Tidak ada)

* **Respons Sukses (`200 OK`):**

    ```json
    {
      "id": "uuid-kategori-yang-dicari",
      "name": "Nama Kategori",
      "description": "Deskripsi Kategori",
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": "YYYY-MM-DDTHH:MM:SS.SSSZ"
    }
    ```

* **Respons Error (`404 Not Found`):**
    * Jika kategori dengan ID yang diberikan tidak ditemukan.

### 2.4. Perbarui Kategori (`PUT /api/v1/categories/{id}`)

Memperbarui informasi kategori yang sudah ada di sistem.

* **Request Method:** `PUT`
* **URL:** `http://localhost:8090/api/v1/categories/{id}` (Ganti `{id}` dengan UUID kategori yang akan diperbarui)
* **Headers:**
    * `Content-Type: application/json`
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, *endpoint* ini akan dilindungi dan memerlukan token otentikasi dari pengguna yang berwenang.)
* **Request Body (JSON):**

    ```json
    {
      "name": "string",        
      "description": "string"  
    }
    ```

* **Respons Sukses (`200 OK`):**

    ```json
    {
      "id": "uuid-kategori-yang-diperbarui",
      "name": "Nama Kategori Baru",
      "description": "Deskripsi Kategori Baru",
      "createdAt": "YYYY-MM-DDTHH:MM:SS.SSSZ",
      "updatedAt": "YYYY-MM-DDTHH:MM:SS.SSSZ"
    }
    ```

* **Respons Error (`400 Bad Request` / `404 Not Found`):**
    * `404 Not Found`: Jika kategori dengan ID yang diberikan tidak ditemukan.
    * `400 Bad Request`: Jika validasi input gagal atau nama kategori baru sudah ada di kategori lain.

### 2.5. Hapus Kategori (`DELETE /api/v1/categories/{id}`)

Menghapus kategori dari sistem berdasarkan ID uniknya.

* **Request Method:** `DELETE`
* **URL:** `http://localhost:8090/api/v1/categories/{id}` (Ganti `{id}` dengan UUID kategori yang akan dihapus)
* **Headers:**
    * `Authorization: Bearer [JWT_TOKEN]` (Di masa depan, *endpoint* ini akan dilindungi dan memerlukan token otentikasi dari pengguna yang berwenang.)
* **Request Body:** (Tidak ada)

* **Respons Sukses (`204 No Content`):**
    * Status `204 No Content` (Tidak ada konten yang dikembalikan, karena berhasil dihapus.)

* **Respons Error (`404 Not Found`):**
    * Jika kategori dengan ID yang diberikan tidak ditemukan.