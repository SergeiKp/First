package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Jon", "Shelby", (byte) 19);
        userService.saveUser("Harry", "Li", (byte) 30);
        userService.saveUser("Simon", "Strong", (byte) 22);
        userService.saveUser("Sam", "Grey", (byte) 33);

        userService.removeUserById(1);
        System.out.println("Список пользователей:");
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
