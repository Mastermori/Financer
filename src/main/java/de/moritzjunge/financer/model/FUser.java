package de.moritzjunge.financer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public final class FUser {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    @OneToMany(mappedBy = "owner")
    private Set<Transaction> ownedTransactions = new HashSet<>();

    @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER)
    private Set<Household> participatingHouseholds = new HashSet<>();

    @Column(nullable = false)
    private Role role;

    // Required by Hibernate
    public FUser() {
    }

    public FUser(Long id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public Set<Household> getParticipatingHouseholds() {
        return participatingHouseholds;
    }

    public Set<Transaction> getOwnedTransactions() {
        return ownedTransactions;
    }

    public FUser setName(String name) {
        this.name = name;
        return this;
    }

    public FUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public FUser setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public FUser setRole(Role role) {
        this.role = role;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FUser user = (FUser) o;
        return Objects.equals(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
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
