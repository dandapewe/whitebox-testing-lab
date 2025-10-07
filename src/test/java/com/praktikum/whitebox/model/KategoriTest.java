package com.praktikum.whitebox.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("White Box Testing dengan Positive & Negative Test - Class Kategori")
public class KategoriTest {

    private Kategori kategori;

    @BeforeEach
    void setUp() {
        kategori = new Kategori(
                "KAT001",
                "Elektronik",
                "Kategori produk elektronik");
    }

    // Konstruktor berparameter
    @Test
    @DisplayName("Konstruktor berparameter menginisialisasi nilai dengan benar")
    void testKonstruktorBerparameter() {
        assertEquals("KAT001", kategori.getKode());
        assertEquals("Elektronik", kategori.getNama());
        assertEquals("Kategori produk elektronik", kategori.getDeskripsi());
        assertTrue(kategori.isAktif());
    }

    // Konstruktor kosong
    @Test
    @DisplayName("Konstruktor kosong dapat membuat objek tanpa error")
    void testKonstruktorKosong() {
        Kategori kat = new Kategori();
        assertNotNull(kat);
        kat.setKode("KAT002");
        kat.setNama("Fashion");
        kat.setDeskripsi("Kategori pakaian");
        kat.setAktif(false);

        assertEquals("KAT002", kat.getKode());
        assertEquals("Fashion", kat.getNama());
        assertEquals("Kategori pakaian", kat.getDeskripsi());
        assertFalse(kat.isAktif());
    }

    // Getter dan Setter (positive)
    @Test
    @DisplayName("Getter dan Setter berfungsi normal")
    void testGetterSetter() {
        kategori.setKode("KAT003");
        kategori.setNama("Makanan");
        kategori.setDeskripsi("Produk konsumsi");
        kategori.setAktif(false);

        assertEquals("KAT003", kategori.getKode());
        assertEquals("Makanan", kategori.getNama());
        assertEquals("Produk konsumsi", kategori.getDeskripsi());
        assertFalse(kategori.isAktif());
    }

    // Negative Test - Setter dengan nilai null
    @Test
    @DisplayName("Setter menerima nilai null tanpa error (tapi tetap null)")
    void testSetterDenganNull() {
        kategori.setKode(null);
        kategori.setNama(null);
        kategori.setDeskripsi(null);

        assertNull(kategori.getKode());
        assertNull(kategori.getNama());
        assertNull(kategori.getDeskripsi());
    }

    // equals dan hashCode (branch & condition coverage)
    @Test
    @DisplayName("Test equals dan hashCode di berbagai kondisi")
    void testEqualsAndHashCode() {
        Kategori kat1 = new Kategori("KAT001", "Laptop", "Elektronik");
        Kategori kat2 = new Kategori("KAT001", "Gadget", "Barang teknologi");
        Kategori kat3 = new Kategori("KAT999", "Makanan", "Produk konsumsi");

        // Sama kode → true
        assertEquals(kat1, kat2);
        assertEquals(kat1.hashCode(), kat2.hashCode());

        // Kode beda → false
        assertNotEquals(kat1, kat3);

        // Objek null → false
        assertNotEquals(kat1, null);

        // Beda tipe → false
        assertNotEquals(kat1, "String");

        // Diri sendiri → true
        assertEquals(kat1, kat1);
    }

    // Negative Test - equals jika kode null
    @Test
    @DisplayName("equals() tetap aman jika kode bernilai null")
    void testEqualsDenganKodeNull() {
        Kategori kat1 = new Kategori(null, "Kosong", "Tanpa kode");
        Kategori kat2 = new Kategori(null, "Kosong juga", "Deskripsi lain");

        // Dua kode null tetap dianggap sama karena Objects.equals() aman
        assertEquals(kat1, kat2);
    }

    // toString test
    @Test
    @DisplayName("toString menampilkan semua informasi utama")
    void testToString() {
        String result = kategori.toString();
        assertTrue(result.contains("KAT001"));
        assertTrue(result.contains("Elektronik"));
        assertTrue(result.contains("Kategori produk elektronik"));
        assertTrue(result.contains("aktif=true"));
    }
}
