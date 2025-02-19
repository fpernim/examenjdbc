package perez.nimo.francisco.jdbc.hardcoded.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Department {
    private int departmentId;
    private String name;
    private List<Employee> employees; // Relación uno a muchos

    public static final Map<String, String> FIELD_MAPPINGS = new HashMap<>();

    public Department(String name) {
        this.name = name;
    }

    static {
        FIELD_MAPPINGS.put("departmentId", "department_id");
        FIELD_MAPPINGS.put("name", "name");
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

}
