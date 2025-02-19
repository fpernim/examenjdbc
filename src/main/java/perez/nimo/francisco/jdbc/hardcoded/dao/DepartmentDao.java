package perez.nimo.francisco.jdbc.hardcoded.dao;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import perez.nimo.francisco.jdbc.hardcoded.model.Department;
import perez.nimo.francisco.jdbc.hardcoded.model.Employee;

public class DepartmentDao extends AbstractDao<Department, Integer> {
    private static final String INSERT_SQL = "INSERT INTO department (department_id, name) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE department SET name = ? WHERE department_id = ?";
    private static final String DELETE_SQL = "DELETE FROM department WHERE department_id = ?";
    private static final String GET_BY_ID_SQL = "SELECT * FROM department WHERE department_id = ?";

    public DepartmentDao() {
        super(Department.class, "departmentId", INSERT_SQL, UPDATE_SQL, DELETE_SQL, GET_BY_ID_SQL, "department");
    }

    public List<Employee> getEmployees(int departmentId) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM " + getDatabaseFieldName("Employee") + " WHERE " + getDatabaseFieldName("departamentoId") + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee();
                    for (Field field : Employee.class.getDeclaredFields()) {
                        field.setAccessible(true);
                        field.set(employee, rs.getObject(getDatabaseFieldName(field.getName())));
                    }
                    employees.add(employee);
                }
            } catch (IllegalAccessException e) {
                throw new SQLException("Error accessing entity fields", e);
            }
        }
        return employees;
    }
}
