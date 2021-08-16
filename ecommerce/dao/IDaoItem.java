package org.generation.italy.ecommerce.dao;

import java.util.List;

import org.generation.italy.ecommerce.model.Item;

public interface IDaoItem {

	List<Item> getItems();
	List<Item> getItems(int typeId);
	Item getItem(int id);
	int addItem(Item type, int typeId);
	boolean updateItem(Item type, int typeId);
	boolean deleteItem(int id);
	
}
