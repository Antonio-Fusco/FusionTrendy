package org.generation.italy.ecommerce.dao;

import java.util.List;

import org.generation.italy.ecommerce.model.ItemType;

public interface IDaoItemTypes {

	List<ItemType> getItemTypes();
	ItemType getItemType(int id);
	int addItemType(ItemType type);
	boolean updateItemType(ItemType type);
	boolean deleteItemType(int id);
	
}
