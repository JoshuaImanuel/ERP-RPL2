package com.erp.dao;

import com.erp.model.Karyawan;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KaryawanDaoImpl implements KaryawanDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Karyawan karyawan) {
        getCurrentSession().persist(karyawan);
    }

    @Override
    public Karyawan findById(String nip) {
        return getCurrentSession().get(Karyawan.class, nip);
    }

    @Override
    public List<Karyawan> findAll() {
        return getCurrentSession().createQuery("from Karyawan", Karyawan.class).list();
    }

    @Override
    public void update(Karyawan karyawan) {
        getCurrentSession().merge(karyawan);
    }

    @Override
    public void delete(String nip) {
        Karyawan karyawan = findById(nip);
        if (karyawan != null) {
            getCurrentSession().remove(karyawan);
        }
    }
}