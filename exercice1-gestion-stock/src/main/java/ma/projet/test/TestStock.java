package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;

public class TestStock {

    public static void main(String[] args) {
        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        LigneCommandeService ligneService = new LigneCommandeService();

        // ---------- Données de test
        Categorie ecrans = new Categorie("CAT1", "Écrans");
        Categorie stockage = new Categorie("CAT2", "Stockage");
        categorieService.create(ecrans);
        categorieService.create(stockage);

        Produit es12 = new Produit("ES12", 120, ecrans);
        Produit zr85 = new Produit("ZR85", 100, stockage);
        Produit ee85 = new Produit("EE85", 200, ecrans);
        Produit ab10 = new Produit("AB10", 80, stockage);
        produitService.create(es12);
        produitService.create(zr85);
        produitService.create(ee85);
        produitService.create(ab10);

        Commande c1 = new Commande(LocalDate.of(2013, 1, 10));
        Commande c2 = new Commande(LocalDate.of(2013, 2, 5));
        Commande c3 = new Commande(LocalDate.of(2013, 3, 1));
        Commande c4 = new Commande(LocalDate.of(2013, 3, 14));
        commandeService.create(c1);
        commandeService.create(c2);
        commandeService.create(c3);
        commandeService.create(c4);

        ligneService.create(new LigneCommande(c1, ab10, 3));
        ligneService.create(new LigneCommande(c2, zr85, 2));
        ligneService.create(new LigneCommande(c3, ab10, 10));
        ligneService.create(new LigneCommande(c4, es12, 7));
        ligneService.create(new LigneCommande(c4, zr85, 14));
        ligneService.create(new LigneCommande(c4, ee85, 5));

        // ---------- 1. Produits par catégorie
        System.out.println("\n===== 1. Produits de la catégorie « " + ecrans.getLibelle() + " » =====");
        produitService.findByCategorie(ecrans).forEach(System.out::println);

        // ---------- 2. Produits commandés entre deux dates
        LocalDate debut = LocalDate.of(2013, 2, 1), fin = LocalDate.of(2013, 3, 1);
        System.out.println("\n===== 2. Produits commandés entre " + debut + " et " + fin + " =====");
        produitService.findProduitsCommandesEntre(debut, fin).forEach(System.out::println);

        // ---------- 3. Produits d'une commande donnée
        System.out.println("\n===== 3. Produits de la commande n°" + c4.getId() + " =====");
        produitService.afficherProduitsCommande(commandeService.findById(c4.getId()));

        // ---------- 4. Requête nommée : prix > 100 DH
        System.out.println("\n===== 4. Produits dont le prix est supérieur à 100 DH (requête nommée) =====");
        produitService.findPrixSuperieur(100).forEach(System.out::println);

        HibernateUtil.getSessionFactory().close();
    }
}
