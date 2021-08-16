package org.generation.italy.ecommerce.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.generation.italy.ecommerce.model.Category;
import org.generation.italy.ecommerce.util.BasicDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * La classe DaoCategory istanzia oggetti di tipo Categoria.
 * Include metodi ORM che restituiscono una lista di oggetti Categoria o uno specifico
 * oggetto in base al parametro passato in ingresso.
 * Comprende anche metodi CRUD (create, update e delete) 
 *  
 * @author Team 2 - Giorgia
 * 
 *@implements IDaoItem includerà i metodi astratti che sviluppiamo in questa
 *classe
 */

@Repository
public class DaoCategory extends BasicDao implements IDaoCategory {

	public DaoCategory(
			@Value("${spring.datasource.url}")String dbAddress,
			@Value("${spring.datasource.username}")String user,
			@Value("${spring.datasource.password}")String password) {
		super(dbAddress, user, password);
		}
	
//=================================METODO GET===================================
	@Override
	/**metodo che mostra la lista di categories
	 * @return lista di categories
	 * @author Gruppo2, Andrea
	 */
	public List<Category> getCategories() {
		//creazione di una lista vuota di nome "ris"
		//invio della query al db per ottenere tutti gli oggetti categoria che, tramite il getAll()
		//saranno inseriti in una lista di mappe 
		//con un foreach si itera ogni mappa appartenente alla lista di mappe 
		//si istanzia l'oggetto
		//si estrapola l'oggetto dalla mappa con tutte le informazioni e i valori settati
		//e lo si aggiunge alla lista "ris"
		
		List<Category> ris= new ArrayList<>();
		List<Map<String,String>>map=getAll("SELECT * FROM categories");
		
		for (Map<String, String> m : map) {
			Category c=new Category();
			c.fromMap(m);
			ris.add(c);
		}
		
		return ris;
	}

	@Override
	/**metodo che mostra la lista di categories
	 * @return una category
	 * @param l' id di una category 
	 * @author Gruppo2, Andrea
	 */
	public Category getCategory(int id) {
		//si istanza un oggetto Categoria che all'inizio avrà valore null
		// si invia la query al database che restituirà
		//un oggetto in base all'id passato come parametro nella firma del metodo.
		//L'oggetto verrà inserito in una mappa
		//Se l'oggetto esiste, si concretizza e lo si estrae dalla mappa con tutte le informazioni
		
		Category ris=null;
		Map<String,String>map=getOne("SELECT* FROM categories WHERE id=?",id);
		if(map!=null) {
			ris=new Category();
			ris.fromMap(map);
		}
		
		return ris;
	}

//==============================METODI CRUD=====================================
	@Override
	/**
	 * Aggiunta di una Category
	 * @param una category
	 * @author Gruppo2, Andrea
	 */
	public int addCategory(Category c) {
		return insertAndGetId("INSERT INTO categories(name) VALUES (?)",c.getName());
	}

	@Override
	/**metodo che modifica una categories
	 * @param una category
	 * @author Gruppo2, Andrea
	 */
	public boolean updateCategory(Category c) {
		return isExecute("UPDATE categories SET name=? WHERE id=?",c.getName(),c.getId());
	}

	@Override
	/**metodo che elimina una categories
	 * @param l' id di una category
	 * @author Gruppo2, Andrea
	 */
	public boolean deleteCategory(int id) {
		return isExecute("DELETE FROM categories WHERE id=?",id);
	}
}
