package perez.nimo.francisco.jdbc.dao;

import perez.nimo.francisco.jdbc.model.User;

public class UserDao extends AbstractDao<User, Integer> {
    public UserDao() {
        super(User.class, "userId");
    }
}
