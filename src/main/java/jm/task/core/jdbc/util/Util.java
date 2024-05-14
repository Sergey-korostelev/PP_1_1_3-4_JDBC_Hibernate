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
    final static String driver = "com.mysql.cj.jdbc.Driver";
    final static String urlTask2 = "jdbc:mysql://localhost:3306/task2?useSSL=false";
    final static String user = "Coloredkor";
    final static String pass = "root";
    final static String dialect = "org.hibernate.dialect.MySQL5Dialect";
    final static String show = "true";
    final static String currentSession = "thread";

    public static Connection Connect() {
        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            System.out.println("Connection failed...");
            throw new RuntimeException(e);
        }
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/task1",
                    user, pass);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory ConnectHibernate() {
        try {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();
            settings.put(Environment.DRIVER, driver);
            settings.put(Environment.URL, urlTask2);
            settings.put(Environment.USER, user);
            settings.put(Environment.PASS, pass);
            settings.put(Environment.DIALECT, dialect);
            settings.put(Environment.SHOW_SQL, show);
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, currentSession);
            settings.put(Environment.HBM2DDL_AUTO, "");
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
