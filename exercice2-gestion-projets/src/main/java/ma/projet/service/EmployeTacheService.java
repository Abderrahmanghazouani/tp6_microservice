package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.dao.AbstractDao;

public class EmployeTacheService extends AbstractDao<EmployeTache> {
    public EmployeTacheService() {
        super(EmployeTache.class);
    }
}
