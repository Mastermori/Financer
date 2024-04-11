package de.moritzjunge.financer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.awt.Color;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private Color color;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "householdId", nullable = false)
    private Household household;

    // Required by Hibernate
    public Category() {
    }

    public Long getId() {
        return id;
    }

    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Category setColor(Color color) {
        this.color = color;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Category setDescription(String description) {
        this.description = description;
        return this;
    }

    public Household getHousehold() {
        return household;
    }

    public Category setHousehold(Household household) {
        this.household = household;
        return this;
    }
}
