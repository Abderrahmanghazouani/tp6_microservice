package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.AbstractDao;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class FemmeService extends AbstractDao<Femme> {

    public FemmeService() {
        super(Femme.class);
    }

    /** Requête native nommée : nombre d'enfants d'une femme pour les mariages commencés entre deux dates. */
    public int nombreEnfantsEntreDates(Femme femme, LocalDate debut, LocalDate fin) {
        try (Session session = openSession()) {
            Object resultat = session.getNamedNativeQuery("Femme.nombreEnfantsEntreDates")
                    .setParameter("femmeId", femme.getId())
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .uniqueResult();
            return ((Number) resultat).intValue(); // SUM renvoie un BigDecimal avec MySQL
        }
    }

    /** Requête nommée : femmes mariées au moins deux fois. */
    public List<Femme> findMarieesAuMoinsDeuxFois() {
        try (Session session = openSession()) {
            return session.createNamedQuery("Femme.marieesAuMoinsDeuxFois", Femme.class).list();
        }
    }

    /** Femme la plus âgée (date de naissance la plus ancienne). */
    public Femme findPlusAgee() {
        try (Session session = openSession()) {
            return session.createQuery("from Femme f order by f.dateNaissance asc", Femme.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }
}
