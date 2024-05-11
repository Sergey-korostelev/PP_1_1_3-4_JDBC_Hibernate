package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.Connect;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        Statement stat = null;
        try {
            stat = Connect().createStatement();
            dropUsersTable();
            stat.executeUpdate("CREATE TABLE `task1`.`users` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(100) NOT NULL,\n" +
                    "  `lastName` VARCHAR(100) NOT NULL,\n" +
                    "  `age` VARCHAR(3) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dropUsersTable() {
        Statement stat = null;
        try {
            stat = Connect().createStatement();
            ResultSet set = stat.executeQuery("SHOW TABLES LIKE 'users'");
            if (set.next()) {
                stat.executeUpdate("DROP TABLE `task1`.`users`;");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        PreparedStatement pState = null;
        String sql = "INSERT INTO users (name, lastName,age) VALUES(?, ?, ?)";
        try {
            pState = Connect().prepareStatement(sql);
            pState.setString(1, name);
            pState.setString(2, lastName);
            pState.setByte(3, age);
            pState.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pState != null) {
                    pState.close();
                }
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void removeUserById(long id) {
        PreparedStatement pStatement = null;
        String sql = "DELETE FROM users WHERE ID=?";
        try {
            pStatement = Connect().prepareStatement(sql);
            pStatement.setLong(1, id);
            pStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
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
        String sql = "SELECT ID ,NAME, LASTNAME, AGE FROM users";
        Statement statement = null;
        try {
            statement = Connect().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setAge(Byte.valueOf(resultSet.getString("AGE")));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
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
        String sql = "SELECT ID  FROM users";
        String sql2 = "DELETE FROM users WHERE ID=?";
        Statement statement = null;
        PreparedStatement pStatement = null;
        try {
            statement = Connect().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(resultSet.getLong("ID"));
            }

            pStatement = Connect().prepareStatement(sql2);
            for (Long x : list) {
                pStatement.setLong(1, x);
                pStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (Connect() != null) {
                    Connect().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
