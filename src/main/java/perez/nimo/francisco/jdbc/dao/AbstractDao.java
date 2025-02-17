package perez.nimo.francisco.jdbc.dao;

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

    public AbstractDao(Class<T> entityClass, String primaryKeyField) {
        this.connection = DatabaseConfig.getConnection();
        this.entityClass = entityClass;
        this.primaryKeyField = primaryKeyField;
    }

    private String getDatabaseColumnName(String fieldName) {
        return getDatabaseFieldName(fieldName);
    }

    public void insert(T entity) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(getDatabaseColumnName(entityClass.getSimpleName())).append(" (");
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()))
                continue; // Skip static fields
            sql.append(getDatabaseColumnName(field.getName())).append(", ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            if (Modifier.isStatic(fields[i].getModifiers()))
                continue; // Skip static fields
            sql.append("?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()))
                    continue; // Skip static fields
                field.setAccessible(true);
                stmt.setObject(index++, field.get(entity));
            }
            stmt.executeUpdate();
        } catch (IllegalAccessException e) {
            throw new SQLException("Error accessing entity fields", e);
        }
    }

    public void update(T entity) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(getDatabaseFieldName(entityClass.getSimpleName())).append(" SET ");
        Field[] fields = entityClass.getDeclaredFields();
        Field idField = null;
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()))
                continue; // Skip static fields
            if (field.getName().equalsIgnoreCase(primaryKeyField)) {
                idField = field;
                continue;
            }
            sql.append(getDatabaseFieldName(field.getName())).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE ").append(getDatabaseFieldName(primaryKeyField)).append(" = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()))
                    continue; // Skip static fields
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
        String sql = "DELETE FROM " + getDatabaseFieldName(entityClass.getSimpleName()) + " WHERE " + getDatabaseFieldName(primaryKeyField) + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    public T findById(ID id) throws SQLException {
        String sql = "SELECT * FROM " + getDatabaseFieldName(entityClass.getSimpleName()) + " WHERE " + getDatabaseFieldName(primaryKeyField) + " = ?";
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
        String sql = "SELECT * FROM " + getDatabaseFieldName(entityClass.getSimpleName());
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
                if (Modifier.isStatic(field.getModifiers()))
                    continue; // Skip static fields
                field.setAccessible(true);
                field.set(entity, rs.getObject(getDatabaseFieldName(field.getName())));
            }
            return entity;
        } catch (Exception e) {
            throw new SQLException("Error mapping ResultSet to entity", e);
        }
    }

    private String getDatabaseFieldName(String fieldName) {
        try {
            return (String) entityClass.getMethod("getDatabaseFieldName", String.class).invoke(null, fieldName);
        } catch (Exception e) {
            return fieldName; // Fallback to default if not found
        }
    }

    private String getJavaFieldName(String databaseFieldName) {
        try {
            return (String) entityClass.getMethod("getJavaFieldName", String.class).invoke(null, databaseFieldName);
        } catch (Exception e) {
            return databaseFieldName; // Fallback to default if not found
        }
    }
}
