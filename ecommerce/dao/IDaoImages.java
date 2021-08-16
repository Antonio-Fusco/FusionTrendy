package org.generation.italy.ecommerce.dao;

import java.util.List;

import org.generation.italy.ecommerce.model.Image;

public interface IDaoImages {
	List<Image> getImages();//solo per il test 
	int addImage(Image i);
	boolean updateImage(Image i);
	boolean deleteImage(int id);
}
