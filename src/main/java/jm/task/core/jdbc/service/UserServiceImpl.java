package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    private final UserDao userDao1 = new UserDaoHibernateImpl();

    public void createUsersTable() {
        userDao1.createUsersTable();
    }

    public void dropUsersTable() {
        userDao1.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        userDao1.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        userDao1.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return userDao1.getAllUsers();
    }

    public void cleanUsersTable() {
        userDao1.cleanUsersTable();
    }
}
