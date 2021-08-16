package org.generation.italy.ecommerce.controller;

import java.util.List;

import org.generation.italy.ecommerce.dao.IDaoItemTypes;
import org.generation.italy.ecommerce.model.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/item-types","/item-types/*"})
public class ItemTypesController {
	
	@Autowired
	IDaoItemTypes dao;
	
	@GetMapping
	/**
	 * metodo che restituisce tutti gli itemtypes
	 * @return tutti gli itemtypes
	 */
	public List<ItemType> getAll() {
		return dao.getItemTypes();
	}
	
	@GetMapping("/{id}")
	/**
	 * metodo che restituisce un itemtype attraverso l' id
	 * @param id di un itemtype
	 * @return un itemtype
	 */
	public ItemType getOne(@PathVariable int id) {
		return dao.getItemType(id);
	}
	
	
	@PostMapping
	/**
	 * metodo che aggiunge un Itemtype
	 * @param i: un oggetto itemtype
	 * @return una stringa che comunica il successo o il fallimento dell' operazione
	 */
	public String addItemType (@RequestBody ItemType it){
		String ris="";
		if(it!=null) {
			dao.addItemType(it);
			ris="Aggiunta riuscita";
		}else {
			ris="Aggiunta fallita";
		}
		return ris;
	}	
	
	@PutMapping("/{id}")
	/**
	 * 
	 * metodo che modifica un ItemType
	 * @param i: un oggetto itemType
	 * @param id: l' id di un itemType
	 * @return una stringa che comunica il successo o il fallimento dell' operazione
	 */
	public String updateItem(@PathVariable int id,@RequestBody ItemType it) {
		String ris="";
		if(it!=null&&dao.getItemType(id)!=null&& id>0) {
			dao.updateItemType(it);
			ris="Modifica riuscita";
		}else {
			ris="Modifica fallita";
		}
		return ris;
	}
	
	@DeleteMapping("/{id}")
	/**
	 * metodo che elimina un ItemType
	 * @param id di un oggetto itemType
	 * @return una stringa che comunica il successo o il fallimento dell' operazione
	 */
	public String deleteItem(@PathVariable int id) {
		String ris="";
		if(dao.getItemType(id)!=null&& id>0) {
			dao.deleteItemType(id);
			ris="Eliminazione riuscita";
		}else {
			ris="Eliminazione fallita";
		}
		return ris;
	}
	
}
