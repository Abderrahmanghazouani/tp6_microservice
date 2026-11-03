package ma.projet.util;

import ma.projet.beans.Homme;
import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

/**
 * Crée une seule SessionFactory pour toute l'application,
 * à partir du fichier application.properties.
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try (InputStream in = HibernateUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(in);

            Configuration configuration = new Configuration().addProperties(props);
            configuration.addAnnotatedClass(Homme.class);
            configuration.addAnnotatedClass(Femme.class);
            configuration.addAnnotatedClass(Mariage.class);
            return configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Échec de la création de la SessionFactory : " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
