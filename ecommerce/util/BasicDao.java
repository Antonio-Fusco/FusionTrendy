package org.generation.italy.ecommerce.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Questa classe ha lo scopo di fornire gli strumenti base per effettuare ORM.
 * Stabilita una connessione � in grado di eseguire una query e restituire, in
 * caso di necessit�, una lista di mappe o una mappa che descrive un ResulSet.
 * La connessione di questo dao � sempre aperta
 * 
 * @author trito
 */
public abstract class BasicDao {

	// propriet�
	private Connection connection;

	// costruttore
	public BasicDao(String dbAddress, String user, String password) {
		try {
			connection = DriverManager.getConnection(dbAddress, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lista contenente mappe che descrivono un'entity nella persistenza La mappa �
	 * praticamente la rappresentazione di una RIGA di una tabella La lista quindi �
	 * l'insieme delle righe di una tabella
	 * 
	 * @param sql        la query da inviare
	 * @param conditions il/i valore/i da sostiuire ai placeholders della query
	 * @return La lista contenente tutte le mappe restituite dal db in base alla
	 *         query inserita
	 */
	public List<Map<String, String>> getAll(String sql, Object... conditions) {
		// ... � chiamato spread operator
		// a questo metodo, oltre alla stringa sql possono arrivare da 0 a infiniti
		// parametri
		// di tipo generico
		List<Map<String, String>> ris = new ArrayList<>();

		try {
			ResultSet rs = executeQuery(sql, conditions);

			// itero il resultset
			while (rs.next()) {
				Map<String, String> map = mapFromRS(rs);

				ris.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ris;
	}

	/**
	 * Restituisce il resultset risultante da una query
	 * 
	 * @param sql        la query da inviare
	 * @param conditions
	 * @return L'oggetto ResultSet risultante dal PreparedStatement in base alla
	 *         query inserita
	 * @throws SQLException
	 */
	private ResultSet executeQuery(String sql, Object... conditions) throws SQLException {
		return prepareStm(sql, conditions).executeQuery();
	}

	/**
	 * Restituisce l'oggetto di tipo PreparedStatement per effettuare una query
	 * 
	 * String sql = "SELECT * FROM tabella WHERE id = ?" => conditions[0] => 1
	 * ?(1) => conditions[0]
	 * ?(2) => conditions[1]
	 * @param sql la query da inviare
	 * @param conditions ...
	 * @return il PreparedStatement contenente la query con i placeholders sostituiti
	 * @throws SQLException
	 */
	private PreparedStatement prepareStm(String sql, Object... conditions) throws SQLException {
		// chiedo alla connessione (sempre aperta) di fornirmi un PS e le passo la
		// string sql
		// SELECT * FROM persone; / SELECT * FROM persone WHERE eta > ?;
		PreparedStatement stm = connection.prepareStatement(sql);

		// conditions � un vettore di parametri
		// lo itero. Se non ne ho passati, l'iterazione non avverr�
		for (int i = 0; i < conditions.length; i++) {
			// devo sostiutire i placeholders ?
			// setObject setta un intero se il parametro che sto iterando � un int,
			// una stringa se il parametro � una stringa .....
			// se vi ricordate l'indice dei ? parte da 1
			// setInt(1, 32)
			stm.setObject(i + 1, conditions[i]);
		}
		
		return stm;
	}

	/**
	 * Restituisce la mappa di una singola riga di un resultSet dove la chiave
	 * rappresenta il nome della colonna della tabella e il valore quello nella
	 * cella di quella determinata riga
	 * 
	 * @param rs il resultSet ottenuto dal DB eseguendo una determinata query
	 * @return la mappa che descrive la singola riga di un resultSet
	 * @throws SQLException
	 */
	private Map<String, String> mapFromRS(ResultSet rs) throws SQLException {
		// creo la mappa per contenere le info della riga della tabella
		Map<String, String> map = new HashMap<>();

		// prendo i metadati
		ResultSetMetaData metaData = rs.getMetaData();

		// itero il numero delle colonne dei metadati
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			// alla chiave della mappa inserisco il nome della colonna
			// e al valore il corrispettivo nella riga
			map.put(metaData.getColumnName(i), rs.getString(i));
		}

		return map;
	}

	/**
	 * Mappa che descrive un'entit� nella persistenza
	 * 
	 * @param sql        la query da inviare
	 * @param conditions ...
	 * @return La mappa restituita dal db in base alla query inserita
	 */
	public Map<String, String> getOne(String sql, Object... conditions) {
		Map<String, String> ris = null;

		try {
			ResultSet rs = executeQuery(sql, conditions);

			if (rs.next()) {
				ris = mapFromRS(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ris;
	}

	/**
	 * Esegue una query impostando prima le condizioni
	 * 
	 * @param sql la query da inviare
	 * @param conditions ...
	 */
	public void execute(String sql, Object... conditions) {
		try {
			prepareStm(sql, conditions).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce un prepared statement che � in grado di restituire l'id
	 * dell'inserimento
	 * @param sql
	 * @param conditions
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement preparedStatementWithGeneratedKey(String sql, Object... conditions) throws SQLException{
		// vado a creare il PS dalla connessione, caricandogli la stringa sql
		// e come secondo parametro, la costante RETURN_GENERATED_KEYS. 
		// Quest'ultima altro non � che un int di valore 1
		// il jdbc legger� quest "interruttore acceso" ed effettuer� la richiesta
		// delle chiavi generate al db
		PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		
		// itero per scambiare i placeholders coni valori passati da parametro
		for (int i = 0; i < conditions.length; i++) {
			stm.setObject(i + 1, conditions[i]);
		}
		
		return stm;
	}
	
	/**
	 * Metodo che effettua una insert e restituisce l'id che viene auto generato
	 * @param sql
	 * @param conditions
	 * @return
	 */
	public int insertAndGetId(String sql, Object... conditions) {
		int id = 0;
	
		try {
			PreparedStatement stm = preparedStatementWithGeneratedKey(sql, conditions);
			
			// mando in esecuzione la query
			stm.executeUpdate();
			
			// una volta che � ritornata, richiedo le chiavi generate
			ResultSet rs = stm.getGeneratedKeys();
			
			if (rs.next()) {
				// vado a prendere il valore contenuto alla prima colonna
				id = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	public boolean isExecute(String query, Object... conditions) {
		int n = 0;
		
		try {
			n = prepareStm(query, conditions).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (n > 0) {
			return true;
		}
		
		return false;
	}
}