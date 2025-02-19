package perez.nimo.francisco.jdbc.hardcoded;

import java.sql.SQLException;

import perez.nimo.francisco.jdbc.hardcoded.dao.EmployeeDao;
import perez.nimo.francisco.jdbc.hardcoded.model.Employee;

public class Main {
    public static void main(String[] args) {
        EmployeeDao userDao = new EmployeeDao();
        try {
            userDao.insert(new Employee("dni"));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}