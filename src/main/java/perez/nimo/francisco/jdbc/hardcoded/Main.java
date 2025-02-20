package perez.nimo.francisco.jdbc.hardcoded;

import java.sql.SQLException;
import java.util.List;

import perez.nimo.francisco.jdbc.hardcoded.dao.DepartmentDao;
import perez.nimo.francisco.jdbc.hardcoded.dao.EmployeeDao;
import perez.nimo.francisco.jdbc.hardcoded.dao.ProjectDao;
import perez.nimo.francisco.jdbc.hardcoded.dao.TaskDao;
import perez.nimo.francisco.jdbc.hardcoded.model.Department;
import perez.nimo.francisco.jdbc.hardcoded.model.Employee;
import perez.nimo.francisco.jdbc.hardcoded.model.Project;
import perez.nimo.francisco.jdbc.hardcoded.model.Task;

public class Main {
    public static void main(String[] args) {
        EmployeeDao employeeDao = new EmployeeDao();
        TaskDao taskDao = new TaskDao();
        ProjectDao projectDao = new ProjectDao();
        DepartmentDao departmentDao = new DepartmentDao();

        try {
            // Insert a new employee
            Employee employee = new Employee("12345678A");
            employeeDao.insert(employee);
            System.out.println("Inserted employee: " + employee);
            employee = employeeDao.findById(1);

            // Assign a task to the employee
            Task task = new Task("Task Description");
            taskDao.insert(task);
            task = taskDao.findById(1);
            employeeDao.assignTask(employee, task);
            System.out.println("Assigned task to employee: " + task);

            // Get the task assigned to the employee
            Task assignedTask = employeeDao.getTask(employee);
            System.out.println("Task assigned to employee: " + assignedTask.getName());

            // Assign a project to the employee
            Project project = new Project("Project Description");
            projectDao.insert(project);
            project = projectDao.findById(1);
            employeeDao.assignProject(employee, project);
            System.out.println("Assigned project to employee: " + project);

            // Get the projects assigned to the employee
            List<Project> projects = employeeDao.getProjects(employee);
            for (Project p : projects) {
                System.out.println("Projects assigned to employee: " + p.getDescription());
            }

            // Assign a department to the employee
            Department department = new Department("Department Name");
            departmentDao.insert(department);
            department = departmentDao.findById(1);
            employeeDao.assignDepartment(employee, department);
            System.out.println("Assigned department to employee: " + department);

            // Get the department assigned to the employee
            Department assignedDepartment = employeeDao.getDepartment(employee);
            System.out.println("Department assigned to employee: " + assignedDepartment.getName());

            System.out.println(taskDao.getEmployee(task).getDni());

            for (Employee e : projectDao.getEmployees(project)) {
                System.out.println(e.getDni());
            }

            for (Employee e : departmentDao.getEmployees(department)) {
                System.out.println(e.getDni());
            }

            // Unassign the task from the employee
            employeeDao.unassignTask(employee);
            System.out.println("Unassigned task from employee");

            // Unassign the project from the employee
            employeeDao.unassignProject(employee, project);
            System.out.println("Unassigned project from employee");

            // Unassign the department from the employee
            employeeDao.unassignDepartment(employee);
            System.out.println("Unassigned department from employee");

            employeeDao.delete(1);
            taskDao.delete(1);
            projectDao.delete(1);
            departmentDao.delete(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}