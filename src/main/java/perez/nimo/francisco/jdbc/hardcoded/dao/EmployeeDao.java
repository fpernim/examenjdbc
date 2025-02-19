package perez.nimo.francisco.jdbc.hardcoded.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import perez.nimo.francisco.jdbc.hardcoded.model.Employee;

public class EmployeeDao extends AbstractDao<Employee, Integer> {
    private static final String INSERT_SQL = "INSERT INTO employee (employee_id, dni) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE employee SET name = ? WHERE employee_id = ?";
    private static final String DELETE_SQL = "DELETE FROM employee WHERE employee_id = ?";
    private static final String GET_BY_ID_SQL = "SELECT * FROM employee WHERE employee_id = ?";

    public EmployeeDao() {
        super(Employee.class, "employeeId", INSERT_SQL, UPDATE_SQL, DELETE_SQL, GET_BY_ID_SQL, "employee");
    }

    public void addEmpleadoToDepartamento(int empleadoId, int departamentoId) throws SQLException {
        String sql = "UPDATE " + getDatabaseFieldName("Empleado") + " SET " + getDatabaseFieldName("departamentoId") + " = ? WHERE "
                + getDatabaseFieldName("empleadoId") + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, departamentoId);
            stmt.setInt(2, empleadoId);
            stmt.executeUpdate();
        }
    }
}