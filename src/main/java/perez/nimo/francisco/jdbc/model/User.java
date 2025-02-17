package perez.nimo.francisco.jdbc.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int userId;
    private String name;
    private String email;

    public static final Map<String, String> FIELD_MAPPINGS = new HashMap<>();

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    static {
        FIELD_MAPPINGS.put("userId", "user_id");
        FIELD_MAPPINGS.put("name", "name");
        FIELD_MAPPINGS.put("email", "email");
    }

    public static String getDatabaseFieldName(String javaFieldName) {
        return FIELD_MAPPINGS.getOrDefault(javaFieldName, javaFieldName);
    }

    public static String getJavaFieldName(String databaseFieldName) {
        for (Map.Entry<String, String> entry : FIELD_MAPPINGS.entrySet()) {
            if (entry.getValue().equals(databaseFieldName)) {
                return entry.getKey();
            }
        }
        return databaseFieldName; // Fallback to default if not found
    }

    // Constructores, getters y setters
}
