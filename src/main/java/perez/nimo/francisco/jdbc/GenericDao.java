package perez.nimo.francisco.jdbc;
    
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T, ID> {
    protected Connection connection;
    private Class<T> entityClass;

    public GenericDao(Connection connection, Class<T> entityClass) {
        this.connection = connection;
        this.entityClass = entityClass;
    }

    
    public void insert(T entity) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(entityClass.getSimpleName().toLowerCase()).append(" (");
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            sql.append(field.getName()).append(", ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            sql.append("?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                stmt.setObject(i + 1, fields[i].get(entity));
            }
            stmt.executeUpdate();
        } catch (IllegalAccessException e) {
            throw new SQLException("Error accessing entity fields", e);
        }
    }

    
    public void update(T entity) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(entityClass.getSimpleName().toLowerCase()).append(" SET ");
        Field[] fields = entityClass.getDeclaredFields();
        Field idField = null;
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("id")) {
                idField = field;
                continue;
            }
            sql.append(field.getName()).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Field field : fields) {
                if (field.equals(idField))
                    continue;
                field.setAccessible(true);
                stmt.setObject(index++, field.get(entity));
            }
            idField.setAccessible(true);
            stmt.setObject(index, idField.get(entity));
            stmt.executeUpdate();
        } catch (IllegalAccessException e) {
            throw new SQLException("Error accessing entity fields", e);
        }
    }

    
    public void delete(ID id) throws SQLException {
        String sql = "DELETE FROM " + entityClass.getSimpleName().toLowerCase() + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    
    public T findById(ID id) throws SQLException {
        String sql = "SELECT * FROM " + entityClass.getSimpleName().toLowerCase() + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        return null;
    }

    
    public List<T> findAll() throws SQLException {
        List<T> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + entityClass.getSimpleName().toLowerCase();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        }
        return entities;
    }

    private T mapResultSetToEntity(ResultSet rs) throws SQLException {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            for (Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(entity, rs.getObject(field.getName()));
            }
            return entity;
        } catch (Exception e) {
            throw new SQLException("Error mapping ResultSet to entity", e);
        }
    }
}

}
