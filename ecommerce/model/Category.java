package org.generation.italy.ecommerce.model;

import org.generation.italy.ecommerce.util.IMappablePro;

public class Category implements IMappablePro {

	private int id;
	private String name;
	
	public Category() {
		super();
	}

	public Category(int id, String name) 
	{
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
