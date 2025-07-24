**Modifikasi Kode:** Lakukan perubahan pada file kode sumber Java Anda (misalnya, `.java` files di `src/main/java`).

2.  **Bangun Ulang Proyek Lokal:**
    Jalankan perintah Gradle di terminal lokal Anda dari *root directory* proyek untuk mengkompilasi kode dan membuat file JAR yang diperbarui.
    ```bash
    ./gradlew clean build -x test
    ```

3.  **Hentikan Lingkungan Docker Lama:**
    Hentikan dan hapus *container* serta jaringan Docker yang sedang berjalan dari *build* sebelumnya.
    ```bash
    docker compose down
    ```

4.  **Bangun Ulang Image & Jalankan Aplikasi di Docker:**
    Bangun ulang *image* aplikasi Spring Boot Anda (agar menyertakan kode terbaru dari JAR yang baru) dan jalankan kembali seluruh *stack* aplikasi (API dan Database) di Docker.
    ```bash
    docker compose up -d --build
    ```