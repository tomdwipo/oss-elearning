# KMP Gradle & UTF-8 Constant Size Limit Gotchas

## 1. JVM 64KB UTF-8 String Constant Limit
### Gejala
Kompilasi Kotlin (`compileKotlin*`) gagal dengan error:
`java.lang.IllegalArgumentException: UTF8 string too large` atau JVM class file verification error saat string literal berukuran besar (> 65,535 bytes) dideklarasikan langsung di kode Kotlin (misal embedding static JSON).

### Akar Masalah
Format `.class` JVM membatasi entri `CONSTANT_Utf8_info` pada Constant Pool maksimal 65,535 byte. File JSON kurikulum lengkap 8 semester memiliki ukuran ~149 KB.

### Solusi
1. Pecah (*chunk*) string literal ke dalam beberapa potongan array berukuran < 25 KB:
   ```kotlin
   private val RAW_JSON_CHUNKS = arrayOf(
       CHUNK_1, CHUNK_2, CHUNK_3, ...
   )
   val RAW_JSON: String get() = RAW_JSON_CHUNKS.joinToString("")
   ```
2. Atau muat secara asinkron dari resource bundle (`composeResources/files/`).

---

## 2. Gradle Version Incompatibility dengan Kotlin Multiplatform 2.1.0
### Gejala
`ClassNotFoundException: org.gradle.api.internal.artifacts.DefaultArtifactPublicationSet` saat mengonfigurasi framework target Apple (iOS).

### Akar Masalah
Gradle 9.x menghapus internal class `DefaultArtifactPublicationSet` yang masih dipanggil oleh KMP plugin Kotlin 2.1.0.

### Solusi
Gunakan Gradle 8.11.1 pada `gradle/wrapper/gradle-wrapper.properties`:
```properties
distributionUrl=https\://mirrors.cloud.tencent.com/gradle/gradle-8.11.1-bin.zip
```
Dan gunakan mirror Google Maven Central untuk network yang memblokir repo Apache:
```kotlin
maven { url = uri("https://maven-central.storage-download.googleapis.com/maven2/") }
```
