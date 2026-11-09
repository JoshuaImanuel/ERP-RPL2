package com.erp.dao;

import com.erp.model.Barang;
import java.util.List;

public interface BarangDao {
    void save(Barang barang);
    Barang findById(String kodeBarang);
    List<Barang> findAll();
    void update(Barang barang);
    void delete(String kodeBarang);
}