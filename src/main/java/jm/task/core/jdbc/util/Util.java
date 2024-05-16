package jm.task.core.jdbc.util;

import java.util.Properties;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static SessionFactory sessionFactory;
    final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    final static String URLTASK2 = "jdbc:mysql://localhost:3306/task2?useSSL=false";
    final static String USER = "Coloredkor";
    final static String PASS = "root";
    final static String DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    final static String SHOW = "true";
    final static String CURRENT_SESSION = "thread";

    public static Connection Connect() {
        try {
            Class.forName(DRIVER).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            System.out.println("Connection failed...");
            throw new RuntimeException(e);
        }
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/task1",
                    USER, PASS);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory ConnectHibernate() {
        try {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();
            settings.put(Environment.DRIVER, DRIVER);
            settings.put(Environment.URL, URLTASK2);
            settings.put(Environment.USER, USER);
            settings.put(Environment.PASS, PASS);
            settings.put(Environment.DIALECT, DIALECT);
            settings.put(Environment.SHOW_SQL, SHOW);
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, CURRENT_SESSION);
            settings.put(Environment.HBM2DDL_AUTO, "none");
            configuration.setProperties(settings);
            configuration.addAnnotatedClass(User.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }
}
