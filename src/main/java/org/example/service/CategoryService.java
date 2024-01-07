package org.example.service;

import org.example.model.Category;

import java.util.List;

public interface CategoryService {
    boolean addCategory(Category category);

    boolean updateCategory(Category category);

    boolean deleteCategory(int id);

    List<Category> showCategory();

    Category findCategoryById(int id);
}
