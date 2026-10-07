# Sigara Modu (Fabric, Minecraft 26.2)

Gerçek sigara/puro/paket/çakmak çeşitleri, güzel pixel-art dokular ve duman efektleri. Yan etki yok.

## Derleme
1. JDK 25 kur.
2. Gradle wrapper jar'ı yoksa: `gradle wrapper --gradle-version 9.3.0`
   (veya https://fabricmc.net/develop/template/ adresinden şablon indirip `gradlew`, `gradlew.bat` ve `gradle/wrapper/gradle-wrapper.jar` dosyalarını bu klasöre kopyala)
3. `./gradlew build` -> `build/libs/sigara-1.0.0.jar` dosyasını `mods/` klasörüne at (Fabric API gerekli).

## Kurulum yapmadan jar almak (GitHub Actions)
1. github.com'da yeni bir repo aç, bu klasörün içindeki her şeyi yükle (`.github` klasörü dahil).
2. Repo'da **Actions** sekmesine gir, "Jar Derle" çalışmasının bitmesini bekle (2-5 dk).
3. Çalışmanın sayfasında alttaki **Artifacts** bölümünden `sigara-mod-jar` dosyasını indir.
   İçindeki `sigara-1.0.0.jar` dosyasını `mods` klasörüne at (sigara-1.0.0-sources.jar'ı atma).
