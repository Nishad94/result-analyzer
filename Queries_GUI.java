import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Queries_GUI {
	static DB_Queries q = new DB_Queries();
	DBCursor db_cursor;
	
	String[] collegeList(DB_Init db_table)
	{
		ArrayList<String> colleges = new ArrayList<String>();
		colleges.add("College");
		db_cursor = db_table.original_table.find();
		try{
			while(db_cursor.hasNext()){
				DBObject ob = db_cursor.next();
				String coll = ob.get("College").toString();
				if(colleges.contains(coll) == false)
					colleges.add(coll);			
			}
		} finally {
			db_cursor.close();
		}
		
		String[] colleges_string_arr = new String[colleges.size()];
		colleges_string_arr = colleges.toArray(colleges_string_arr);
		
		return colleges_string_arr;
	}
	
	String[] branchList(DB_Init db_table)
	{
		ArrayList<String> branch = new ArrayList<String>();
		branch.add("Branch");
		db_cursor = db_table.original_table.find();
		try{
			while(db_cursor.hasNext()){
				DBObject ob = db_cursor.next();
				String bran = ob.get("Branch").toString();
				if(branch.contains(bran) == false)
					branch.add(bran);			
			}
		} finally {
			db_cursor.close();
		}
		
		String[] branch_string_arr = new String[branch.size()];
		branch_string_arr = branch.toArray(branch_string_arr);
		
		return branch_string_arr;
	}
	
	String[] batchList(DB_Init db_table)
	{
		ArrayList<String> batch = new ArrayList<String>();
		batch.add("Batch");
		db_cursor = db_table.original_table.find();
		try{
			while(db_cursor.hasNext()){
				DBObject ob = db_cursor.next();
				String coll = ob.get("Batch").toString();
				if(batch.contains(coll) == false)
					batch.add(coll);			
			}
		} finally {
			db_cursor.close();
		}
		
		String[] batch_string_arr = new String[batch.size()];
		batch_string_arr = batch.toArray(batch_string_arr);
		
		return batch_string_arr;
	}
	
	List<String[]> searchByName(String name, DB_Init db_table)
	{
		List<String[]> records = new ArrayList<String[]>();
		db_cursor = q.filter_by_name(name, db_table.filtered_collection);
		db_table.filtered_collection = db_cursor.getCollection();
		
		int counter = 1;
		try {
			while(db_cursor.hasNext()){
				DBObject ob = db_cursor.next();
				//System.out.println(ob);
				records.add(new String[] {Integer.toString(counter), ob.get("Name").toString(), ob.get("College").toString(), ob.get("Branch").toString()});
				counter++;
			}
		} finally {
			db_cursor.close();
		}
		
		return records;
	}
	
	List<String[]> searchByRoll(String roll, DB_Init db_table)
	{
		List<String[]> records = new ArrayList<String[]>();
		db_cursor = q.filter_by_roll(roll, db_table.filtered_collection);
		db_table.filtered_collection = db_cursor.getCollection();
		
		int counter = 1;
		try {
			while(db_cursor.hasNext()){
				DBObject ob = db_cursor.next();
				System.out.println(ob.get(roll.substring(0, 1)));
				//records.add(new String[] {Integer.toString(counter), ((BasicDBObject)ob.get(roll.substring(0, 1))).get("Roll").toString(), ob.get("College").toString()});
				records.add(new String[] {Integer.toString(counter), ob.get("Name").toString(), ob.get("College").toString(), ob.get("Branch").toString()});
				counter++;
			}
		} finally {
			db_cursor.close();
		}
		
		return records;
	}
	List<String[]> filterByCriteria(String college, String branch, String batch, String year, DB_Init db_table)
	{
		
		List<String[]> records = new ArrayList<String[]>();
		db_cursor = db_table.original_table.find();    // Default table
		db_table.filtered_collection.drop();
		System.out.println(college + " " + year);
		
		List<DBObject> db_list_main = db_cursor.toArray();
		for(int i = 0; i < db_list_main.size(); ++i)
			db_table.filtered_collection.insert(db_list_main.get(i));
		
		// Filters
		if(college != "College"){
			System.out.println("Inside filter by colg");
			List<DBObject> db_list = q.filter_by_college(college, db_table.original_table).toArray();
			System.out.println(db_list.size()+ " ---  ");
			db_table.filtered_collection.drop();
			System.out.println(db_list.size());
			
			/*try{
				while(db_cursor.hasNext()){
					db_table.filtered_collection.insert(db_cursor.next());
				}
			} finally {
				db_cursor.close();
			}
			System.out.println("After year filter : " + Long.toString(db_table.filtered_collection.count()));*/
			for(int i = 0; i < db_list.size(); ++i)
				db_table.filtered_collection.insert(db_list.get(i));
			System.out.println("After college filter : " + Long.toString(db_table.filtered_collection.count()));
		}
		if(branch != "Branch"){
			List<DBObject> db_list = q.filter_by_branch(branch, db_table.filtered_collection).toArray();
			System.out.println(db_cursor.count()+ " ---  ");
			db_table.filtered_collection.drop();
			System.out.println(db_list.size());
			
			for(int i = 0; i < db_list.size(); ++i)
				db_table.filtered_collection.insert(db_list.get(i));
		}
		if(batch != "Batch"){
			
			List<DBObject> db_list = q.filter_by_batch(batch, db_table.filtered_collection).toArray();
			System.out.println(db_cursor.count()+ " ---  ");
			db_table.filtered_collection.drop();
			System.out.println(db_list.size());
			
			for(int i = 0; i < db_list.size(); ++i)
				db_table.filtered_collection.insert(db_list.get(i));
		}
		if(year != "Year"){
			System.out.println(year);
			year = year.substring(0,1);
			System.out.println("Before year filter : " + Long.toString(db_table.filtered_collection.count()));
			List<DBObject> db_list = q.filter_by_year(year, db_table.filtered_collection).toArray();
			System.out.println(db_cursor.count()+ " ---  ");
			db_table.filtered_collection.drop();
			System.out.println(db_list.size());
			
			for(int i = 0; i < db_list.size(); ++i)
				db_table.filtered_collection.insert(db_list.get(i));
			//System.out.println(db_cursor.count());
		}
		
		int counter = 1;
		db_cursor = db_table.filtered_collection.find();
		try {
			while(db_cursor.hasNext()){
				DBObject ob = db_cursor.next();
				//System.out.println(ob);
				records.add(new String[] {Integer.toString(counter), ob.get("Name").toString(), ob.get("College").toString(), ob.get("Branch").toString()});
				counter++;
			}
		} finally {
			db_cursor.close();
		}
		
		/*for(int i = 0; i < records.size(); ++i) {
    		System.out.println(records.get(i)[1]);
    	}*/
		System.out.println("Size after filter " + Integer.toString(records.size()));
		return records;
			
	}
	
}
