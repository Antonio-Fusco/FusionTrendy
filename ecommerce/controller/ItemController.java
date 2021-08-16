package org.generation.italy.ecommerce.controller;

import java.util.List;

import org.generation.italy.ecommerce.dao.IDaoItem;
import org.generation.italy.ecommerce.model.Item;
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
@RequestMapping({"/items","/items/*"})
public class ItemController {
	
	
	@Autowired
	IDaoItem dao;
	
	@GetMapping
	/**
	 * metodo che restituisce tutti gli items
	 * @return tutti gli items
	 */
	public List<Item> getAll(){
		return dao.getItems();
	}
	
	@GetMapping("/{id}")
	/**
	 * metodo che restituisce un item attraverso l'id
	 * @param id di un item
	 * @return un item
	 */
	public Item getOne(@PathVariable int id) {
		return dao.getItem(id);
	}
	
	@GetMapping("/item-id-type/{idType}")
	/**
	 * 
	 * metodo che restituisce una lista di item attraverso l' item type
	 * @param id del type degli items
	 * @return un al lista di item
	 */
	public List<Item> getAllByType(@PathVariable int idType ) {
		return dao.getItems(idType);
	}
	
	@PostMapping("/item-type-id/{id}")
	/**
	 * metodo che aggiunge un Item
	 * @param i: un oggetto item
	 * @return una stringa che comunica il successo o il fallimento dell' operazione
	 */
	public String addItem (@RequestBody Item i, @PathVariable int id){
		String ris="";
		if(i!=null) {
			dao.addItem(i,id);
			ris="Aggiunta riuscita";
		}else {
			ris="Aggiunta fallita";
		}
		return ris;
	}
	
	@PutMapping("/{id}/{idType}")
	/**
	 * 
	 * metodo che modifica un Item
	 * @param i: un oggetto item
	 * @param id: l' id di un item
	 * @return una stringa che comunica il successo o il fallimento dell' operazione
	 */
	public String updateItem(@PathVariable int id,@PathVariable int idType,@RequestBody Item i) {
		String ris="";
		if(i!=null&&dao.getItem(id)!=null&&id>0&&idType>0) {
			dao.updateItem(i,idType);//da confermare
			ris="Modifica riuscita";
		}else {
			ris="Modifica fallita";
		}
		return ris;
	}
	
	@DeleteMapping("/{id}")
	/**
	 * metodo che elimina un Item
	 * @param id di un oggetto item
	 * @return una stringa che comunica il successo o il fallimento dell' operazione
	 */
	public String deleteItem(@PathVariable int id) {
		String ris="";
		if(dao.getItem(id)!=null&&id>0) {
			dao.deleteItem(id);
			ris="Eliminazione riuscita";
		}else {
			ris="Eliminazione fallita";
		}
		return ris;
	}

}
