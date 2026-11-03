package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;

public class TestEtatCivil {

    public static void main(String[] args) {
        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        // ---------- 1. Créer 10 femmes et 5 hommes
        Femme salima = new Femme("RAMI", "SALIMA", "0611111101", "Rabat", LocalDate.of(1970, 5, 12));
        Femme amal = new Femme("ALI", "AMAL", "0611111102", "Rabat", LocalDate.of(1974, 2, 3));
        Femme wafa = new Femme("ALAOUI", "WAFA", "0611111103", "Salé", LocalDate.of(1978, 8, 20));
        Femme karima = new Femme("ALAMI", "KARIMA", "0611111104", "Témara", LocalDate.of(1967, 11, 9));
        Femme nadia = new Femme("BERRADA", "NADIA", "0611111105", "Fès", LocalDate.of(1980, 1, 15));
        Femme hind = new Femme("TAZI", "HIND", "0611111106", "Fès", LocalDate.of(1982, 6, 30));
        Femme loubna = new Femme("CHRAIBI", "LOUBNA", "0611111107", "Meknès", LocalDate.of(1985, 3, 22));
        Femme samira = new Femme("IDRISSI", "SAMIRA", "0611111108", "Kénitra", LocalDate.of(1965, 7, 1));
        Femme fatima = new Femme("BENNANI", "FATIMA", "0611111109", "Casablanca", LocalDate.of(1960, 12, 25));
        Femme imane = new Femme("FASSI", "IMANE", "0611111110", "Casablanca", LocalDate.of(1990, 9, 9));
        Femme[] femmes = {salima, amal, wafa, karima, nadia, hind, loubna, samira, fatima, imane};
        for (Femme f : femmes) femmeService.create(f);

        Homme said = new Homme("SAFI", "SAID", "0622222201", "Rabat", LocalDate.of(1965, 4, 10));
        Homme omar = new Homme("KADIRI", "OMAR", "0622222202", "Fès", LocalDate.of(1972, 10, 5));
        Homme youssef = new Homme("AMRANI", "YOUSSEF", "0622222203", "Casablanca", LocalDate.of(1968, 1, 18));
        Homme hamid = new Homme("BOUZIDI", "HAMID", "0622222204", "Meknès", LocalDate.of(1975, 3, 3));
        Homme rachid = new Homme("LAHLOU", "RACHID", "0622222205", "Kénitra", LocalDate.of(1988, 12, 12));
        Homme[] hommes = {said, omar, youssef, hamid, rachid};
        for (Homme h : hommes) hommeService.create(h);

        // Mariages de SAFI SAID (exemple de l'énoncé) : 3 en cours + 1 échoué
        mariageService.create(new Mariage(said, karima, LocalDate.of(1989, 9, 3), LocalDate.of(1990, 9, 3), 0));
        mariageService.create(new Mariage(said, salima, LocalDate.of(1990, 9, 3), null, 4));
        mariageService.create(new Mariage(said, amal, LocalDate.of(1995, 9, 3), null, 2));
        mariageService.create(new Mariage(said, wafa, LocalDate.of(2000, 11, 4), null, 3));
        // Autres mariages
        mariageService.create(new Mariage(omar, karima, LocalDate.of(1992, 6, 1), null, 2));   // KARIMA : 2e mariage
        mariageService.create(new Mariage(omar, nadia, LocalDate.of(2001, 4, 14), null, 1));
        mariageService.create(new Mariage(youssef, samira, LocalDate.of(1988, 7, 7), LocalDate.of(1995, 1, 1), 1));
        mariageService.create(new Mariage(hamid, samira, LocalDate.of(1997, 5, 5), null, 2)); // SAMIRA : 2e mariage
        mariageService.create(new Mariage(hamid, hind, LocalDate.of(2005, 2, 2), null, 3));
        mariageService.create(new Mariage(rachid, imane, LocalDate.of(2015, 8, 8), null, 1));
        mariageService.create(new Mariage(youssef, fatima, LocalDate.of(1996, 3, 3), null, 5));
        mariageService.create(new Mariage(rachid, loubna, LocalDate.of(2010, 10, 10), LocalDate.of(2013, 1, 1), 0));

        // ---------- 2. Liste des femmes
        System.out.println("\n===== Liste des femmes =====");
        femmeService.findAll().forEach(System.out::println);

        // ---------- 3. Femme la plus âgée
        System.out.println("\n===== Femme la plus âgée =====");
        System.out.println(femmeService.findPlusAgee());

        // ---------- 4. Épouses d'un homme entre deux dates
        LocalDate d1 = LocalDate.of(1989, 1, 1), d2 = LocalDate.of(2000, 12, 31);
        System.out.println("\n===== Épouses de " + said.getNomComplet() + " entre " + d1 + " et " + d2 + " =====");
        hommeService.findEpousesEntreDates(said, d1, d2).forEach(System.out::println);

        // ---------- 5. Nombre d'enfants d'une femme entre deux dates (requête native nommée)
        System.out.println("\n===== Nombre d'enfants de " + karima.getNomComplet()
                + " entre 1985-01-01 et 2000-12-31 (requête native nommée) =====");
        System.out.println(femmeService.nombreEnfantsEntreDates(karima,
                LocalDate.of(1985, 1, 1), LocalDate.of(2000, 12, 31)) + " enfant(s)");

        // ---------- 6. Femmes mariées au moins deux fois (requête nommée)
        System.out.println("\n===== Femmes mariées deux fois ou plus (requête nommée) =====");
        femmeService.findMarieesAuMoinsDeuxFois().forEach(System.out::println);

        // ---------- 7. Hommes mariés à quatre femmes entre deux dates (API Criteria)
        System.out.println("\n===== Nombre d'hommes mariés à 4 femmes entre 1985-01-01 et 2005-12-31 (Criteria) =====");
        System.out.println(hommeService.nombreHommesMariesAQuatreFemmes(
                LocalDate.of(1985, 1, 1), LocalDate.of(2005, 12, 31)) + " homme(s)");

        // ---------- 8. Mariages d'un homme avec tous les détails
        System.out.println("\n===== Mariages de " + said.getNomComplet() + " =====");
        hommeService.afficherMariages(said);

        HibernateUtil.getSessionFactory().close();
    }
}
