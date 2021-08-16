package org.generation.italy.ecommerce.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.generation.italy.ecommerce.model.Image;
import org.generation.italy.ecommerce.util.BasicDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * La classe DaoImages ha la responsabilità di istanziare oggetti di tipo Image.
 * Comprende il metodo che restituisce la lista delle immagini e le operazioni
 * CRUD 
 * @author Giorgia
 *
 */
@Repository
public class DaoImages extends BasicDao implements IDaoImages{

	public DaoImages(
			@Value("${spring.datasource.url}")String dbAddress,
		    @Value("${spring.datasource.username}")String user, 
			@Value("${spring.datasource.password}")String password) {
		super(dbAddress, user, password);
		// TODO Auto-generated constructor stub
	}


//==============================METODI GET======================================
	@Override
	/**metodo che mostra la lista di immagini
	 * @return lista di immagini
	 * @author Gruppo2, Andrea
	 */
	public List<Image> getImages() {
		//creazione di una lista vuota
		//La query seleziona tutte le immagini e le inserire, tramite il getAll(), in una lista di mappe
		//si itera la lista di mappe
		//ad ogni mappa si crea un oggetto Image
		//lo si estrare dalla mappa con i valori settate e lo si aggiunge alla lista ris
		
		List<Image> ris= new ArrayList<>();
		List<Map<String,String>>map=getAll("SELECT * FROM images");
		
		for (Map<String, String> m : map) {
			Image i=new Image();
			i.fromMap(m);
			ris.add(i);
		}
		
		return ris;
	}

//==============================METODI CRUD=====================================
	@Override
	/**metodo che aggiunge un immagine
	 * @param un immagine
	 * @author Gruppo2, Andrea
	 */
	public int addImage(Image i) {
		return insertAndGetId("INSERT INTO images (filepath, typeid)VALUES (?,?)", i.getFilepath(), i.getTypeid());
	}

	@Override
	/**metodo che modifica un immagine
	 * @param un immagine
	 * @author Gruppo2, Andrea
	 */
	public boolean updateImage(Image i) {
		return isExecute("UPDATE images SET filepath=?,typeid=? WHERE id=?",i.getTypeid(),i.getFilepath(),i.getId());
	}

	@Override
	/**metodo che elimina un immagine
	 * @param l' id di un immagine
	 * @author Gruppo2, Andrea
	 */ 
	public boolean deleteImage(int id) {
		return isExecute("DELETE FROM images WHERE id=?",id);
	}
}
