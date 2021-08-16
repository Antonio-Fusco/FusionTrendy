package org.generation.italy.ecommerce.controller;

import java.util.List;

import org.generation.italy.ecommerce.dao.IDaoCategory;
import org.generation.italy.ecommerce.model.Category;
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
@RequestMapping({"/categories","/categories/*"})
public class CategoryController {
  
  @Autowired
  IDaoCategory dao;
  
  @GetMapping
  /*
   * metodo che restituisce tutte le categorie
   * @return tutte le categorie
   */
  public List<Category> getAll() {
    return dao.getCategories();
    
  }
  @GetMapping("/{id}")
  /*
   * metodo che restituisce una categoria attraverso l' id
   * @param id di una categoria
   * @return una categoria
   */
  public Category getOne(@PathVariable int id) {
    return dao.getCategory(id);
    
  }
  
  @PostMapping
  /*
   * metodo che aggiunge una categoria alla lista
   * @param c: la nuova category 
   * @return il messaggio di successo o fallimento dell' operazione
   */
  public String addCategory(@RequestBody Category c) {
    String ris="";//preparo una string vuota
    if(c!=null) {
      dao.addCategory(c);//se c esiste la aggiungo alla lista 
      ris="Aggiunta effettuata";//comunico l' avvenuta aggiunta
    }else {
      ris="Errore nell' aggiunta";//comunico l' errore
    }
    return ris;
  }
  
  @PutMapping("/{id}")
  /*
   * metodo che modifica una categoria
   * @param c: category modificata
   * @param id della category da modificare
   * @return il messaggio di successo o fallimento dell' operazione
   */
  public String updateCategory(@RequestBody Category c,@PathVariable int id) {
    String ris="";//preparo una string vuota
    if(c!=null||id>0) {
      dao.updateCategory(c);//se c esiste e id è >0 la modifico
      ris="Modifica effettuata";//comunico l' avvenuta modifica
    }else {
      ris="Errore nella Modifica";//comunico l' errore
    }
    return ris;
  }
  
  @DeleteMapping("/{id}")
  /*
   * metodo che elimina una categoria
   * @param id della category da eliminare
   * @return il messaggio di successo o fallimento dell' operazione
   */
  public String deleteCategory(@PathVariable int id) {
    String ris="";//preparo una string vuota
    if(dao.getCategory(id)!=null||id>0) {
      dao.deleteCategory(id);//se getCategory(id) esiste e id è >0 la elimino
      ris="Eliminazione effettuata";//comunico l' avvenuta eliminazione
    }else {
      ris="Errore nell' eliminazione";//comunico l' errore
    }
    return ris;
  }

}