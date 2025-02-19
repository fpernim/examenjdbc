package perez.nimo.francisco.jdbc.hardcoded.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Employee {
    private int employeeId;
    private String dni;

    public static final Map<String, String> FIELD_MAPPINGS = new HashMap<>();

    public Employee(String dni) {
        this.dni = dni;
    }

    static {
        FIELD_MAPPINGS.put("employeeId", "employee_id");
        FIELD_MAPPINGS.put("dni", "dni");
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
