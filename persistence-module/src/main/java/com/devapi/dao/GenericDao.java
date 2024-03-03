package com.devapi.dao;

import java.util.List;

interface GenericDao<S, T> {
    S save(S entity);

    List<S> findAll();

    S findById(T id);

    void delete(S entity);
}
