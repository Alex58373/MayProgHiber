package jm.task.core.jdbc.util;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.*;
import java.util.logging.Logger;
import jm.task.core.jdbc.model.User;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String URL = "jdbc:mysql://localhost:3306/task_jdbc";
    private static final String USER = "root";
    private static final String PASSWORD = "кщще1212";
    private static final Logger logger = Logger.getLogger(Util.class.getName());
    private static  SessionFactory sessionFactory;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    public static SessionFactory  getSessionFactory() {
        if (sessionFactory == null){
            try {
                Configuration configuration = new Configuration();
                configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                configuration.setProperty("hibernate.connection.url", URL);
                configuration.setProperty("hibernate.connection.username", USER);
                configuration.setProperty("hibernate.connection.password", PASSWORD);
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
                configuration.setProperty("hibernate.show_sql", "true");
                configuration.setProperty("hibernate.format_sql", "true");
                configuration.setProperty("hibernate.hbm2ddl.auto", "update");

                configuration.addAnnotatedClass(User.class);
                sessionFactory = configuration.buildSessionFactory();
                logger.info("SessionFactory успешно создан");

            } catch (Exception e) {
                logger.severe("Ошибка при создании SessionFactory: " + e.getMessage());
                throw new RuntimeException("Ошибка инициализации Hibernate", e);
            }
        }
        return sessionFactory;
    } // Подключение к базе данных при помощи hibernate
    public static Connection getConnection() {
        try {
            logger.info("Подключение к MySQL: " + URL);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к MySQL", e);
        }
    }
}
