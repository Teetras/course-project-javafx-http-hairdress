package org.example.service.serviceImpl;

import org.example.dao.daoImpl.CategoryDaoImpl;
import org.example.model.Category;
import org.example.service.CategoryService;
import org.hibernate.HibernateError;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    org.example.dao.CategoryDao CategoryDao=new CategoryDaoImpl();
    @Override
    public boolean addCategory(Category category){
        System.out.println(category+"CategoryServiceImpl");
        boolean isAdded = false;
        try {
            if (CategoryDao.addCategory(category))
                isAdded = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isAdded;
    }

    @Override
    public boolean updateCategory(Category category){
        boolean isUpdated = false;
        try {
            if (CategoryDao.updateCategory(category))
                isUpdated = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteCategory(int id){
        boolean isDeleted = false;
        try {
            if (CategoryDao.deleteCategory(id))
                isDeleted = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isDeleted;
    }


    @Override
    public List<Category> showCategory(){
        List<Category> categories = null;
        try {
            categories = CategoryDao.showCategory();
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        System.out.println("@Override\n" +
                "    public List<Category> showCategory()"+ categories);
        return categories;
    }

    @Override
    public Category findCategoryById(int id){
        Category category = null;
        try {
            category = CategoryDao.findCategoryById(id);
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return category;
    }

}
