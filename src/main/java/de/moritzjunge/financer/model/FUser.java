package de.moritzjunge.financer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public final class FUser {
    @Id
    @GeneratedValue
    private final long id;

    private final String name;
    private final String email;
    private final String passwordHash;

    public FUser(long id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    public String passwordHash() {
        return passwordHash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FUser) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.passwordHash, that.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, passwordHash);
    }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "email=" + email + ", " +
                "passwordHash=" + passwordHash + ']';
    }


}
