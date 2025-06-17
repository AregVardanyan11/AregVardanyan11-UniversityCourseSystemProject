package org.example.project.model.enums;

public enum Grade{
    A_PLUS("A+", 4.0),
    A("A", 4.0),
    A_MINUS("A-", 3.7),
    B_PLUS("B+", 3.3),
    B("B", 3.0),
    B_MINUS("B-", 2.7),
    C_PLUS("C+", 2.3),
    C("C", 2.0),
    C_MINUS("C-", 1.7),
    D_PLUS("D+", 1.3),
    D("D", 1.0),
    D_MINUS("D-", 0.7),
    F("F", 0.0);

    private final String label;
    private final double value;

    Grade(String label, double value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public double getValue() {
        return value;
    }

    // Convert a string like "B+" to the corresponding enum
    public static Grade fromString(String input) {
        for (Grade g : Grade.values()) {
            if (g.label.equalsIgnoreCase(input.trim())) {
                return g;
            }
        }
        throw new IllegalArgumentException("Invalid grade: " + input);
    }
}
