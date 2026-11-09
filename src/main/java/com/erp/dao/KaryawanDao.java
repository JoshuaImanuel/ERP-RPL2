package com.erp.dao;

import com.erp.model.Karyawan;
import java.util.List;

public interface KaryawanDao {
    void save(Karyawan karyawan);
    Karyawan findById(String nip);
    List<Karyawan> findAll();
    void update(Karyawan karyawan);
    void delete(String nip);
}