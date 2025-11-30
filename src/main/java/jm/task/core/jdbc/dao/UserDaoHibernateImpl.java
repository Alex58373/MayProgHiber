package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoHibernateImpl.class);

    public UserDaoHibernateImpl() {
        Util.getSessionFactory();
    }

    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                String sql = """
                        CREATE TABLE IF NOT EXISTS user (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY, 
                        name VARCHAR(100),  
                        lastName VARCHAR(100), 
                        age TINYINT) 
                        """;
                session.createNativeQuery(sql).executeUpdate();
                transaction.commit();
                logger.info("Таблица user создана");
            } catch (Exception e) {
                try {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                } catch (Exception re) {
                    logger.error("Ошибка при откате транзакции: " + re.getMessage());
                }
                logger.error("Ошибка при создании таблицы: ", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Ошибка при работе с сессией: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                String sql = "DROP TABLE IF EXISTS user";
                session.createNativeQuery(sql).executeUpdate();
                transaction.commit();
                logger.info("Таблица user удалена");
            } catch (Exception e) {
                try {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                } catch (Exception re) {
                    logger.error("Ошибка при откате транзакции: " + re.getMessage());
                }
                logger.error("Ошибка при удалении таблицы: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Ошибка при работе с сессией: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = new User(name, lastName, age);
                session.save(user);
                transaction.commit();
                logger.info("Пользователь: " + name + " " + lastName + "  сохранен.");
            } catch (Exception e) {
                try {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                } catch (Exception re) {
                    logger.error("Ошибка при откате транзакции: " + re.getMessage());
                }
                logger.error("Ошибка при сохранении пользователя: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Ошибка при работе с сессией: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                String sql = "DELETE FROM user WHERE id = :userId";
                int clearUser = session.createNativeQuery(sql)
                        .setParameter("userId", id)
                        .executeUpdate();
                transaction.commit();
                logger.info("Удалено пользователей: " + clearUser);
            } catch (Exception e) {
                try {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                } catch (Exception re) {
                    logger.error("Ошибка при откате транзакции: " + re.getMessage());
                }
                logger.error("Ошибка при удалении пользователя: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Ошибка при работе с сессией: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        String sql = "SELECT * FROM user";
        try (Session session = getSessionFactory().openSession()) {
            users = session
                    .createNativeQuery(sql, User.class)
                    .getResultList();
            logger.info("Получено пользователей: " + users.size());
        } catch (Exception e) {
            logger.error("Ошибка при получении пользователей: " + e.getMessage());
            return new ArrayList<>();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            String sql = "TRUNCATE TABLE user";
            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
            logger.info("Таблица user очищена");
        } catch (Exception e) {
            logger.error("Ошибка при очистке таблицы: " + e.getMessage());
        }
    }
}
