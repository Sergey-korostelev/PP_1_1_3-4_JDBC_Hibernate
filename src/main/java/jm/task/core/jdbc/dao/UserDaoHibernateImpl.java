package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.sql.ResultSet;
import java.util.List;

import static jm.task.core.jdbc.util.Util.ConnectHibernate;


public class UserDaoHibernateImpl implements UserDao {

    @Override
    public void createUsersTable() {
        dropUsersTable();
        try (SessionFactory factory = ConnectHibernate();
             Session session = factory.getCurrentSession()) {
            final String sql = "CREATE TABLE `task2`.`users` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(100) NOT NULL,\n" +
                    "  `lastname` VARCHAR(100) NOT NULL,\n" +
                    "  `age` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8\n" +
                    "COLLATE = utf8_danish_ci;";
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery(sql).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (SessionFactory factory = ConnectHibernate();
             Session session = factory.getCurrentSession()) {
            final String sql = "DROP TABLE IF EXISTS `task2`.`users`;";
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery(sql).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (SessionFactory factory = ConnectHibernate();
             Session session = factory.getCurrentSession()) {
            User user = new User(name, lastName, age);
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (SessionFactory factory = ConnectHibernate();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            User removeUser = session.get(User.class, id);
            session.delete(removeUser);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList;
        try (SessionFactory factory = ConnectHibernate();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            userList = session.createQuery("from User").getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (SessionFactory factory = ConnectHibernate();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}
