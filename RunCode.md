# Cara Menjalankan QuizLab API (Untuk Pengguna / Pengembang)

Jika Anda hanya ingin menjalankan QuizLab API di lingkungan lokal Anda (tanpa melakukan modifikasi kode), ikuti langkah-langkah mudah ini.

## Prasyarat

Pastikan Anda telah menginstal perangkat lunak berikut di komputer Anda:

* **Git**: Untuk mengkloning repositori proyek.
* **Docker Desktop**: Ini mencakup Docker Engine dan Docker Compose, yang akan kita gunakan untuk menjalankan aplikasi dan database.

## Langkah-langkah Menjalankan Aplikasi

1.  **Kloning Repositori Proyek:**
    Buka terminal atau PowerShell Anda, lalu kloning repositori QuizLab API:
    ```bash
    git clone [https://github.com/your-username/quizlab-api.git](https://github.com/your-username/quizlab-api.git)
    cd quizlab-api
    ```
    *(Ganti `https://github.com/your-username/quizlab-api.git` dengan URL repositori Git Anda yang sebenarnya.)*

2.  **Konfigurasi Database (Penting!):**
    Anda **harus** mengatur *password* untuk database MySQL agar koneksi dari aplikasi berhasil.
    * Buka file `docker-compose.yml` di *root directory* proyek.
    * Cari bagian `environment` di bawah *service* `quizlab-app` dan `quizlab-db`.
    * **Ganti *placeholder* `your_secure_password` dan `your_root_password` dengan *password* nyata yang kuat dan Anda inginkan.** Pastikan `SPRING_DATASOURCE_PASSWORD` di `quizlab-app` **SAMA PERSIS** dengan `MYSQL_PASSWORD` di `quizlab-db`.

    ```yaml
    # Contoh bagian relevan di docker-compose.yml
    services:
      quizlab-app:
        environment:
          # ...
          SPRING_DATASOURCE_PASSWORD: MyStrongPass123! # <--- GANTI INI DENGAN PASSWORD ANDA SENDIRI
          # ...

      quizlab-db:
        environment:
          MYSQL_ROOT_PASSWORD: MyRootAdminPass! # <--- GANTI INI DENGAN PASSWORD ROOT ANDA SENDIRI
          MYSQL_DATABASE: quizlab_db
          MYSQL_USER: quizlab_user
          MYSQL_PASSWORD: MyStrongPass123! # <--- INI HARUS SAMA PERSIS DENGAN YANG DI ATAS
          # ...
    ```
    **Simpan perubahan pada file `docker-compose.yml`.**

3.  **Jalankan Aplikasi dengan Docker Compose:**
    Dari terminal atau PowerShell Anda, pastikan Anda berada di *root directory* proyek (`D:\IdeaProjec\QuizLab\`). Lalu, jalankan perintah berikut:
    ```bash
    docker compose up -d --build
    ```
    * Perintah ini akan secara otomatis mengunduh *image* MySQL, membangun *image* aplikasi Spring Boot Anda (menggunakan `Dockerfile`), dan menjalankan kedua komponen (`quizlab-app` dan `quizlab-db`) sebagai *container* yang saling terhubung di *background*.
    * `--build` memastikan *image* aplikasi dibangun jika belum ada atau ada perubahan.

4.  **Verifikasi Aplikasi Berjalan:**
    Anda bisa memeriksa status *container* dengan:
    ```bash
    docker compose ps
    ```
    Pastikan `quizlab-api-container` dan `quizlab-mysql-container` keduanya memiliki status `Up`.

5.  **Akses API:**
    Setelah kedua *container* berjalan, QuizLab API Anda akan tersedia di port `8090` di mesin lokal Anda.
    * Buka *browser* Anda dan kunjungi: `http://localhost:8090/api/health/status`
    * Anda seharusnya melihat pesan: `QuizLab API is Up and Running!`
    * Anda juga dapat mengunjungi dokumentasi Swagger UI di: `http://localhost:8090/swagger-ui.html`

## Menghentikan Aplikasi

Untuk menghentikan dan membersihkan *container* serta jaringan yang dibuat oleh Docker Compose:

```bash
docker compose down