package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    String sqlCreate = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                lastName VARCHAR(50) NOT NULL,
                age TINYINT NOT NULL
            )
            """;
    String sqlDrop = """
            DROP TABLE IF EXISTS users
            """;
    String sqlSave = """
            INSERT INTO users (name, lastName, age)
            VALUES (?, ?, ?)
            """;
    String sqlDelete = """
            DELETE FROM users WHERE id = ?
            """;
    String sqlSelect = """
            SELECT * FROM users
            """;
    String sqlClean = """
            TRUNCATE TABLE users
            """;


    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlCreate);
                connection.commit();
                System.out.println("Таблица users создана");
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Ошибка при создании таблицы: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при установлении соединения: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlDrop);
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Ошибка при удалении таблицы" + e);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к БД: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement prStatement = connection.prepareStatement(sqlSave)) {
                prStatement.setString(1, name);
                prStatement.setString(2, lastName);
                prStatement.setByte(3, age);
                prStatement.executeUpdate();

                connection.commit();
                System.out.println("User с именем — " + name + " добавлен в базу данных");

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Ошибка при добавлении пользователя: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при установлении соединения: " + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement prStatement = connection.prepareStatement(sqlDelete)) {
                prStatement.setLong(1, id);
                prStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к БД: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlSelect)) {

                while (resultSet.next()) {
                    User user = new User(
                            resultSet.getString("name"),
                            resultSet.getString("lastName"),
                            resultSet.getByte("age")
                    );
                    users.add(user);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Ошибка при получении пользователей: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к БД: " + e.getMessage());
        }
        return users;
    }


    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlClean);
                connection.commit();
                System.out.println("Таблица users очищена");
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Ошибка при очистке таблицы: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к БД: " + e.getMessage());
        }
    }
}
