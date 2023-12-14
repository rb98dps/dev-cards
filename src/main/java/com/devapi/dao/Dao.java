package com.devapi.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class Dao<S, T> implements GenericDao<S, T> {
    EntityManager entityManager;

    private Class<S> clazz;

    public Dao() {
    }

    public Dao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public final void setClazz(final Class<S> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public S save(S entity) {
        return entityManager.merge(entity);
    }


    public List<S> findAll() {
        return entityManager.createQuery("from " + clazz.getName(), clazz).getResultList();
    }

    @Override
    public S findById(T id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public void delete(S entity) {
        entityManager.remove(entity);
    }

    public void deleteById(final T entityId) {
        final S entity = findById(entityId);
        delete(entity);
    }

    public boolean checkEntity(String id, String value) {
        CriteriaQuery<S> criteriaQuery = getCriteriaForSingleAttribute(id, value);
        return !entityManager.createQuery(criteriaQuery).getResultList().isEmpty();
    }

    public List<S> getEntity(String id, String value) {
        CriteriaQuery<S> criteriaQuery = getCriteriaForSingleAttribute(id, value);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private CriteriaQuery<S> getCriteriaForSingleAttribute(String id, String value) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<S> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<S> root = criteriaQuery.from(clazz);
        Predicate condition = criteriaBuilder.equal(root.get(id), value);
        criteriaQuery.where(condition);
        return criteriaQuery;
    }
}
