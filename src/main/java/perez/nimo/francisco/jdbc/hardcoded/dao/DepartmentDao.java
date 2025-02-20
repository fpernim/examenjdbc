package perez.nimo.francisco.jdbc.hardcoded.dao;

import java.sql.SQLException;
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

    public List<Employee> getEmployees(Department department) throws SQLException {
        return getOneToMany("employee", "department_id", department.getDepartmentId(), Employee.class, "employeeId");
    }
}