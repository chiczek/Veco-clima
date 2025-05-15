package pl.vecoclima;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import pl.vecoclima.data.entity.*;

import java.util.Properties;



public class HibernateConnection {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
                settings.put(Environment.URL, "jdbc:sqlserver://LOCALHOST\\SQLEXPRESS:1433;databaseName=Testowa;encrypt=true;trustServerCertificate=true");
                settings.put(Environment.USER, "maciek");
                settings.put(Environment.PASS, "maciek");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.SQLServer2012Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                //settings.put(Environment.HBM2DDL_AUTO, "none");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(Person.class);
                configuration.addAnnotatedClass(ShoppingCart.class);
                configuration.addAnnotatedClass(ShoppingCartLine.class);
                configuration.addAnnotatedClass(Order.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        try {
            System.out.println("Hibernate shut");
            getSessionFactory().close();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
