package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    // --- isValidKodeProduk ---
    @Test
    @DisplayName("Kode produk valid")
    void testIsValidKodeProduk_Valid() {
        assertTrue(ValidationUtils.isValidKodeProduk("P123"));
    }

    @Test
    @DisplayName("Kode produk null atau kosong")
    void testIsValidKodeProduk_NullOrEmpty() {
        assertFalse(ValidationUtils.isValidKodeProduk(null));
        assertFalse(ValidationUtils.isValidKodeProduk("   "));
    }

    @Test
    @DisplayName("Kode produk terlalu panjang / karakter ilegal")
    void testIsValidKodeProduk_InvalidFormat() {
        assertFalse(ValidationUtils.isValidKodeProduk("A@#"));
        assertFalse(ValidationUtils.isValidKodeProduk("ABCDE123456789")); // >10 karakter
    }

    // --- isValidNama ---
    @Test
    @DisplayName("Nama valid")
    void testIsValidNama_Valid() {
        assertTrue(ValidationUtils.isValidNama("Laptop Asus"));
    }

    @Test
    @DisplayName("Nama null, kosong, atau terlalu pendek")
    void testIsValidNama_Invalid() {
        assertFalse(ValidationUtils.isValidNama(null));
        assertFalse(ValidationUtils.isValidNama("  "));
        assertFalse(ValidationUtils.isValidNama("AB"));
    }

    @Test
    @DisplayName("Nama terlalu panjang")
    void testIsValidNama_TooLong() {
        String panjang = "A".repeat(101);
        assertFalse(ValidationUtils.isValidNama(panjang));
    }

    // --- isValidHarga ---
    @Test
    @DisplayName("Harga valid")
    void testIsValidHarga_Valid() {
        assertTrue(ValidationUtils.isValidHarga(1000.0));
    }

    @Test
    @DisplayName("Harga negatif atau nol tidak valid")
    void testIsValidHarga_Invalid() {
        assertFalse(ValidationUtils.isValidHarga(0));
        assertFalse(ValidationUtils.isValidHarga(-1));
    }

    // --- isValidStok dan isValidStokMinimum ---
    @Test
    @DisplayName("Stok dan stok minimum valid")
    void testIsValidStok_Valid() {
        assertTrue(ValidationUtils.isValidStok(0));
        assertTrue(ValidationUtils.isValidStok(5));
        assertTrue(ValidationUtils.isValidStokMinimum(3));
    }

    @Test
    @DisplayName("Stok dan stok minimum negatif")
    void testIsValidStok_Invalid() {
        assertFalse(ValidationUtils.isValidStok(-1));
        assertFalse(ValidationUtils.isValidStokMinimum(-2));
    }

    // --- isValidProduk ---
    @Test
    @DisplayName("Produk valid")
    void testIsValidProduk_Valid() {
        Produk produk = new Produk(
                "P01",
                "Laptop",
                "Elektronik",
                10000000,
                10,
                2);
        assertTrue(ValidationUtils.isValidProduk(produk));
    }

    @Test
    @DisplayName("Produk null")
    void testIsValidProduk_Null() {
        assertFalse(ValidationUtils.isValidProduk(null));
    }

    @Test
    @DisplayName("Produk dengan kode tidak valid")
    void testIsValidProduk_KodeInvalid() {
        Produk produk = new Produk(
                "@@@",
                "Laptop",
                "Elektronik",
                10000000,
                10,
                2);
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

    @Test
    @DisplayName("Produk dengan stok negatif")
    void testIsValidProduk_StokNegatif() {
        Produk produk = new Produk(
                "P01",
                "Laptop",
                "Elektronik",
                10000000,
                -1,
                2);
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

    // --- isValidKategori ---
    @Test
    @DisplayName("Kategori valid")
    void testIsValidKategori_Valid() {
        Kategori kategori = new Kategori("K01", "Elektronik", "Perangkat elektronik");
        assertTrue(ValidationUtils.isValidKategori(kategori));
    }

    @Test
    @DisplayName("Kategori null")
    void testIsValidKategori_Null() {
        assertFalse(ValidationUtils.isValidKategori(null));
    }

    @Test
    @DisplayName("Kategori kode invalid")
    void testIsValidKategori_KodeInvalid() {
        Kategori kategori = new Kategori("###", "Elektronik", "Deskripsi");
        assertFalse(ValidationUtils.isValidKategori(kategori));
    }

    @Test
    @DisplayName("Kategori dengan deskripsi terlalu panjang (>500)")
    void testIsValidKategori_DeskripsiPanjang() {
        String panjang = "A".repeat(501);
        Kategori kategori = new Kategori("K01", "Elektronik", panjang);
        assertFalse(ValidationUtils.isValidKategori(kategori));
    }

    // --- isValidPersentase ---
    @Test
    @DisplayName("Persentase valid")
    void testIsValidPersentase_Valid() {
        assertTrue(ValidationUtils.isValidPersentase(0));
        assertTrue(ValidationUtils.isValidPersentase(100));
        assertTrue(ValidationUtils.isValidPersentase(55.5));
    }

    @Test
    @DisplayName("Persentase di luar rentang 0-100")
    void testIsValidPersentase_Invalid() {
        assertFalse(ValidationUtils.isValidPersentase(-1));
        assertFalse(ValidationUtils.isValidPersentase(120));
    }

    // --- isValidKuantitas ---
    @Test
    @DisplayName("Kuantitas valid dan invalid")
    void testIsValidKuantitas() {
        assertTrue(ValidationUtils.isValidKuantitas(5));
        assertFalse(ValidationUtils.isValidKuantitas(0));
        assertFalse(ValidationUtils.isValidKuantitas(-1));
    }

    // --- Tambahan isValidProduk ---
    @Test
    @DisplayName("Produk dengan kategori tidak valid")
    void testIsValidProduk_KategoriInvalid() {
        Produk produk = new Produk("P01", "Laptop", "!", 10000000, 10, 2);
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

    @Test
    @DisplayName("Produk dengan harga nol atau negatif")
    void testIsValidProduk_HargaTidakValid() {
        Produk produk1 = new Produk("P01", "Laptop", "Elektronik", 0, 10, 2);
        Produk produk2 = new Produk("P01", "Laptop", "Elektronik", -5000, 10, 2);
        assertFalse(ValidationUtils.isValidProduk(produk1));
        assertFalse(ValidationUtils.isValidProduk(produk2));
    }

    @Test
    @DisplayName("Produk dengan stok minimum negatif")
    void testIsValidProduk_StokMinimumNegatif() {
        Produk produk = new Produk("P01", "Laptop", "Elektronik", 10000000, 10, -1);
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

    // --- Tambahan isValidKategori ---
    @Test
    @DisplayName("Kategori dengan deskripsi null tetap valid")
    void testIsValidKategori_DeskripsiNull() {
        Kategori kategori = new Kategori("K01", "Elektronik", null);
        assertTrue(ValidationUtils.isValidKategori(kategori));
    }

    @Test
    @DisplayName("Kategori dengan nama tidak valid")
    void testIsValidKategori_NamaTidakValid() {
        Kategori kategori = new Kategori("K01", "A", "Deskripsi pendek");
        assertFalse(ValidationUtils.isValidKategori(kategori));
    }

    @Test
    @DisplayName("Produk dengan nama tidak valid menyebabkan isValidProduk false")
    void testIsValidProduk_NamaTidakValid() {
        Produk produk = new Produk("P01", "A", "Elektronik", 10000000, 10, 5);
        assertFalse(ValidationUtils.isValidProduk(produk));
    }

}
