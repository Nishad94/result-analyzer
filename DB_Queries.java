import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

// year = 'F'/'S'/'T'/'B'
public class DB_Queries {

	DB_Init init = new DB_Init();

	public DBCursor filter_by_prn(String prn, DBCollection dbc) {
		return dbc.find(new BasicDBObject("PRN", prn));
	}

	public DBCursor filter_by_name(String name, DBCollection dbc) {
		return dbc.find(new BasicDBObject("Name", java.util.regex.Pattern
				.compile("(.*?)" + name + "(.*?)", Pattern.CASE_INSENSITIVE)));
	}

	public DBCursor filter_by_roll(String roll, DBCollection dbc) {
		roll = roll.toUpperCase();
		return dbc.find(new BasicDBObject(roll.charAt(0) + ".Roll", roll));
	}

	public DBCursor filter_by_branch(String branch, DBCollection dbc) {
		return dbc.find(new BasicDBObject("Branch", branch));
	}

	public DBCursor filter_by_class(char year, String result_class,
			DBCollection dbc) {
		return dbc.find(new BasicDBObject(year + ".Result", result_class));

	}

	public DBCursor filter_by_college(String college, DBCollection dbc) {
		// return dbc.find(new
		// BasicDBObject("College",java.util.regex.Pattern.compile("(.*?)"+college+"(.*?)",
		// Pattern.CASE_INSENSITIVE)));
		return dbc.find(new BasicDBObject("College", college));
	}

	public DBCursor filter_by_year(String year, DBCollection dbc) {
		DBObject query = new BasicDBObject(year, new BasicDBObject("$exists",
				true));
		DBCursor cursor = dbc.find(query);
		System.out.println("Inside filter_by_year : "
				+ Integer.toString(cursor.count()));
		return cursor;
	}

	public DBCursor filter_by_batch(String batch, DBCollection dbc) {
		return dbc.find(new BasicDBObject("Batch", batch));
	}

	public DBCursor sort_by_name(DBCollection dbc) {
		return dbc.find().sort(new BasicDBObject("Name", 1));
	}

	public DBCursor sort_by_college(DBCollection dbc) {
		return dbc.find().sort(new BasicDBObject("College", 1));

	}

	public DBCursor sort_by_roll(DBCollection dbc, char year) {
		return dbc.find().sort(new BasicDBObject(year + ".Roll", 1));
	}

	public DBCursor sort_by_scored(DBCollection dbc, char year) {
		return dbc.find().sort(new BasicDBObject(year + ".Scored", -1));
	}

	public DBCursor sort_by_subject(String sub_name, char year, DBCollection dbc) {
		return dbc.find().sort(
				new BasicDBObject(year + "." + sub_name + "." + "Scored", -1));
	}
}