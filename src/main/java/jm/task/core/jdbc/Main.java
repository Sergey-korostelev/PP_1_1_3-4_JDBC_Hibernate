package jm.task.core.jdbc;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        UserDaoHibernateImpl userDaoHibernate = new UserDaoHibernateImpl();

        userDaoHibernate.createUsersTable();

        userDaoHibernate.saveUser("Nike", "Petrov", (byte) 5);
        userDaoHibernate.saveUser("Mihail", "Pypkin", (byte) 3);
        userDaoHibernate.saveUser("Serg", "Gorelov", (byte) 100);
        userDaoHibernate.saveUser("Saha", "Ivanova", (byte) 30);

        List<User> list = userDaoHibernate.getAllUsers();
        for (User user : list) {
            System.out.println(user.toString());
        }
        userDaoHibernate.cleanUsersTable();

        userDaoHibernate.dropUsersTable();
    }
}
