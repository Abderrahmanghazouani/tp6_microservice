package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;
import ma.projet.classes.Produit;
import ma.projet.dao.AbstractDao;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ProduitService extends AbstractDao<Produit> {

    private static final DateTimeFormatter FORMAT_DATE =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);

    public ProduitService() {
        super(Produit.class);
    }

    /** Liste des produits d'une catégorie. */
    public List<Produit> findByCategorie(Categorie categorie) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "from Produit p where p.categorie.id = :id order by p.reference", Produit.class)
                    .setParameter("id", categorie.getId())
                    .list();
        }
    }

    /** Produits commandés entre deux dates (incluses). */
    public List<Produit> findProduitsCommandesEntre(LocalDate debut, LocalDate fin) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "select distinct lc.produit from LigneCommande lc " +
                            "where lc.commande.date between :debut and :fin", Produit.class)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }

    /** Lignes (produit + quantité) d'une commande donnée. */
    public List<LigneCommande> findLignesDeCommande(Commande commande) {
        try (Session session = openSession()) {
            return session.createQuery(
                            "from LigneCommande lc where lc.commande.id = :id order by lc.id", LigneCommande.class)
                    .setParameter("id", commande.getId())
                    .list();
        }
    }

    /** Affiche les produits d'une commande au format demandé par l'énoncé. */
    public void afficherProduitsCommande(Commande commande) {
        String date = commande.getDate().format(FORMAT_DATE);
        date = date.substring(0, 3) + Character.toUpperCase(date.charAt(3)) + date.substring(4); // 14 Mars 2013

        System.out.println("Commande : " + commande.getId() + "     Date : " + date);
        System.out.println("Liste des produits :");
        System.out.printf("%-11s %-7s %s%n", "Référence", "Prix", "Quantité");
        for (LigneCommande lc : findLignesDeCommande(commande)) {
            System.out.printf("%-11s %-7s %d%n",
                    lc.getProduit().getReference(),
                    String.format("%.0f DH", lc.getProduit().getPrix()),
                    lc.getQuantite());
        }
    }

    /** Produits dont le prix est supérieur à un seuil : requête nommée Produit.findPrixSuperieur. */
    public List<Produit> findPrixSuperieur(float prix) {
        try (Session session = openSession()) {
            return session.createNamedQuery("Produit.findPrixSuperieur", Produit.class)
                    .setParameter("prix", prix)
                    .list();
        }
    }
}
