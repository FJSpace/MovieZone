/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Space
 * @param <T> Type
 * @param <K> Primary key (id)
 */
public abstract class DAO<T, K> implements IDAO<T, K> {
    private final Class<T> clazz;

    // To be overridden by subclasses
    protected abstract EntityManager getEntityManager();

    protected DAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void create(T t) {
        getEntityManager().persist(t);
    }

    @Override
    public void delete(K id) {
        T t = getEntityManager().getReference(clazz, id);
        getEntityManager().remove(t);
    }
    @Override
    public void update(T t) {
        getEntityManager().merge(t);
    }

    @Override
    public T find(K id) {
       return getEntityManager().find(clazz, id);
    }

    @Override
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        List<T> found = new ArrayList<>();
        TypedQuery<T> q = em.createQuery("select t from " + clazz.getSimpleName() + " t", clazz);
        found.addAll(q.getResultList());
        return found;
    }

    @Override
    public List<T> findById(K id) {
        EntityManager em = getEntityManager();
        List<T> found = new ArrayList<>();
        TypedQuery<T> q = em.createQuery(
                "select t from " + clazz.getSimpleName() + 
                        " t where id=:id", clazz);
        found.addAll(q.getResultList());
        return found;
    }
}
