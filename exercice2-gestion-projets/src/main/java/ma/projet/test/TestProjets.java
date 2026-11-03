package ma.projet.test;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;

public class TestProjets {

    public static void main(String[] args) {
        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService etService = new EmployeTacheService();

        // ---------- Données de test
        Employe chef = new Employe("Alami", "Karim", "0600000001");
        Employe dev = new Employe("Bennani", "Sara", "0600000002");
        employeService.create(chef);
        employeService.create(dev);

        Projet stock = new Projet("Gestion de stock", LocalDate.of(2013, 1, 14), LocalDate.of(2013, 6, 30), chef);
        Projet rh = new Projet("Gestion RH", LocalDate.of(2013, 9, 1), LocalDate.of(2013, 12, 31), chef);
        projetService.create(stock);
        projetService.create(rh);

        Tache analyse = new Tache("Analyse", LocalDate.of(2013, 2, 1), LocalDate.of(2013, 2, 15), 800, stock);
        Tache conception = new Tache("Conception", LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 15), 1500, stock);
        Tache developpement = new Tache("Développement", LocalDate.of(2013, 4, 1), LocalDate.of(2013, 4, 30), 4000, stock);
        Tache cahier = new Tache("Cahier charges", LocalDate.of(2013, 9, 1), LocalDate.of(2013, 9, 10), 600, rh);
        tacheService.create(analyse);
        tacheService.create(conception);
        tacheService.create(developpement);
        tacheService.create(cahier);

        etService.create(new EmployeTache(chef, analyse, LocalDate.of(2013, 2, 10), LocalDate.of(2013, 2, 20)));
        etService.create(new EmployeTache(dev, conception, LocalDate.of(2013, 3, 10), LocalDate.of(2013, 3, 15)));
        etService.create(new EmployeTache(dev, developpement, LocalDate.of(2013, 4, 10), LocalDate.of(2013, 4, 25)));

        // ---------- EmployeService
        System.out.println("\n===== Tâches réalisées par " + dev + " =====");
        employeService.findTachesRealisees(dev).forEach(System.out::println);

        System.out.println("\n===== Projets gérés par " + chef + " =====");
        employeService.findProjetsGeres(chef).forEach(System.out::println);

        // ---------- ProjetService
        System.out.println("\n===== Tâches planifiées pour le projet « " + stock.getNom() + " » =====");
        projetService.findTachesPlanifiees(stock).forEach(System.out::println);

        System.out.println("\n===== Tâches réalisées avec les dates réelles =====");
        projetService.afficherTachesRealisees(projetService.findById(stock.getId()));

        // ---------- TacheService
        System.out.println("\n===== Tâches dont le prix est supérieur à 1000 DH (requête nommée) =====");
        tacheService.findPrixSuperieur(1000).forEach(System.out::println);

        LocalDate debut = LocalDate.of(2013, 3, 1), fin = LocalDate.of(2013, 4, 30);
        System.out.println("\n===== Tâches réalisées entre " + debut + " et " + fin + " =====");
        tacheService.findRealiseesEntre(debut, fin).forEach(System.out::println);

        HibernateUtil.getSessionFactory().close();
    }
}
