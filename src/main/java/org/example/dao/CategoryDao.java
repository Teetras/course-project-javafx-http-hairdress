package org.example.dao;

import org.example.model.Category;

import java.util.List;

public interface CategoryDao {
    boolean addCategory(Category category);

    boolean updateCategory(Category category);

    boolean deleteCategory(int id);

    List<Category> showCategory();

    Category findCategoryById(int id);
}
