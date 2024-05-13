package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.Connect;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = Connect();

    public void createUsersTable() {
        try (Statement stat = connection.createStatement()) {
            connection.setAutoCommit(false);
            dropUsersTable();
            stat.executeUpdate("CREATE TABLE `task1`.`users` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(100) NOT NULL,\n" +
                    "  `lastName` VARCHAR(100) NOT NULL,\n" +
                    "  `age` VARCHAR(3) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;");
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.rollback();
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dropUsersTable() {
        try (Statement stat = connection.createStatement()) {
            connection.setAutoCommit(false);
            ResultSet set = stat.executeQuery("SHOW TABLES LIKE 'users'");
            if (set.next()) {
                stat.executeUpdate("DROP TABLE `task1`.`users`;");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.rollback();
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        final String sql = "INSERT INTO users (name, lastName,age) VALUES(?, ?, ?)";
        try (PreparedStatement pState = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            pState.setString(1, name);
            pState.setString(2, lastName);
            pState.setByte(3, age);
            pState.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.rollback();
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void removeUserById(long id) {
        final String sql = "DELETE FROM users WHERE ID=?";
        try (PreparedStatement pStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            pStatement.setLong(1, id);
            pStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.rollback();
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        final String sql = "SELECT ID ,NAME, LASTNAME, AGE FROM users";
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setAge(Byte.valueOf(resultSet.getString("AGE")));
                list.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.rollback();
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    public void cleanUsersTable() {
        List<Long> list = new ArrayList<>();
        final String sql = "SELECT ID  FROM users";
        final String sql2 = "DELETE FROM users WHERE ID=?";
        try (Statement statement = connection.createStatement();
             PreparedStatement pStatement = Connect().prepareStatement(sql2)) {
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(resultSet.getLong("ID"));
            }
            for (Long x : list) {
                pStatement.setLong(1, x);
                pStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.rollback();
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
