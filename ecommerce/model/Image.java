package org.generation.italy.ecommerce.model;

import org.generation.italy.ecommerce.util.IMappablePro;

public class Image implements IMappablePro {

	private int id;
	private int typeid;
	private String filepath;
	
	public Image(int id, int typeid, String filepath) 
	{
		this.id = id;
		this.typeid = typeid;
		this.filepath = filepath;
	}

	public Image() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int itemtypeid) {
		this.typeid = itemtypeid;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
		
}

