package ma.projet.dao;

import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;

/**
 * Implémentation générique de IDao avec Hibernate.
 * Chaque service hérite de cette classe et n'écrit que ses méthodes spécifiques.
 */
public abstract class AbstractDao<T> implements IDao<T> {

    private final Class<T> type;

    protected AbstractDao(Class<T> type) {
        this.type = type;
    }

    protected Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    /** Exécute une écriture dans une transaction : commit si tout va bien, rollback sinon. */
    private boolean inTransaction(Consumer<Session> work) {
        Transaction tx = null;
        try (Session session = openSession()) {
            tx = session.beginTransaction();
            work.accept(session);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean create(T o) {
        return inTransaction(s -> s.save(o));
    }

    @Override
    public boolean update(T o) {
        return inTransaction(s -> s.update(o));
    }

    @Override
    public boolean delete(T o) {
        return inTransaction(s -> s.delete(o));
    }

    @Override
    public T findById(int id) {
        try (Session session = openSession()) {
            return session.get(type, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = openSession()) {
            return session.createQuery("from " + type.getSimpleName(), type).list();
        }
    }
}
