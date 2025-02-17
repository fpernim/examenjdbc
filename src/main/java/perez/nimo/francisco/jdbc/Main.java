package perez.nimo.francisco.jdbc;

import java.sql.SQLException;

import perez.nimo.francisco.jdbc.dao.UserDao;
import perez.nimo.francisco.jdbc.model.User;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        try {
            userDao.insert(new User("Fran", "email"));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}