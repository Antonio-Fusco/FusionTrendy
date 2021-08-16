package org.generation.italy.ecommerce.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.generation.italy.ecommerce.model.Image;
import org.generation.italy.ecommerce.model.Item;
import org.generation.italy.ecommerce.util.BasicDao;
import org.generation.italy.ecommerce.util.IMappablePro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * La classe DaoItem si occuperà di gestire l'istanza degli oggetti di tipo Item,
 * i metodi per la richiesta di una lista o di uno specifico oggetto Item, a seconda dei
 * parametri passati in ingresso, e le operazioni CRUD. 
 *  
 * @author Team 2 - Giorgia
 * 
 *@implements IDaoItem che includerà i metodi astratti che sviluppiamo in questa
 *classe
 */

@Repository
public class DaoItem extends BasicDao implements IDaoItem {

	public DaoItem(@Value("${spring.datasource.url}")String dbAddress, 
			@Value("${spring.datasource.username}")String user,
			@Value("${spring.datasource.password}")String password) {
		super(dbAddress, user, password);

	}

//===============================METODI GET=====================================
	
	@Override
	/**
	 * Questo metodo ha la responsabilità di creare una lista contenente
	 * tutti gli Item.
	 * @author Team 2 - Giorgia, Antonio
	 */
	public List<Item> getItems() {
		//creazione di una lista vuota
		//invio della query al db per ottenere tutti gli item che, tramite il getAll()
		//li inserirà in una lista di mappe 
		//iterazione della lista di mappe 
		//istanza di Item, estraggo l'oggetto dalla mappa settando valori e la sua classe
		//con il metodo imageExtracted() rifattorizzo l'esecuzione della query che restituirà 
		//tutte le immagini associate al singolo Item e riempio la lista images
		//con il set() assegno le immagini contenute nella lista al corrispondente item
		//aggiunta dell'item alla lista ris.
		
		List<Item> ris = new ArrayList<>();
		List<Map<String, String>> maps = getAll("select * from items");
		for(Map<String, String> map : maps) {
			Item item = IMappablePro.fromMap(Item.class, map);
			//List<Image> images = imageExtracted(item);
			//item.setImages(images);
			ris.add(item);
		}
		return ris;
	}

	
	@Override	
	/**
	 * Questo metodo ha il compito di istanziare una lista di oggetti Item, ad 
	 * ognuno dei quali si allegheranno le informazioni relative al suo Type 
	 * (es. name, price, discount etc.)
	 * 
	 * @author Team 2 - Giorgia
	 * @param passa l'id che allegherà le informazioni riguardanti TypeItem
	 * @return una lista di Item
	 */
	public List<Item> getItems(int typeId) {
		//creazione della lista ris
		//invio della query al db per ottenere tutti gli item in base all'id del Type che,
		//tramite il getAll() li inserirà in una lista di mappe 
		//itero la lista di mappe 
		//con il fromMap() estrapolo l'oggetto dalla mappa, i valori settati
		//e passo come parametro la classe dell'oggetto e la singola mappa per l'istanza di Item
		////con il metodo imageExtracted() rifattorizzo l'esecuzione della query che restituirà 
		//tutte le immagini associate al singolo Item e riempio la lista images
		//infine assegno le immagini contenute nella lista di immagini al singolo item
		
		List<Item> ris = new ArrayList<>();
		List<Map<String, String>> maps = getAll("select * from items where typeid=?", typeId);
		for(Map<String, String> map : maps) {
			Item item = IMappablePro.fromMap(Item.class, map);
			ris.add(item);
		}
		return ris;
	}

	
	@Override
	/**
	 * Questo metodo si occupa di istanziare uno specifico oggetto di tipo Item
	 * con le immagini
	 * @author Team 2 - Giorgia
	 * @param l'id per ricavare tutti i dati dell'Item e le immagini 
	 * @return l'oggetto Item
	 */
	public Item getItem(int id) {
		//Istanza dell'oggetto, estrapolando dal fromMap() l'oggetto con i valori delle proprietà settate
		//passando come parametri la classe Item e la query che richiede al database l'invio dell'item
		//creazione di una lista delle mappe IMAGEMAPS che conterrà tutte le immagini
		//restituite dalla query sql in base all'id dell'item
		//creazione di un ArrayList IMAGES 
		//iterazione di ogni singola mappa appartenente alla lista di mappe con un foreach
		//aggiunta delle immagini alla singola mappa, si estrae con il fromMap() i valori settati 
		//e si passa come parametro il tipo della classe Image e la singola mappa.
		//assegno le immagini all'item
				
		Item item = IMappablePro.fromMap(Item.class, getOne("select * from items where id=?", id));
		return item;
	}

	//==============================METODI CRUD=====================================
	@Override
	/**
	 * Questo metodo ha lo scopo di gestire la creazione di nuove insert 
	 * @author Team 2 - Giorgia
	 * @param un oggetto Item e un typeId
	 * @return una insert con l'id autogenerato
	 */
	public int addItem(Item item, int typeId) {
		return insertAndGetId("insert into items(size, color, quantity,typeid) values(?,?,?,?)",
				item.getSize(),
				item.getColor(),
				item.getQuantity(),
				typeId);
	}

	
	@Override
	/**
	 * Questo metodo si occupa della modifica di uno o più dati del singolo oggetto
	 * @author Team 2 - Giorgia
	 * @param l'item e il typeId
	 * @return un risultato true / false se la modifica è avvenuta o meno
	 */
	public boolean updateItem(Item item, int typeId) {
		return isExecute("update items set size=?, color=?, quantity=?, typeid=? where id=?",
				item.getSize(),
				item.getColor(),
				item.getQuantity(),
				typeId,
				item.getId()
				);
	}

	
	@Override
	/**
	 * Questo metodo ha la responsabilità di eliminare l'oggetto
	 * @author Team 2 - Giorgia
	 * @param l'id dell'oggetto
	 * @return un risultato vero o falso se la cancellazione è avvenuta con successo o è fallita
	 */
	public boolean deleteItem(int id) {
		return isExecute("delete from items where id=?", id);
	}

	//=============================METODI ACCESSORI=================================
	/**
	 * Questo metodo accessorio si occupa di rifattorizzare determinati pezzi di codice per evitare
	 *ripetizioni. Nel caso specifico istanzia una lista di immagini contenente 
	 *tutte le immagini in base all'id dell'item.
	 *
	 * @param item
	 * @return
	 */
//	private List<Image> imageExtracted(Item item) {
//		List<Map<String, String>> imageMaps = getAll("select * from images where id=?", item.getId());
//		List<Image> images = new ArrayList<>();
//		for(Map<String, String> imageMap : imageMaps) {
//			images.add(
//					IMappablePro.
//					fromMap(Image.class, imageMap)
//					);
//
//}
//		return images;
	} 
