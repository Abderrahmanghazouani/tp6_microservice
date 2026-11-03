package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.AbstractDao;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HommeService extends AbstractDao<Homme> {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public HommeService() {
        super(Homme.class);
    }

    /** Épouses d'un homme dont le mariage a commencé entre deux dates. */
    public List<Femme> findEpousesEntreDates(Homme homme, LocalDate debut, LocalDate fin) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "select m.femme from Mariage m where m.homme.id = :id " +
                            "and m.dateDebut between :debut and :fin order by m.dateDebut", Femme.class)
                    .setParameter("id", homme.getId())
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }

    /**
     * API Criteria : nombre d'hommes mariés à quatre femmes (mariages commencés entre deux dates).
     * Équivalent HQL : select m.homme.id from Mariage m where m.dateDebut between :d1 and :d2
     *                  group by m.homme.id having count(distinct m.femme.id) = 4
     */
    public long nombreHommesMariesAQuatreFemmes(LocalDate debut, LocalDate fin) {
        try (Session session = openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Mariage> m = cq.from(Mariage.class);

            cq.select(m.<Homme>get("homme").<Integer>get("id"))
              .where(cb.between(m.get("dateDebut"), debut, fin))
              .groupBy(m.get("homme").get("id"))
              .having(cb.equal(cb.countDistinct(m.get("femme").get("id")), 4L));

            return session.createQuery(cq).getResultList().size();
        }
    }

    /** Mariages d'un homme avec les détails. */
    public List<Mariage> findMariages(Homme homme) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "from Mariage m where m.homme.id = :id order by m.dateDebut", Mariage.class)
                    .setParameter("id", homme.getId())
                    .list();
        }
    }

    /** Affiche les mariages d'un homme au format demandé par l'énoncé. */
    public void afficherMariages(Homme homme) {
        List<Mariage> mariages = findMariages(homme);

        System.out.println("Nom : " + homme.getNomComplet());
        System.out.println("Mariages En Cours :");
        int i = 1;
        for (Mariage m : mariages) {
            if (m.isEnCours()) {
                System.out.printf("%d. Femme : %-14s Date Début : %s    Nbr Enfants : %d%n",
                        i++, m.getFemme().getPrenomNom(), m.getDateDebut().format(FORMAT), m.getNbrEnfant());
            }
        }

        System.out.println();
        System.out.println("Mariages échoués :");
        i = 1;
        for (Mariage m : mariages) {
            if (!m.isEnCours()) {
                System.out.printf("%d. Femme : %-14s Date Début : %s%n   Date Fin : %s    Nbr Enfants : %d%n",
                        i++, m.getFemme().getPrenomNom(), m.getDateDebut().format(FORMAT),
                        m.getDateFin().format(FORMAT), m.getNbrEnfant());
            }
        }
    }
}
