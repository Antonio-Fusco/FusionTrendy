package org.generation.italy.ecommerce.dao;

import java.util.List;

import org.generation.italy.ecommerce.model.Category;


public interface IDaoCategory {
	List<Category>getCategories();
	Category getCategory(int id);
	int addCategory(Category c);
	boolean updateCategory(Category c);
	boolean deleteCategory(int id);

}
