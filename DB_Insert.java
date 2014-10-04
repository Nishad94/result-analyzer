import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DB_Insert {

	public void put_name(String name, BasicDBObject document) {
		document.put("Name", name);
	}

	public void put_prn_number(String prn, BasicDBObject document) {
		document.put("PRN", prn);
	}

	public void put_batch(String batch, BasicDBObject document) {
		document.put("Batch", batch);
	}

	public void put_roll(String roll, BasicDBObject document) {
		document.put("Roll", roll);
	}

	public void put_college(String college, BasicDBObject document) {
		document.put("College", college);
	}

	public void put_branch(String roll, BasicDBObject document, DB_Init db_table,
			ArrayList<String> branchArray, ArrayList<String> rollStartArray,
			ArrayList<String> rollEndArray) {
		String first_char = roll.substring(0, 1);
		System.out.println("Outside branch insertion module ");
		for (int i = 0; i < branchArray.size(); ++i) {
			String startRoll = rollStartArray.get(i);
			String endRoll = rollEndArray.get(i);
			System.out.println("startROll : " + startRoll + " endROll : "
					+ endRoll + "first_char : " + first_char + "   Roll: "
					+ roll);
			System.out.println(document);
			if (first_char.equals(startRoll.substring(0, 1))) {
				String temp_roll, temp_startRoll, temp_endRoll;
				temp_startRoll = startRoll.substring(1);
				temp_endRoll = endRoll.substring(1);
				temp_roll = roll.substring(1);
				long start = Integer.parseInt(temp_startRoll), end = Integer
						.parseInt(temp_endRoll), current_roll = Integer
						.parseInt(temp_roll);
				if (current_roll >= start && current_roll <= end) {
					if (document.containsField("Branch") == true) {
						System.out.println("START OF DOC" + document);
						String id = document.get("_id").toString();
						BasicDBObject doc2  = new BasicDBObject();
						doc2.put("Branch", branchArray.get(i));
						db_table.original_table.update(new BasicDBObject("_id", id), doc2);
					}
					document.put("Branch", branchArray.get(i));
					System.out.println("Inside branch insertion module "
							+ branchArray.get(i));
				}
			}
		}
	}

	public void put_total_marks(int[] total_array, BasicDBObject document) {
		document.put("Total", total_array[0]);
		document.put("Scored", total_array[1]);
	}

	public void put_subject_details(int[] sub_details_array,
			BasicDBObject sub_doc) {
		sub_doc.put("Total", sub_details_array[0]);
		sub_doc.put("Scored", sub_details_array[1]);
	}

	public void put_subject_status(char status, BasicDBObject sub_doc) {
		sub_doc.put("Status", status);
	}

	/*public void put_result_statement(String tmp, BasicDBObject obj) {
		String year;
		if (obj.containsField("F"))
			year = "F";
		else if (obj.containsField("S"))
			year = "S";
		else if (obj.containsField("T"))
			year = "T";
		else
			year = "B";
		((DBObject) obj.get(year)).put("Result", tmp);
	}

	public void put_result_code(String tmp, BasicDBObject obj) {
		
		((DBObject) obj.get(year)).put("Result Code", tmp);
	}*/
	// Insert academic record into main if it isn't a duplicate and then insert
	// into db
	// Returns false if an entry/update is performed
	public String insert_and_update(BasicDBObject main, BasicDBObject acad_record, DB_Init db_table) {
		BasicDBObject query = new BasicDBObject("PRN", main.get("PRN"));
		DBCollection db = db_table.original_table;
		DBObject query_prn = db.findOne(query);
		String roll = (String) acad_record.get("Roll");
		String year = String.valueOf(roll.charAt(0));
		if (year.equals("E") == true)
			year = "F";
		//System.out.println(acad_record);
		// If the PRN doesn't exist add a new entry
		if (query_prn == null) {
			main.put(year, acad_record);
			db.insert(main);
			return "f";
		} else {
			// Check to see if the entry for this academic year exists
			// If it doesn't exist , add
			if (query_prn.containsField(year) == false) {
				query_prn.put(year, acad_record);
				// Change branch to new branch if earlier branch is FE
				if(query_prn.get("Branch").toString().equals("F.E. Common"))
					query_prn.put("Branch", main.get("Branch").toString());
				db.save(query_prn);
				return "f";
			} else
				return "t";	// not added anything
		}
	}
}