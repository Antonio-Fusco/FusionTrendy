package org.generation.italy.ecommerce.controller;

import java.util.List;

import org.generation.italy.ecommerce.dao.IDaoImages;
import org.generation.italy.ecommerce.model.Image;
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
@RequestMapping({"/images","images/*"})
public class ImagesController {

	@Autowired
	IDaoImages dao;

	@GetMapping
	/*
	 * metodo che restituisce tutte le immagini
	 * @return tutte le immagini
	 */
	public List<Image> getAll() {
		return dao.getImages();

	}

	@PostMapping
	/*
	 * metodo che aggiunge una immagine alla lista
	 * @param i: la nuova immagine 
	 * @return il messaggio di successo o fallimento dell' operazione
	 */
	public String addImage(@RequestBody Image i) {
		String ris="";//preparo una string vuota
		if(i!=null) {
			dao.addImage(i);//se c esiste la aggiungo alla lista 
			ris="Aggiunta effettuata";//comunico l' avvenuta aggiunta
		}else {
			ris="Errore nell' aggiunta";//comunico l' errore
		}
		return ris;
	}

	@PutMapping("/{id}")
	/*
	 * metodo che modifica un immagine
	 * @param i: immagine modificata
	 * @param id dell'immagine da modificare
	 * @return il messaggio di successo o fallimento dell' operazione
	 */
	public String updateImage(@RequestBody Image i,@PathVariable int id) {
		String ris="";//preparo una string vuota
		if(i!=null||id>0) {
			dao.updateImage(i);//se i esiste e id è >0 la modifico
			ris="Modifica effettuata";//comunico l' avvenuta modifica
		}else {
			ris="Errore nella Modifica";//comunico l' errore
		}
		return ris;
	}

	@DeleteMapping("/{id}")
	/*
	 * metodo che elimina un immagine
	 * @param id dell' immagine da eliminare
	 * @return il messaggio di successo o fallimento dell' operazione
	 */
	public String deleteImage(@PathVariable int id) {
		String ris="";//preparo una string vuota
		if(id>0) {
			dao.deleteImage(id);// id è >0 la elimino
			ris="Eliminazione effettuata";//comunico l' avvenuta eliminazione
		}else {
			ris="Errore nell' eliminazione";//comunico l' errore
		}
		return ris;
	}

}
