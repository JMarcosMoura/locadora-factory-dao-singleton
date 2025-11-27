package com.example.locadora.dao;

import com.example.locadora.model.Veiculo;
import com.example.locadora.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class VeiculoDAO extends GenericDAO<Veiculo> {

    public VeiculoDAO() { super(Veiculo.class); }

    public List<Veiculo> findDisponiveis() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Veiculo> q = em.createQuery(
                "SELECT v FROM Veiculo v WHERE v.disponivel = true", Veiculo.class
            );
            return q.getResultList();
        }
        finally { em.close(); }
    }

    public Veiculo findByPlaca(String placa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Veiculo> q = em.createQuery(
                "SELECT v FROM Veiculo v WHERE v.placa = :p", Veiculo.class
            );
            q.setParameter("p", placa);
            List<Veiculo> list = q.getResultList();
            return list.isEmpty() ? null : list.get(0);
        }
        finally { em.close(); }
    }
}
