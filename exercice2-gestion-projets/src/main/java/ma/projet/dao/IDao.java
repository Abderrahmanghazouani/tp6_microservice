package ma.projet.dao;

import java.util.List;

/**
 * Interface générique : contrat CRUD commun à toutes les entités.
 */
public interface IDao<T> {
    boolean create(T o);
    boolean update(T o);
    boolean delete(T o);
    T findById(int id);
    List<T> findAll();
}
