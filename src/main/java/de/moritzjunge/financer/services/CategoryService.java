package de.moritzjunge.financer.services;

import de.moritzjunge.financer.model.Category;
import de.moritzjunge.financer.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(Category newCategory) {
        categoryRepository.save(newCategory);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public boolean categoryExists(Long id) {
        return categoryRepository.existsById(id);
    }


}
