package perez.nimo.francisco.jdbc.hardcoded.dao;

import java.sql.SQLException;
import java.util.List;

import perez.nimo.francisco.jdbc.hardcoded.model.Department;
import perez.nimo.francisco.jdbc.hardcoded.model.Employee;
import perez.nimo.francisco.jdbc.hardcoded.model.Project;
import perez.nimo.francisco.jdbc.hardcoded.model.Task;

public class EmployeeDao extends AbstractDao<Employee, Integer> {
    private static final String INSERT_SQL = "INSERT INTO employee (employee_id, dni) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE employee SET name = ? WHERE employee_id = ?";
    private static final String DELETE_SQL = "DELETE FROM employee WHERE employee_id = ?";
    private static final String GET_BY_ID_SQL = "SELECT * FROM employee WHERE employee_id = ?";

    public EmployeeDao() {
        super(Employee.class, "employeeId", INSERT_SQL, UPDATE_SQL, DELETE_SQL, GET_BY_ID_SQL, "employee");
    }

    public void assignTask(Employee employee, Task task) throws SQLException {
        addOneToOne("employee", "task_id", employee.getEmployeeId(), task.getTaskId());
    }

    public void unassignTask(Employee employee) throws SQLException {
        removeOneToOne("employee", "task_id", employee.getEmployeeId());
    }

    public void assignProject(Employee employee, Project project) throws SQLException {
        addManyToMany("project_has_employee", "employee_id", "project_id", employee.getEmployeeId(), project.getProjectId());
    }

    public void unassignProject(Employee employee, Project project) throws SQLException {
        removeManyToMany("project_has_employee", "employee_id", "project_id", employee.getEmployeeId(), project.getProjectId());
    }

    public void assignDepartment(Employee employee, Department department) throws SQLException {
        addOneToMany("employee", "department_id", employee.getEmployeeId(), department.getDepartmentId());
    }

    public void unassignDepartment(Employee employee) throws SQLException {
        removeOneToMany("employee", "department_id", employee.getEmployeeId());
    }

    public Task getTask(Employee employee) throws SQLException {
        return getOneToOne("employee", "task_id", employee.getEmployeeId(), Task.class, "taskId");
    }

    public List<Project> getProjects(Employee employee) throws SQLException {
        return getManyToMany("project_has_employee", "employee_id", "project_id", employee.getEmployeeId(), Project.class, "projectId");
    }

    public Department getDepartment(Employee employee) throws SQLException {
        return getOneToOne("employee", "department_id", employee.getEmployeeId(), Department.class, "departmentId");
    }
}