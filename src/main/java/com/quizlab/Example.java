//////////// src/main/java/com/quizlab/api/service/UserService.java
//////////package com.quizlab;
//////////
//////////import com.quizlab.dto.UserRegisterRequest; // Import DTO input register
//////////import com.quizlab.dto.UserLoginRequest;   // Import DTO input login
//////////import com.quizlab.dto.UserResponse;      // Import DTO output
//////////import com.quizlab.model.User;           // Import entitas User
//////////import com.quizlab.repository.UserRepository; // Import UserRepository
//////////import lombok.RequiredArgsConstructor;
//////////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import BCryptPasswordEncoder
//////////import org.springframework.stereotype.Service;
//////////import org.springframework.transaction.annotation.Transactional;
//////////
//////////import java.time.LocalDateTime;
//////////import java.util.Optional;
//////////import java.util.UUID;
//////////
//////////@Service
//////////@RequiredArgsConstructor
//////////@Transactional // Semua method publik di sini akan dalam transaksi DB
//////////public class UserService {
//////////
//////////    private final UserRepository userRepository;
//////////    private final BCryptPasswordEncoder passwordEncoder; // Injeksi BCryptPasswordEncoder
//////////
//////////    // Contoh method sederhana untuk mencari user berdasarkan ID
//////////    public Optional<User> findUserById(UUID id) {
//////////        return userRepository.findById(id);
//////////    }
//////////
//////////    /**
//////////     * Mendaftarkan user baru ke sistem.
//////////     * Melakukan hashing password dan menyimpan user ke database.
//////////     * @param request DTO yang berisi data username dan password untuk registrasi.
//////////     * @return UserResponse DTO dari user yang berhasil didaftarkan.
//////////     * @throws RuntimeException jika username atau email sudah terdaftar.
//////////     */
//////////    public UserResponse registerUser(UserRegisterRequest request) {
//////////        // 1. Cek apakah username sudah terdaftar
//////////        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
//////////            throw new RuntimeException("Username '" + request.getUsername() + "' sudah terdaftar.");
//////////        }
//////////        // Jika Anda punya email di UserRegisterRequest dan ingin unik:
//////////        // if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//////////        //     throw new RuntimeException("Email '" + request.getEmail() + "' sudah terdaftar.");
//////////        // }
//////////
//////////        // 2. Hash password
//////////        String hashedPassword = passwordEncoder.encode(request.getPassword());
//////////
//////////        // 3. Buat objek User dari DTO Request
//////////        User newUser = new User();
//////////        newUser.setUsername(request.getUsername());
//////////        newUser.setPasswordHash(hashedPassword);
//////////        // Field lain (email, displayName, bio, profilePictureUrl) bisa diatur di sini
//////////        // jika ada di UserRegisterRequest atau punya nilai default.
//////////        // Untuk sekarang, mereka akan null kecuali diset.
//////////        newUser.setCreatedAt(LocalDateTime.now()); // @PrePersist akan mengisi ini juga, tapi tidak masalah diset di sini.
//////////        // @PrePersist akan memastikan ia TIDAK NULL jika kita lupa set di sini.
//////////
//////////        // 4. Simpan user ke database
//////////        User savedUser = userRepository.save(newUser);
//////////
//////////        // 5. Konversi entitas User yang disimpan ke DTO Response dan kembalikan
//////////        return UserResponse.builder()
//////////                .id(savedUser.getId())
//////////                .username(savedUser.getUsername())
//////////                .email(savedUser.getEmail()) // Akan null jika tidak ada di request atau tidak diset
//////////                .displayName(savedUser.getDisplayName()) // Akan null jika tidak diset
//////////                .bio(savedUser.getBio()) // Akan null jika tidak diset
//////////                .profilePictureUrl(savedUser.getProfilePictureUrl()) // Akan null jika tidak diset
//////////                .createdAt(savedUser.getCreatedAt())
//////////                .updatedAt(savedUser.getUpdatedAt())
//////////                .build();
//////////    }
//////////
//////////    /**
//////////     * Melakukan otentikasi user.
//////////     * Memverifikasi username dan password.
//////////     * @param request DTO yang berisi data username dan password untuk login.
//////////     * @return Optional<UserResponse> DTO dari user yang berhasil diotentikasi, atau Optional.empty() jika gagal.
//////////     */
//////////    public Optional<UserResponse> authenticateUser(UserLoginRequest request) {
//////////        // 1. Cari user berdasarkan username
//////////        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
//////////
//////////        // 2. Jika user ditemukan, verifikasi password
//////////        if (userOptional.isPresent()) {
//////////            User user = userOptional.get();
//////////            // passwordEncoder.matches(rawPassword, encodedPassword) membandingkan password mentah dengan hash
//////////            if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
//////////                // Password cocok, user berhasil diotentikasi
//////////                return Optional.of(UserResponse.builder()
//////////                        .id(user.getId())
//////////                        .username(user.getUsername())
//////////                        .email(user.getEmail())
//////////                        .displayName(user.getDisplayName())
//////////                        .bio(user.getBio())
//////////                        .profilePictureUrl(user.getProfilePictureUrl())
//////////                        .createdAt(user.getCreatedAt())
//////////                        .updatedAt(user.getUpdatedAt())
//////////                        .build());
//////////            }
//////////        }
//////////        // Jika user tidak ditemukan atau password tidak cocok
//////////        return Optional.empty();
//////////    }
//////////}
////////
////////// src/main/java/com/quizlab/api/controller/AuthController.java
////////package com.quizlab.api.controller;
////////
////////import com.quizlab.api.dto.UserLoginRequest;   // DTO untuk permintaan login
////////import com.quizlab.api.dto.UserRegisterRequest;  // DTO untuk permintaan registrasi
////////import com.quizlab.api.dto.UserResponse;       // DTO untuk respons user
////////import com.quizlab.api.service.UserService;     // Service layer yang akan kita panggil
////////import jakarta.validation.Valid;                // Untuk mengaktifkan validasi DTO
////////import lombok.RequiredArgsConstructor;          // Lombok untuk Dependency Injection
////////import org.springframework.http.HttpStatus;     // Untuk kode status HTTP
////////import org.springframework.http.ResponseEntity; // Untuk membangun respons HTTP
////////import org.springframework.web.bind.annotation.*; // Anotasi untuk REST Controller
////////
////////import java.util.Optional; // Untuk menangani hasil Optional dari UserService
////////
////////@RestController // Menandakan bahwa ini adalah REST Controller
////////@RequestMapping("/api/auth") // Base path untuk semua endpoint di controller ini
////////@RequiredArgsConstructor // Lombok: Membuat konstruktor untuk UserService (Dependency Injection)
////////public class AuthController {
////////
////////    private final UserService userService; // Injeksi UserService
////////
////////    /**
////////     * Endpoint untuk registrasi pengguna baru.
////////     * Menerima UserRegisterRequest, memanggil UserService untuk registrasi.
////////     * @param request DTO yang berisi username dan password untuk registrasi.
////////     * @return ResponseEntity<UserResponse> dengan status 201 Created jika sukses, atau error jika gagal.
////////     */
////////    @PostMapping("/register") // Memetakan permintaan HTTP POST ke /api/auth/register
////////    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
////////        // @Valid: Memicu validasi yang didefinisikan di UserRegisterRequest (misal @NotBlank, @Size).
////////        //         Jika validasi gagal, Spring akan mengembalikan HTTP 400 Bad Request secara otomatis.
////////        // @RequestBody: Menunjukkan bahwa data request (JSON) akan dipetakan ke objek UserRegisterRequest.
////////
////////        try {
////////            UserResponse newUser = userService.registerUser(request);
////////            // Jika registrasi berhasil, kembalikan status 201 Created dan data user.
////////            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
////////        } catch (RuntimeException e) {
////////            // Jika ada RuntimeException dari UserService (misal: username sudah terdaftar),
////////            // kembalikan status 400 Bad Request dengan pesan error.
////////            // Di aplikasi nyata, Anda akan membuat Custom Exception dan Global Exception Handler
////////            // untuk penanganan error yang lebih rapi dan spesifik.
////////            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
////////            // Atau Anda bisa mengembalikan pesan error di body:
////////            // return new ResponseEntity<>(new UserResponse(), HttpStatus.BAD_REQUEST); // Contoh jika UserResponse punya field error
////////            // Atau cukup: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
////////        }
////////    }
////////
////////    /**
////////     * Endpoint untuk login pengguna.
////////     * Menerima UserLoginRequest, memanggil UserService untuk otentikasi.
////////     * @param request DTO yang berisi username dan password untuk login.
////////     * @return ResponseEntity<UserResponse> dengan status 200 OK jika sukses, atau 401 Unauthorized jika gagal.
////////     */
////////    @PostMapping("/login") // Memetakan permintaan HTTP POST ke /api/auth/login
////////    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserLoginRequest request) {
////////        // @Valid dan @RequestBody berfungsi sama seperti di endpoint register.
////////
////////        Optional<UserResponse> authenticatedUser = userService.authenticateUser(request);
////////
////////        if (authenticatedUser.isPresent()) {
////////            // Jika otentikasi berhasil, kembalikan status 200 OK dan data user.
////////            // Di sini nanti kita bisa tambahkan JWT Token ke UserResponse atau sebagai Header.
////////            return new ResponseEntity<>(authenticatedUser.get(), HttpStatus.OK);
////////        } else {
////////            // Jika otentikasi gagal (username tidak ditemukan atau password salah),
////////            // kembalikan status 401 Unauthorized.
////////            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
////////            // Di aplikasi nyata, Anda mungkin ingin lebih spesifik (misal 403 Forbidden untuk akses salah,
////////            // atau hanya 400 Bad Request jika Anda tidak ingin memberi tahu apakah username ada atau tidak).
////////        }
////////    }
////////}do
//////
//////// src/main/java/com/quizlab/service/CategoryService.java
//////package com.quizlab.service; // Pastikan ini sesuai dengan struktur paket Anda
//////
//////import com.quizlab.dto.CategoryRequest;  // DTO input untuk kategori
//////import com.quizlab.dto.CategoryResponse; // DTO output untuk kategori
//////import com.quizlab.model.Category;      // Entitas Category
//////import com.quizlab.repository.CategoryRepository; // Repositori untuk Category
//////import lombok.RequiredArgsConstructor;   // Lombok untuk konstruktor
//////import org.springframework.stereotype.Service; // Anotasi @Service
//////import org.springframework.transaction.annotation.Transactional; // Untuk manajemen transaksi
//////import java.time.LocalDateTime;           // Untuk timestamp
//////import java.util.List;
//////import java.util.Optional;
//////import java.util.UUID;
//////import java.util.stream.Collectors;       // Untuk konversi List
//////
//////@Service // Menandakan bahwa kelas ini adalah komponen Service Spring
//////@RequiredArgsConstructor // Lombok: Membuat konstruktor untuk field final (Dependency Injection)
//////@Transactional // Semua method publik di kelas ini akan berjalan dalam sebuah transaksi database
//////public class CategoryService {
//////
//////    private final CategoryRepository categoryRepository; // Injeksi CategoryRepository
//////
//////    /**
//////     * Membuat kategori baru.
//////     * @param request DTO yang berisi nama dan deskripsi kategori.
//////     * @return CategoryResponse DTO dari kategori yang berhasil dibuat.
//////     * @throws RuntimeException jika nama kategori sudah ada.
//////     */
//////    public CategoryResponse createCategory(CategoryRequest request) {
//////        // 1. Cek apakah nama kategori sudah ada
//////        if (categoryRepository.findByName(request.getName()).isPresent()) {
//////            throw new RuntimeException("Nama kategori '" + request.getName() + "' sudah ada.");
//////        }
//////
//////        // 2. Buat objek Category dari DTO Request
//////        Category newCategory = new Category();
//////        newCategory.setName(request.getName());
//////        newCategory.setDescription(request.getDescription());
//////        // createdAt dan updatedAt akan diisi otomatis oleh @PrePersist/@PreUpdate di entitas
//////
//////        // 3. Simpan kategori ke database
//////        Category savedCategory = categoryRepository.save(newCategory);
//////
//////        // 4. Konversi entitas Category yang disimpan ke DTO Response dan kembalikan
//////        return mapToCategoryResponse(savedCategory);
//////    }
//////
//////    /**
//////     * Mengambil daftar semua kategori.
//////     * @return List<CategoryResponse> daftar kategori.
//////     */
//////    @Transactional(readOnly = true) // Transaksi hanya untuk membaca, bisa lebih optimal
//////    public List<CategoryResponse> getAllCategories() {
//////        return categoryRepository.findAll().stream()
//////                .map(this::mapToCategoryResponse) // Konversi setiap entitas ke DTO
//////                .collect(Collectors.toList());
//////    }
//////
//////    /**
//////     * Mengambil kategori berdasarkan ID.
//////     * @param id ID kategori.
//////     * @return Optional<CategoryResponse> DTO kategori jika ditemukan.
//////     */
//////    @Transactional(readOnly = true)
//////    public Optional<CategoryResponse> getCategoryById(UUID id) {
//////        return categoryRepository.findById(id)
//////                .map(this::mapToCategoryResponse); // Konversi ke DTO jika ada
//////    }
//////
//////    /**
//////     * Memperbarui kategori yang sudah ada.
//////     * @param id ID kategori yang akan diperbarui.
//////     * @param request DTO dengan data kategori yang baru.
//////     * @return Optional<CategoryResponse> DTO kategori yang diperbarui jika ditemukan.
//////     * @throws RuntimeException jika kategori tidak ditemukan atau nama kategori duplikat.
//////     */
//////    public Optional<CategoryResponse> updateCategory(UUID id, CategoryRequest request) {
//////        return categoryRepository.findById(id).map(existingCategory -> {
//////            // Cek duplikasi nama jika nama berubah dan sudah ada di kategori lain
//////            if (!existingCategory.getName().equals(request.getName())) {
//////                if (categoryRepository.findByName(request.getName()).isPresent()) {
//////                    throw new RuntimeException("Nama kategori '" + request.getName() + "' sudah ada.");
//////                }
//////            }
//////
//////            existingCategory.setName(request.getName());
//////            existingCategory.setDescription(request.getDescription());
//////            // updatedAt akan diisi otomatis oleh @PreUpdate di entitas
//////
//////            Category updatedCategory = categoryRepository.save(existingCategory);
//////            return mapToCategoryResponse(updatedCategory);
//////        });
//////        // Jika Optional kosong (kategori tidak ditemukan), map() tidak akan dijalankan dan Optional kosong akan dikembalikan
//////    }
//////
//////    /**
//////     * Menghapus kategori berdasarkan ID.
//////     * @param id ID kategori yang akan dihapus.
//////     * @return true jika berhasil dihapus, false jika kategori tidak ditemukan.
//////     */
//////    public boolean deleteCategory(UUID id) {
//////        if (categoryRepository.existsById(id)) {
//////            categoryRepository.deleteById(id);
//////            return true;
//////        }
//////        return false;
//////    }
//////
//////    /**
//////     * Metode helper untuk mengkonversi entitas Category ke CategoryResponse DTO.
//////     * @param category Entitas Category.
//////     * @return CategoryResponse DTO.
//////     */
//////    private CategoryResponse mapToCategoryResponse(Category category) {
//////        return CategoryResponse.builder()
//////                .id(category.getId())
//////                .name(category.getName())
//////                .description(category.getDescription())
//////                .createdAt(category.getCreatedAt())
//////                .updatedAt(category.getUpdatedAt())
//////                .build();
//////    }
//////}
////
////// src/main/java/com/quizlab/controller/CategoryController.java
////package com.quizlab.controller; // Pastikan ini sesuai dengan struktur paket Anda
////
////import com.quizlab.dto.CategoryRequest;   // DTO input untuk kategori
////import com.quizlab.dto.CategoryResponse;  // DTO output untuk kategori
////import com.quizlab.service.CategoryService; // Service layer yang akan kita panggil
////import jakarta.validation.Valid;           // Untuk mengaktifkan validasi DTO
////import lombok.RequiredArgsConstructor;     // Lombok untuk Dependency Injection
////import org.springframework.http.HttpStatus; // Untuk kode status HTTP
////import org.springframework.http.ResponseEntity; // Untuk membangun respons HTTP
////import org.springframework.web.bind.annotation.*; // Anotasi untuk REST Controller
////
////import java.util.List;
////import java.util.UUID;
////import java.util.Optional;
////
////@RestController // Menandakan bahwa kelas ini adalah REST Controller
////@RequestMapping("/api/v1/categories") // Base path untuk semua endpoint kategori
////@RequiredArgsConstructor // Lombok: Membuat konstruktor untuk CategoryService (Dependency Injection)
////public class CategoryController {
////
////    private final CategoryService categoryService; // Injeksi CategoryService
////
////    /**
////     * Endpoint untuk membuat kategori baru.
////     * POST /api/v1/categories
////     * @param request DTO yang berisi nama dan deskripsi kategori.
////     * @return ResponseEntity<CategoryResponse> dengan status 201 Created jika sukses, atau 400 Bad Request jika gagal.
////     */
////    @PostMapping
////    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
////        try {
////            CategoryResponse newCategory = categoryService.createCategory(request);
////            return new ResponseEntity<>(newCategory, HttpStatus.CREATED); // Status 201 CREATED
////        } catch (RuntimeException e) {
////            // Contoh penanganan error dasar untuk nama kategori duplikat
////            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Status 400 Bad Request
////        }
////    }
////
////    /**
////     * Endpoint untuk mengambil semua kategori.
////     * GET /api/v1/categories
////     * @return ResponseEntity<List<CategoryResponse>> daftar kategori.
////     */
////    @GetMapping
////    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
////        List<CategoryResponse> categories = categoryService.getAllCategories();
////        return new ResponseEntity<>(categories, HttpStatus.OK); // Status 200 OK
////    }
////
////    /**
////     * Endpoint untuk mengambil kategori berdasarkan ID.
////     * GET /api/v1/categories/{id}
////     * @param id ID kategori.
////     * @return ResponseEntity<CategoryResponse> dengan status 200 OK jika ditemukan, atau 404 Not Found.
////     */
////    @GetMapping("/{id}") // Path variable {id}
////    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
////        // @PathVariable: Mengambil nilai 'id' dari URL path.
////        Optional<CategoryResponse> category = categoryService.getCategoryById(id);
////        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Jika ada, return 200 OK
////                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Jika tidak, return 404 Not Found
////    }
////
////    /**
////     * Endpoint untuk memperbarui kategori.
////     * PUT /api/v1/categories/{id}
////     * @param id ID kategori yang akan diperbarui.
////     * @param request DTO dengan data kategori yang diperbarui.
////     * @return ResponseEntity<CategoryResponse> dengan status 200 OK jika sukses, atau 404 Not Found / 400 Bad Request.
////     */
////    @PutMapping("/{id}")
////    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryRequest request) {
////        try {
////            Optional<CategoryResponse> updatedCategory = categoryService.updateCategory(id, request);
////            return updatedCategory.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Jika update sukses, return 200 OK
////                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Jika kategori tidak ditemukan (ID salah), return 404 Not Found
////        } catch (RuntimeException e) {
////            // Contoh penanganan error dasar untuk nama kategori duplikat saat update
////            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Status 400 Bad Request
////        }
////    }
////
////    /**
////     * Endpoint untuk menghapus kategori.
////     * DELETE /api/v1/categories/{id}
////     * @param id ID kategori yang akan dihapus.
////     * @return ResponseEntity<Void> dengan status 204 No Content jika sukses, atau 404 Not Found.
////     */
////    @DeleteMapping("/{id}")
////    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
////        boolean deleted = categoryService.deleteCategory(id);
////        if (deleted) {
////            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Status 204 No Content (Sukses tanpa body)
////        } else {
////            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Status 404 Not Found (Kategori tidak ditemukan)
////        }
////    }
////}
//
//// src/main/java/com/quizlab/service/QuestionService.java
//package com.quizlab.service; // Pastikan ini sesuai dengan struktur paket Anda
//
//import com.quizlab.dto.QuestionRequest;   // DTO input untuk pertanyaan
//import com.quizlab.dto.QuestionResponse;  // DTO output untuk pertanyaan
//import com.quizlab.model.Category;        // Entitas Category
//import com.quizlab.model.Question;        // Entitas Question
//import com.quizlab.repository.CategoryRepository; // Repositori untuk Category (untuk validasi FK)
//import com.quizlab.repository.QuestionRepository; // Repositori untuk Question
//import jakarta.persistence.EntityNotFoundException; // Exception jika entitas tidak ditemukan
//import lombok.RequiredArgsConstructor;     // Lombok untuk konstruktor
//import org.springframework.stereotype.Service; // Anotasi @Service
//import org.springframework.transaction.annotation.Transactional; // Untuk manajemen transaksi
//
//import java.time.LocalDateTime;           // Untuk timestamp
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;       // Untuk konversi List
//
//@Service // Menandakan bahwa kelas ini adalah komponen Service Spring. Di sinilah logika bisnis aplikasi berada.
//@RequiredArgsConstructor // Lombok: Otomatis membuat konstruktor dengan semua field final sebagai argumen (Dependency Injection).
//@Transactional // Anotasi ini menandakan bahwa semua method publik di kelas ini akan berjalan dalam sebuah transaksi database. Penting untuk konsistensi data.
//public class QuestionService {
//
//    private final QuestionRepository questionRepository; // Injeksi QuestionRepository
//    private final CategoryRepository categoryRepository; // Injeksi CategoryRepository (untuk validasi kategori)
//
//    /**
//     * Membuat pertanyaan baru.
//     * @param request DTO yang berisi detail pertanyaan.
//     * @return QuestionResponse DTO dari pertanyaan yang berhasil dibuat.
//     * @throws EntityNotFoundException jika kategori yang diacu tidak ditemukan.
//     */
//    public QuestionResponse createQuestion(QuestionRequest request) {
//        // 1. Validasi: Pastikan kategori yang diacu ada
//        Category category = categoryRepository.findById(request.getCategoryId())
//                .orElseThrow(() -> new EntityNotFoundException("Kategori dengan ID " + request.getCategoryId() + " tidak ditemukan."));
//
//        // 2. Buat objek Question dari DTO Request
//        Question newQuestion = new Question();
//        newQuestion.setText(request.getText());
//        newQuestion.setCategory(category); // Set objek Category yang sudah ditemukan
//        newQuestion.setDifficultyLevel(request.getDifficultyLevel());
//        newQuestion.setExplanation(request.getExplanation());
//        newQuestion.setActive(true); // Defaultnya aktif saat dibuat
//
//        // createdAt dan updatedAt akan diisi otomatis oleh @PrePersist/@PreUpdate di entitas Question.
//
//        // 3. Simpan pertanyaan ke database menggunakan repository
//        Question savedQuestion = questionRepository.save(newQuestion);
//
//        // 4. Konversi entitas Question yang disimpan ke DTO Response dan kembalikan
//        return mapToQuestionResponse(savedQuestion);
//    }
//
//    /**
//     * Mengambil daftar semua pertanyaan.
//     * @return List<QuestionResponse> daftar pertanyaan.
//     */
//    @Transactional(readOnly = true) // Transaksi hanya untuk membaca, bisa lebih optimal performanya.
//    public List<QuestionResponse> getAllQuestions() {
//        return questionRepository.findAll().stream()
//                .map(this::mapToQuestionResponse)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Mengambil pertanyaan berdasarkan ID.
//     * @param id ID pertanyaan.
//     * @return Optional<QuestionResponse> DTO pertanyaan jika ditemukan.
//     */
//    @Transactional(readOnly = true)
//    public Optional<QuestionResponse> getQuestionById(UUID id) {
//        return questionRepository.findById(id)
//                .map(this::mapToQuestionResponse);
//    }
//
//    /**
//     * Mengambil daftar pertanyaan berdasarkan ID kategori.
//     * @param categoryId ID kategori.
//     * @return List<QuestionResponse> daftar pertanyaan dalam kategori tersebut.
//     * @throws EntityNotFoundException jika kategori yang diacu tidak ditemukan.
//     */
//    @Transactional(readOnly = true)
//    public List<QuestionResponse> getQuestionsByCategoryId(UUID categoryId) {
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new EntityNotFoundException("Kategori dengan ID " + categoryId + " tidak ditemukan."));
//
//        return questionRepository.findByCategory(category).stream()
//                .map(this::mapToQuestionResponse)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Mengambil daftar pertanyaan berdasarkan tingkat kesulitan.
//     * @param difficultyLevel Tingkat kesulitan.
//     * @return List<QuestionResponse> daftar pertanyaan dengan tingkat kesulitan tersebut.
//     */
//    @Transactional(readOnly = true)
//    public List<QuestionResponse> getQuestionsByDifficultyLevel(com.quizlab.model.DifficultyLevel difficultyLevel) {
//        return questionRepository.findByDifficultyLevel(difficultyLevel).stream()
//                .map(this::mapToQuestionResponse)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Memperbarui pertanyaan yang sudah ada.
//     * @param id ID pertanyaan yang akan diperbarui.
//     * @param request DTO dengan data pertanyaan yang baru.
//     * @return Optional<QuestionResponse> DTO pertanyaan yang diperbarui jika ditemukan.
//     * @throws EntityNotFoundException jika pertanyaan atau kategori yang diacu tidak ditemukan.
//     */
//    public Optional<QuestionResponse> updateQuestion(UUID id, QuestionRequest request) {
//        return questionRepository.findById(id).map(existingQuestion -> {
//            // Validasi: Pastikan kategori yang diacu ada (jika categoryId berubah atau selalu dikirim)
//            Category category = categoryRepository.findById(request.getCategoryId())
//                    .orElseThrow(() -> new EntityNotFoundException("Kategori dengan ID " + request.getCategoryId() + " tidak ditemukan."));
//
//            existingQuestion.setText(request.getText());
//            existingQuestion.setCategory(category);
//            existingQuestion.setDifficultyLevel(request.getDifficultyLevel());
//            existingQuestion.setExplanation(request.getExplanation());
//            // isActive bisa diupdate melalui endpoint terpisah atau disertakan di request jika diinginkan
//            // existingQuestion.setActive(request.isActive());
//
//            Question updatedQuestion = questionRepository.save(existingQuestion);
//            return mapToQuestionResponse(updatedQuestion);
//        });
//    }
//
//    /**
//     * Menghapus pertanyaan berdasarkan ID.
//     * @param id ID pertanyaan yang akan dihapus.
//     * @return true jika berhasil dihapus, false jika pertanyaan tidak ditemukan.
//     */
//    public boolean deleteQuestion(UUID id) {
//        if (questionRepository.existsById(id)) {
//            questionRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Mengubah status aktif/non-aktif pertanyaan.
//     * @param id ID pertanyaan.
//     * @param isActive Status aktif baru.
//     * @return Optional<QuestionResponse> DTO pertanyaan yang diperbarui jika ditemukan.
//     */
//    public Optional<QuestionResponse> updateQuestionActiveStatus(UUID id, boolean isActive) {
//        return questionRepository.findById(id).map(question -> {
//            question.setActive(isActive);
//            Question updatedQuestion = questionRepository.save(question);
//            return mapToQuestionResponse(updatedQuestion);
//        });
//    }
//
//    /**
//     * Metode helper untuk mengkonversi entitas Question ke QuestionResponse DTO.
//     * @param question Entitas Question.
//     * @return QuestionResponse DTO.
//     */
//    private QuestionResponse mapToQuestionResponse(Question question) {
//        return QuestionResponse.builder()
//                .id(question.getId())
//                .text(question.getText())
//                .categoryId(question.getCategory().getId()) // Ambil ID kategori dari objek Category
//                .categoryName(question.getCategory().getName()) // Ambil Nama kategori dari objek Category
//                .difficultyLevel(question.getDifficultyLevel())
//                .explanation(question.getExplanation())
//                .isActive(question.isActive())
//                .createdAt(question.getCreatedAt())
//                .updatedAt(question.getUpdatedAt())
//                .build();
//    }
//}