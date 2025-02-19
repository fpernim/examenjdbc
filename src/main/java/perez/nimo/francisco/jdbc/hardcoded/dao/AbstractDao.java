package perez.nimo.francisco.jdbc.hardcoded.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import perez.nimo.francisco.jdbc.DatabaseConfig;

public abstract class AbstractDao<T, ID> {
    protected Connection connection;
    private Class<T> entityClass;
    private String primaryKeyField;
    private String insertSql;
    private String updateSql;
    private String deleteSql;
    private String getByIdSql;
    private String tableName;

    public AbstractDao(Class<T> entityClass, String primaryKeyField, String insertSql, String updateSql, String deleteSql, String getByIdSql,
            String tableName) {
        this.connection = DatabaseConfig.getConnection();
        this.entityClass = entityClass;
        this.primaryKeyField = primaryKeyField;
        this.insertSql = insertSql;
        this.updateSql = updateSql;
        this.deleteSql = deleteSql;
        this.getByIdSql = getByIdSql;
        this.tableName = tableName;
    }

    public void insert(T entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
            setPreparedStatementParameters(stmt, entity, entityClass.getDeclaredFields(), null);
            stmt.executeUpdate();
        } catch (IllegalAccessException e) {
            throw new SQLException("Error accessing entity fields", e);
        }
    }

    public void update(T entity) throws SQLException {
        Field idField = null;
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase(primaryKeyField)) {
                idField = field;
                break;
            }
        }
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            setPreparedStatementParameters(stmt, entity, entityClass.getDeclaredFields(), idField);
            stmt.executeUpdate();
        } catch (IllegalAccessException e) {
            throw new SQLException("Error accessing entity fields", e);
        }
    }

    public void delete(ID id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    public T findById(ID id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getByIdSql)) {
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
        String sql = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        }
        return entities;
    }

    private void setPreparedStatementParameters(PreparedStatement stmt, T entity, Field[] fields, Field idField) throws SQLException, IllegalAccessException {
        int index = 1;
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || field.equals(idField))
                continue;
            field.setAccessible(true);
            stmt.setObject(index++, field.get(entity));
        }
        if (idField != null) {
            idField.setAccessible(true);
            stmt.setObject(index, idField.get(entity));
        }
    }

    private T mapResultSetToEntity(ResultSet rs) throws SQLException {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            for (Field field : entityClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()))
                    continue;
                field.setAccessible(true);
                field.set(entity, rs.getObject(getDatabaseFieldName(field.getName())));
            }
            return entity;
        } catch (Exception e) {
            throw new SQLException("Error mapping ResultSet to entity", e);
        }
    }

    public String getDatabaseFieldName(String fieldName) {
        try {
            return (String) entityClass.getMethod("getDatabaseFieldName", String.class).invoke(null, fieldName);
        } catch (Exception e) {
            return fieldName; // Fallback to default if not found
        }
    }

    // private String getJavaFieldName(String databaseFieldName) {
    // try {
    // return (String) entityClass.getMethod("getJavaFieldName", String.class).invoke(null, databaseFieldName);
    // } catch (Exception e) {
    // return databaseFieldName; // Fallback to default if not found
    // }
    // }
}
