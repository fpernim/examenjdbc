package perez.nimo.francisco.jdbc.hardcoded.dao;

import perez.nimo.francisco.jdbc.hardcoded.model.User;

public class UserDao extends AbstractDao<User, Integer> {
    private static final String INSERT_SQL = "INSERT INTO user (user_id, name, email) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE user SET name = ?, email = ? WHERE user_id = ?";
    private static final String DELETE_SQL = "DELETE FROM user WHERE user_id = ?";
    private static final String GET_BY_ID_SQL = "SELECT * FROM user WHERE user_id = ?";

    public UserDao() {
        super(User.class, "userId", INSERT_SQL, UPDATE_SQL, DELETE_SQL, GET_BY_ID_SQL, "user");
    }
}