package ma.projet.service;

import ma.projet.classes.LigneCommande;
import ma.projet.dao.AbstractDao;

public class LigneCommandeService extends AbstractDao<LigneCommande> {
    public LigneCommandeService() {
        super(LigneCommande.class);
    }
}
