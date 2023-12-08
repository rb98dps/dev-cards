package com.devapi.services;

import com.devapi.dao.Dao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<S,T> {

    Dao<S,T> dao;
    @Autowired
    public GenericServiceImpl(Dao<S,T> dao) {
        this.dao = dao;
    }


    /*public StudentServiceImpl() {
        dao = new Dao<>();
        dao.setClazz(Student.class);
    }*/



    @Transactional
    public S save(S entity) {
        return dao.save(entity);
    }


    @Transactional
    public S update(S entity) {
        return dao.save(entity);
    }


    public Optional<S> findById(T id) {
        System.out.println(dao);
        return Optional.ofNullable(dao.findById(id));
    }


    public List<S> findAll() {
        return dao.findAll();
    }


    @Transactional
    public boolean deleteById(T id) {
        Optional<S> entity = Optional.ofNullable(dao.findById(id));
        System.out.println(entity);
        entity.ifPresent(value -> dao.delete(value));
        return true;
    }
}
