package com.pmu.pmudemo.domains;

public enum Role {
    USER("USER"),
    AGENT("AGENT"),
    ADMIN("ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rôle invalide: " + value);
    }
} 