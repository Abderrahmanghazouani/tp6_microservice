package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_commande")
    private LocalDate date;

    @OneToMany(mappedBy = "commande")
    private List<LigneCommande> lignes = new ArrayList<>();

    public Commande() {}

    public Commande(LocalDate date) {
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<LigneCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }
}
