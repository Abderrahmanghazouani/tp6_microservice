package ma.projet.service;

import ma.projet.classes.Tache;
import ma.projet.dao.AbstractDao;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class TacheService extends AbstractDao<Tache> {

    public TacheService() {
        super(Tache.class);
    }

    /** Tâches dont le prix dépasse un seuil : requête nommée Tache.findPrixSuperieur. */
    public List<Tache> findPrixSuperieur(double prix) {
        try (Session session = openSession()) {
            return session.createNamedQuery("Tache.findPrixSuperieur", Tache.class)
                    .setParameter("prix", prix)
                    .list();
        }
    }

    /** Tâches réalisées entre deux dates (dates réelles comprises dans l'intervalle). */
    public List<Tache> findRealiseesEntre(LocalDate debut, LocalDate fin) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "select distinct et.tache from EmployeTache et " +
                            "where et.dateDebutReelle >= :debut and et.dateFinReelle <= :fin", Tache.class)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }
}
