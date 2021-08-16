package org.generation.italy.ecommerce.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.generation.italy.ecommerce.model.Category;
import org.generation.italy.ecommerce.model.Image;
import org.generation.italy.ecommerce.model.Item;
import org.generation.italy.ecommerce.model.ItemType;
import org.generation.italy.ecommerce.util.BasicDao;
import org.generation.italy.ecommerce.util.IMappablePro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
/**
 * La classe DaoItemTypes si occuperà di gestire l'istanza degli oggetti di tipo ItemType.
 * Comprende l'implementazione di metodi per la richiesta di una lista 
 * o di uno specifico oggetto ItemType
 * Include anche metodi CRUD per la creazione, modifica ed eliminazione degli oggetti. 
 *  
 * @author Team 2 - Giorgia
 * 
 *@implements IDaoItem che includerà i metodi astratti che sviluppiamo in questa
 *classe
 */
public class DaoItemTypes extends BasicDao implements IDaoItemTypes {

	public DaoItemTypes(@Value("${spring.datasource.url}")String dbAddress,
			@Value("${spring.datasource.username}")String user,
			@Value("${spring.datasource.password}")String password) {
		super(dbAddress, user, password);
		
	}

//============================METODI GET========================================
	@Override
	/**
	 * Questo metodo ha la responsabilità di istanziare una lista contenente
	 * tutti gli ItemTypes.
	 * @author Team 2 - Giorgia, Antonio
	 * @return una lista di oggetti di tipo ItemType
	 */

//================================METODI GET====================================
	
	public List<ItemType> getItemTypes() {
		//Possiamo suddividere l'implementazione di questo metodo in 3 parti:
		
		// 1) creazione di una lista vuota
		//invio della query al db per ottenere tutti gli itemtypes che, tramite il getAll()
		//saranno inseriti in una lista di mappe 
		//con il foreach si itera ogni mappa appartenente alla lista di mappe
		//si istanza l'oggetto ItemType
		//si estrae l'oggetto dalla mappa settando valori e la sua classe con il fromMap()
		
		// 2) si ripete la procedura per allegare le informazioni della Categoria, passando come 
		//parametro la query che restituirà la singola categoria in base all'id nel metodo fromMap()
		//si definisce il "valore" della categoria per l'oggetto type con setCategory()
		
		// 3) si inseriscono in una lista di mappe gli oggetti Item selezionati in base al typeid con il getAll()
		// creazione di una lista vuota 
		// il foreach itera ogni singola mappa itemMap appartenente alla lista di mappe
		//si aggiungono alla nuova lista gli oggetti con il fromMap() che setterà la classe e i 
		//valori delle sue proprietà
		//con il metodo setItems() si assegnano le informazioni di Item al type
		//alla fine si aggiunge l'oggetto type con tutte le informazioni alla lista ris
		List<ItemType> ris = new ArrayList<>();
		List<Map<String, String>> maps = getAll("select * from itemtypes");
		for(Map<String, String> map : maps) {
			ItemType type = IMappablePro.fromMap(ItemType.class, map);
			Category category = IMappablePro.fromMap(
					Category.class, 
					getOne("select * from categories where id=?",map.get("categoryid"))
					);
			type.setCategory(category);
			List<Map<String, String>> itemMaps = getAll("select * from items where typeid=?", map.get("id"));
			List<Item> items = new ArrayList<>();
			for(Map<String, String> itemMap : itemMaps) {
				items.add(
						IMappablePro.
						fromMap(Item.class, itemMap)
						);
			}
			type.setItems(items);
			List<Map<String,String>>imagesMaps=getAll("select * from images where typeid=?",map.get("id"));
			List<Image> images=new ArrayList<>();
			for (Map<String, String> imageMap : imagesMaps) {
				images.add(IMappablePro.
						fromMap(Image.class, imageMap)
						);
				
			}
			type.setImages(images);
			ris.add(type);
		}
		return ris;
	}


