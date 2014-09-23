import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DB_Init {

	MongoClient mongo;
	DB db;
	DBCollection original_table, filtered_collection;
	
	public DB_Init() {
		try {
			System.out.println("In db init");
			mongo = new MongoClient("localhost", 27017);
			db = mongo.getDB("testdb");
			original_table = db.getCollection("students");
	//		filtered_collection = original_table;
			filtered_collection = db.getCollection("filtered");
			System.out.println("exiting db init");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}