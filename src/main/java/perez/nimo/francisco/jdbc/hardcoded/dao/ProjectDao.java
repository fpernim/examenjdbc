package perez.nimo.francisco.jdbc.hardcoded.dao;

import java.sql.SQLException;
import java.util.List;

import perez.nimo.francisco.jdbc.hardcoded.model.Employee;
import perez.nimo.francisco.jdbc.hardcoded.model.Project;

public class ProjectDao extends AbstractDao<Project, Integer> {
    private static final String INSERT_SQL = "INSERT INTO project (project_id, description) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE project SET description = ? WHERE project_id = ?";
    private static final String DELETE_SQL = "DELETE FROM project WHERE project_id = ?";
    private static final String GET_BY_ID_SQL = "SELECT * FROM project WHERE project_id = ?";

    public ProjectDao() {
        super(Project.class, "projectId", INSERT_SQL, UPDATE_SQL, DELETE_SQL, GET_BY_ID_SQL, "project");
    }

    public List<Employee> getEmployees(Project project) throws SQLException {
        return getManyToMany("project_has_employee", "project_id", "employee_id", project.getProjectId(), Employee.class, "employeeId");
    }
}