	@Override
	/**
	 * Questo metodo restituisce un oggetto di tipo ItemType in base all'id passato
	 * come parametro con tutte i dati relativi all'item e la categoria appartenente
	 * 
	 * @author Team 2 - Giorgia
	 * @param passa l'id che allegherà le informazioni riguardanti TypeItem
	 * @return una oggetto di tipo ItemType
	 */
	public ItemType getItemType(int id) {
		//La query riempirà la mappa con le informazioni di una specifica riga della tabella
		//itemtypes in base all'id selezionato e passata come parametro nel metodo
		//Verrà istanziato un oggetto di tipo ItemType che, con il metodo fromMap(), estrapolerà
		//le informazioni relative alla classe e alle sue proprietà contenute nella mappa
		//si ripete il processo per l'istanza dell'oggetto Categoria con il metodo rifattorizzato extractedCategory(map)
		
		//dopodiché con una query si prendono dal database tutti i dati relativi ad un singolo record
		//della tabella items in base al typeid
		//si inizializza la lista items con il metodo extractedItems(itemMaps) che aggiunge alla lista
		//gli oggetti Item con la classe e le proprietà già settate
		//infine si assegnano i dati degli items all'oggetto di tipo ItemType in base all'id
		//e la categoria
		Map<String, String> map = getOne("select * from itemtypes where id=?", id);
		ItemType type = IMappablePro.fromMap(ItemType.class, map);
		Category category = extractedCategory(map);
		List<Map<String, String>> itemMaps = getAll("select * from items where typeid=?", map.get("id"));
		// TODO: rifattorizzazione italy
		List<Item> items = extractedItems(itemMaps);
		type.setItems(items);
		type.setCategory(category);
		List<Map<String,String>> imagesMaps=getAll("select* from images where typeid=?",map.get("id"));
		List<Image>images=extractedImages(imagesMaps);
		type.setImages(images);
		return type;
	}

	

	private Category extracted(Map<String, String> map) {
		Category category = IMappablePro.fromMap(
				Category.class, 
				getOne("select * from categories where id=?",
						map.get("categoryid")
						)
				);
		return category;
	}
	
	private List<Image> extractedImages(List<Map<String,String>> imagesMaps){
		List<Image> images=new ArrayList<>();
		for (Map<String,String> imageMap : imagesMaps) {
			images.add(IMappablePro.
					fromMap(Image.class,imageMap));
		}
		return images;
	}

//============================METODI CRUD=======================================
	@Override
	public int addItemType(ItemType type) {
		return insertAndGetId("insert into itemtypes(name, price, discount, categoryid) values(?,?,?,?)",
				type.getName(),
				type.getPrice(),
				type.getDiscount(),
				type.getCategory().getId());
	}

	@Override
	public boolean updateItemType(ItemType type) {
		return isExecute("update itemtypes set name=?, price=?, discount=?, categoryid=?, description=? where id=?",
				type.getName(),
				type.getPrice(),
				type.getDiscount(),
				type.getCategory().getId(),
				type.getDescription(),
				type.getId()
				);
	}

	@Override
	public boolean deleteItemType(int id) {
		return isExecute("delete from itemtypes where id=?", id);
	}


//=============================METODI ACCESSORI=================================
	/**
	 * Metodo accessorio che restituisce una lista di oggetti Item passando
	 * come parametro una lista di mappe
	 * @author Team 2 - Giorgia
	 * @param itemMaps
	 * @return una lista di oggetti Item
	 */
	private List<Item> extractedItems(List<Map<String, String>> itemMaps) {
		List<Item> items = new ArrayList<>();
		for(Map<String, String> itemMap : itemMaps) {
			items.add(
					IMappablePro.
					fromMap(Item.class, itemMap)
					);
		}
		return items;
	}

	/**
	 * Metodo che ritorna un oggetto Categoria in base alla mappa passata come
	 * parametro in ingresso, dalla quale si va a prendere l'id dell'oggetto
	 * @param map
	 * @return un oggetto Categoria 
	 */
	private Category extractedCategory(Map<String, String> map) {
		Category category = IMappablePro.fromMap(
				Category.class, 
				getOne("select * from categories where id=?",
						map.get("categoryid")
						)
				);
		return category;
	}

}
