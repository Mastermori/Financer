package de.moritzjunge.financer.model.dtos;

import jakarta.validation.constraints.NotEmpty;

public class CategoryDTO {

    @NotEmpty
    private String description;

    public @NotEmpty String getDescription() {
        return description;
    }

    public CategoryDTO setDescription(@NotEmpty String description) {
        this.description = description;
        return this;
    }
}
