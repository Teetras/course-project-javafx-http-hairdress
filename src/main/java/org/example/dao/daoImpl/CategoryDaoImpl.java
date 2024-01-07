package org.example.dao.daoImpl;


import org.example.dao.CategoryDao;
import org.example.model.Category;
import org.example.sessionFactory.SessionFactoryImpl;
import org.example.utils.SessionUtils;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    @Override
    public boolean addCategory(Category category){
        System.out.println(category+"    CategoryDaoImpl");

        return SessionUtils.saveEntity(category);
    }

    @Override
    public boolean updateCategory(Category category){

        return SessionUtils.updateEntity(category);
    }

    @Override
    public  boolean deleteCategory(int id){
        return SessionUtils.deleteEntity(id, Category.class);
    }

    @Override
    public  List<Category> showCategory(){
        System.out.println((List<Category>) SessionFactoryImpl.getSessionFactory()
                .openSession()
                .createQuery("FROM Category")
                .list()+" LIST");
        return (List<Category>) SessionFactoryImpl.getSessionFactory()
                .openSession()
                .createQuery("FROM Category")
                .list();
    }//есть некоторые вопросы к from category

    @Override
    public  Category findCategoryById(int id){
        return SessionUtils.find(Category.class, id, "category_id");
    }
}
