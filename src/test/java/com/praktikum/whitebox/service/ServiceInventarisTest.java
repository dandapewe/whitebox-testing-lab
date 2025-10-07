package com.praktikum.whitebox.service;
import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test Service Inventaris dengan Mocking")
public class ServiceInventarisTest {
    @Mock
    private RepositoryProduk mockRepositoryProduk;
    private ServiceInventaris serviceInventaris;
    private Produk produkTest;
    @BeforeEach
    void setUp() {
        serviceInventaris = new ServiceInventaris(mockRepositoryProduk);
        produkTest = new Produk(
                "PROD001",
                "Laptop Gaming",
                "Elektronik",
                15000000,
                10,
                5);
    }

    @Test
    @DisplayName("Tambah produk berhasil - semua kondisi valid")
    void testTambahProdukBerhasil() {
// Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        when(mockRepositoryProduk.simpan(produkTest)).thenReturn(true);
// Act
        boolean hasil = serviceInventaris.tambahProduk(produkTest);
// Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).simpan(produkTest);
    }

    @Test
    @DisplayName("Tambah produk gagal - produk sudah ada")
    void testTambahProdukGagalSudahAda() {
// Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        // Act
        boolean hasil = serviceInventaris.tambahProduk(produkTest);
// Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).simpan(any(Produk.class));
    }

    @Test
    @DisplayName("Keluar stok berhasil - stok mencukupi")
    void testKeluarStokBerhasil() {
// Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001",
                5)).thenReturn(true);
// Act
        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);
// Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 5);
    }

    @Test
    @DisplayName("Keluar stok gagal - stok tidak mencukupi")
    void testKeluarStokGagalStokTidakMencukupi() {
// Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
// Act
        boolean hasil = serviceInventaris.keluarStok("PROD001", 15);
// Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).updateStok(anyString(),
                anyInt());
    }

    @Test
    @DisplayName("Hitung total nilai inventaris")
    void testHitungTotalNilaiInventaris() {
// Arrange
        Produk produk1 = new Produk(
                "PROD001",
                "Laptop",
                "Elektronik",
                10000000,
                2,
                1);
        Produk produk2 = new Produk(
                "PROD002",
                "Mouse",
                "Elektronik",
                500000,
                5,
                2);
        Produk produkNonAktif = new Produk(
                "PROD003",
                "Keyboard",
                "Elektronik",
                300000,
                3,
                1);

        produkNonAktif.setAktif(false);
        List<Produk> semuaProduk = Arrays.asList(produk1, produk2,
                produkNonAktif);
        when(mockRepositoryProduk.cariSemua()).thenReturn(semuaProduk);
        // Act
        double totalNilai =
                serviceInventaris.hitungTotalNilaiInventaris();

// Assert
        double expected = (10000000 * 2) + (500000 * 5); // hanya produk aktif
        assertEquals(expected, totalNilai, 0.001);
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Get produk stok menipis")
    void testGetProdukStokMenipis() {
// Arrange
        Produk produkStokAman = new Produk(
                "PROD001",
                "Laptop",
                "Elektronik",
                10000000,
                10,
                5);
        Produk produkStokMenipis = new Produk(
                "PROD002",
                "Mouse",
                "Elektronik",
                500000,
                3,
                5);
        List<Produk> produkMenipis = Collections.singletonList(produkStokMenipis);
        when(mockRepositoryProduk.cariProdukStokMenipis()).thenReturn(produkMenipis);
// Act
        List<Produk> hasil = serviceInventaris.getProdukStokMenipis();
// Assert
        assertEquals(1, hasil.size());
        assertEquals("PROD002", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokMenipis();
    }

    @Test
    @DisplayName("Tambah produk gagal - produk tidak valid")
    void testTambahProdukGagalProdukTidakValid() {
        Produk produkInvalid = null;
        assertFalse(serviceInventaris.tambahProduk(produkInvalid));
    }

    @Test
    @DisplayName("Hapus produk gagal - kode tidak valid")
    void testHapusProdukGagalKodeTidakValid() {
        boolean hasil = serviceInventaris.hapusProduk("");
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Hapus produk gagal - produk tidak ditemukan")
    void testHapusProdukGagalTidakDitemukan() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        boolean hasil = serviceInventaris.hapusProduk("PROD001");
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Hapus produk gagal - stok masih ada")
    void testHapusProdukGagalStokMasihAda() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        boolean hasil = serviceInventaris.hapusProduk("PROD001");
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Hapus produk berhasil - stok 0")
    void testHapusProdukBerhasil() {
        Produk produkHabis = new Produk("P02","Keyboard","Elektronik",300000,0,1);
        when(mockRepositoryProduk.cariByKode("P02")).thenReturn(Optional.of(produkHabis));
        when(mockRepositoryProduk.hapus("P02")).thenReturn(true);
        assertTrue(serviceInventaris.hapusProduk("P02"));
    }

    @Test
    @DisplayName("Cari produk by kode - kode tidak valid")
    void testCariProdukByKodeInvalid() {
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("");
        assertTrue(hasil.isEmpty());
    }

    @Test
    @DisplayName("Cari produk by kode - valid dan ditemukan")
    void testCariProdukByKodeBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("PROD001");
        assertTrue(hasil.isPresent());
        assertEquals("PROD001", hasil.get().getKode());
    }

    @Test
    @DisplayName("Update stok gagal - stok negatif")
    void testUpdateStokGagalNegatif() {
        boolean hasil = serviceInventaris.updateStok("PROD001", -1);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Update stok gagal - produk tidak ditemukan")
    void testUpdateStokGagalTidakDitemukan() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        boolean hasil = serviceInventaris.updateStok("PROD001", 10);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Update stok berhasil")
    void testUpdateStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 20)).thenReturn(true);
        assertTrue(serviceInventaris.updateStok("PROD001", 20));
    }

    @Test
    @DisplayName("Masuk stok gagal - kode tidak valid")
    void testMasukStokKodeInvalid() {
        assertFalse(serviceInventaris.masukStok("", 5));
    }

    @Test
    @DisplayName("Masuk stok gagal - jumlah <= 0")
    void testMasukStokJumlahInvalid() {
        assertFalse(serviceInventaris.masukStok("PROD001", 0));
    }

    @Test
    @DisplayName("Masuk stok gagal - produk tidak ditemukan")
    void testMasukStokProdukTidakDitemukan() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        assertFalse(serviceInventaris.masukStok("PROD001", 5));
    }

    @Test
    @DisplayName("Masuk stok gagal - produk tidak aktif")
    void testMasukStokProdukTidakAktif() {
        produkTest.setAktif(false);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        assertFalse(serviceInventaris.masukStok("PROD001", 5));
    }

    @Test
    @DisplayName("Masuk stok berhasil")
    void testMasukStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 15)).thenReturn(true);
        assertTrue(serviceInventaris.masukStok("PROD001", 5));
    }

    @Test
    @DisplayName("Get produk stok habis")
    void testGetProdukStokHabis() {
        when(mockRepositoryProduk.cariProdukStokHabis()).thenReturn(Collections.emptyList());
        List<Produk> hasil = serviceInventaris.getProdukStokHabis();
        assertTrue(hasil.isEmpty());
    }

    @Test
    @DisplayName("Hitung total stok hanya produk aktif")
    void testHitungTotalStok() {
        Produk aktif = new Produk("P1", "A", "K1", 1000, 5, 1);
        Produk nonAktif = new Produk("P2", "B", "K2", 2000, 10, 1);
        nonAktif.setAktif(false);
        when(mockRepositoryProduk.cariSemua()).thenReturn(Arrays.asList(aktif, nonAktif));
        assertEquals(5, serviceInventaris.hitungTotalStok());
    }

    @Test
    @DisplayName("Cari produk berdasarkan nama berhasil")
    void testCariProdukByNamaBerhasil() {
        List<Produk> produkList = List.of(new Produk("P01", "Keyboard", "Elektronik", 300000, 10, 2));
        when(mockRepositoryProduk.cariByNama("Keyboard")).thenReturn(produkList);

        List<Produk> hasil = serviceInventaris.cariProdukByNama("Keyboard");

        assertEquals(1, hasil.size());
        assertEquals("P01", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariByNama("Keyboard");
    }

    @Test
    @DisplayName("Cari produk berdasarkan kategori berhasil")
    void testCariProdukByKategoriBerhasil() {
        List<Produk> produkList = List.of(new Produk("P02", "Mouse", "Elektronik", 200000, 5, 3));
        when(mockRepositoryProduk.cariByKategori("Elektronik")).thenReturn(produkList);

        List<Produk> hasil = serviceInventaris.cariProdukByKategori("Elektronik");

        assertEquals(1, hasil.size());
        assertEquals("Mouse", hasil.get(0).getNama());
        verify(mockRepositoryProduk).cariByKategori("Elektronik");
    }

    @Test
    @DisplayName("Keluar stok gagal - kode produk tidak valid")
    void testKeluarStokKodeTidakValid() {
        boolean hasil = serviceInventaris.keluarStok("!", 5);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Keluar stok gagal - jumlah tidak valid")
    void testKeluarStokJumlahTidakValid() {
        boolean hasil = serviceInventaris.keluarStok("P01", 0);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Keluar stok gagal - produk tidak ditemukan")
    void testKeluarStokProdukTidakDitemukan() {
        when(mockRepositoryProduk.cariByKode("P01")).thenReturn(Optional.empty());
        boolean hasil = serviceInventaris.keluarStok("P01", 5);
        assertFalse(hasil);
    }

    @Test
    @DisplayName("Keluar stok gagal - produk tidak aktif")
    void testKeluarStokProdukTidakAktif() {
        Produk produk = new Produk("P01", "Keyboard", "Elektronik", 300000, 10, 5);
        produk.setAktif(false);
        when(mockRepositoryProduk.cariByKode("P01")).thenReturn(Optional.of(produk));

        boolean hasil = serviceInventaris.keluarStok("P01", 5);

        assertFalse(hasil);
    }

    @Test
    @DisplayName("Keluar stok gagal - stok tidak mencukupi")
    void testKeluarStokStokTidakCukup() {
        Produk produk = new Produk("P01", "Keyboard", "Elektronik", 300000, 3, 5);
        when(mockRepositoryProduk.cariByKode("P01")).thenReturn(Optional.of(produk));

        boolean hasil = serviceInventaris.keluarStok("P01", 5);

        assertFalse(hasil);
    }


}