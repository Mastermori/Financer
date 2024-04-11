package de.moritzjunge.financer.model;

public enum Role {

    USER("user", "ROLE_USER"), ADMIN("administrator", "ROLE_ADMIN");

    private final String description;
    private final String authority;

    Role(String description, String authority) {
        this.description = description;
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
