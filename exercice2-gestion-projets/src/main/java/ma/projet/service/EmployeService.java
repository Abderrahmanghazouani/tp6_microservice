package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.AbstractDao;
import org.hibernate.Session;

import java.util.List;

public class EmployeService extends AbstractDao<Employe> {

    public EmployeService() {
        super(Employe.class);
    }

    /** Tâches réalisées par un employé. */
    public List<Tache> findTachesRealisees(Employe employe) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "select et.tache from EmployeTache et " +
                            "where et.employe.id = :id order by et.dateDebutReelle", Tache.class)
                    .setParameter("id", employe.getId())
                    .list();
        }
    }

    /** Projets gérés par un employé (il en est le chef). */
    public List<Projet> findProjetsGeres(Employe employe) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "from Projet p where p.chefProjet.id = :id order by p.dateDebut", Projet.class)
                    .setParameter("id", employe.getId())
                    .list();
        }
    }
}
