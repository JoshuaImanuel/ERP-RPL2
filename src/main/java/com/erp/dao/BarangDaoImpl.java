package com.erp.dao;

import com.erp.model.Barang;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BarangDaoImpl implements BarangDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Barang barang) {
        getCurrentSession().persist(barang);
    }

    @Override
    public Barang findById(String kodeBarang) {
        return getCurrentSession().get(Barang.class, kodeBarang);
    }

    @Override
    public List<Barang> findAll() {
        return getCurrentSession().createQuery("from Barang", Barang.class).list();
    }

    @Override
    public void update(Barang barang) {
        getCurrentSession().merge(barang);
    }

    @Override
    public void delete(String kodeBarang) {
        Barang barang = findById(kodeBarang);
        if (barang != null) {
            getCurrentSession().remove(barang);
        }
    }
}