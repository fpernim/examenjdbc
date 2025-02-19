package perez.nimo.francisco.jdbc.hardcoded.dao;

import perez.nimo.francisco.jdbc.hardcoded.model.Task;

public class TaskDao extends AbstractDao<Task, Integer> {
    private static final String INSERT_SQL = "INSERT INTO task (task_id, name) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE task SET name = ? WHERE task_id = ?";
    private static final String DELETE_SQL = "DELETE FROM task WHERE task_id = ?";
    private static final String GET_BY_ID_SQL = "SELECT * FROM task WHERE task_id = ?";

    public TaskDao() {
        super(Task.class, "taskId", INSERT_SQL, UPDATE_SQL, DELETE_SQL, GET_BY_ID_SQL, "task");
    }
}
