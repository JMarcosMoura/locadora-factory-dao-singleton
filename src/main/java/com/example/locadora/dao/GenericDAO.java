package com.example.locadora.dao;

import com.example.locadora.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericDAO<T> {
    private final Class<T> clazz;

    public GenericDAO(Class<T> clazz) { this.clazz = clazz; }

    public void save(T entity) { 
        executeInsideTransaction(em -> em.persist(entity)); 
    }

    public T update(T entity) {
        return executeInsideTransactionWithResult(em -> em.merge(entity));
    }

    public void delete(Object id) {
        executeInsideTransaction(em -> {
            T ref = em.find(clazz, id);
            if (ref != null) em.remove(ref);
        });
    }

    public T find(Object id) {
        EntityManager em = JPAUtil.getEntityManager();
        try { return em.find(clazz, id); }
        finally { em.close(); }
    }

    public List<T> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<T> q = em.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz);
            return q.getResultList();
        }
        finally { em.close(); }
    }

    protected void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    protected <R> R executeInsideTransactionWithResult(Function<EntityManager, R> fn) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = fn.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }
}
