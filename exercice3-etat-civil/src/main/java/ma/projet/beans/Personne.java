package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Attributs communs à Homme et Femme.
 * @MappedSuperclass : pas de table « personne », les colonnes sont recopiées dans chaque table fille.
 */
@MappedSuperclass
public abstract class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    protected Personne() {}

    protected Personne(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    /** Nom complet en majuscules, ex. « SAFI SAID ». */
    public String getNomComplet() {
        return (nom + " " + prenom).toUpperCase();
    }

    /** Prénom puis nom en majuscules, ex. « SALIMA RAMI ». */
    public String getPrenomNom() {
        return (prenom + " " + nom).toUpperCase();
    }

    @Override
    public String toString() {
        return getNomComplet() + " (né(e) le " + dateNaissance + ")";
    }
}
