package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.AbstractDao;
import org.hibernate.Session;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ProjetService extends AbstractDao<Projet> {

    private static final DateTimeFormatter LONG = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    private static final DateTimeFormatter COURT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProjetService() {
        super(Projet.class);
    }

    /** Tâches planifiées pour un projet. */
    public List<Tache> findTachesPlanifiees(Projet projet) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "from Tache t where t.projet.id = :id order by t.dateDebut", Tache.class)
                    .setParameter("id", projet.getId())
                    .list();
        }
    }

    /** Tâches réalisées d'un projet, avec leurs dates réelles. */
    public List<EmployeTache> findTachesRealisees(Projet projet) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "from EmployeTache et where et.tache.projet.id = :id " +
                            "order by et.dateDebutReelle", EmployeTache.class)
                    .setParameter("id", projet.getId())
                    .list();
        }
    }

    /** Affiche les tâches réalisées au format demandé par l'énoncé. */
    public void afficherTachesRealisees(Projet projet) {
        String date = projet.getDateDebut().format(LONG);
        date = date.substring(0, 3) + Character.toUpperCase(date.charAt(3)) + date.substring(4);

        System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom()
                + "     Date début : " + date);
        System.out.println("Liste des tâches:");
        System.out.printf("%-4s%-15s%-20s%s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : findTachesRealisees(projet)) {
            System.out.printf("%-4d%-15s%-20s%s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    et.getDateDebutReelle().format(COURT),
                    et.getDateFinReelle().format(COURT));
        }
    }
}
