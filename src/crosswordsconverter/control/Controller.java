package crosswordsconverter.control;

import crosswordsconverter.model.CFReader;
import crosswordsconverter.model.CFWriter;
import crosswordsconverter.structures.CrosswordsFileObject;
import crosswordsconverter.structures.ReqID;

public class Controller {
	static CrosswordsFileObject cfo=null;
	static boolean[][] table=null, reverse_h=null, reverse_v=null;
	static String[][][] horizontal=null, vertical=null;
	public static void Request(ReqID id, String path) throws Exception {
		switch(id) {
		case SAVE:
			cfo = new CrosswordsFileObject(table, horizontal, vertical, reverse_h, reverse_v);
			CFWriter.Write(path, cfo);
			CFWriter.WriteHTML(path, cfo);
			break;
		case LOAD:
			cfo = CFReader.Read(path);
			table = cfo.get_Table_rows_as_bool();
			reverse_h = cfo.get_reversed_h_as_bool();
			reverse_v = cfo.get_reversed_v_as_bool();
			horizontal = cfo.get_Horizontal();
			vertical = cfo.get_Vertical();
			break;
		}
	}
	public static void set_table(boolean[][] table) {
		Controller.table = table;
	}
	public static void set_reverse_h(boolean[][] rev_h) {
		Controller.reverse_h = rev_h;
	}
	public static void set_reverse_v(boolean[][] rev_v) {
		Controller.reverse_v = rev_v;
	}
	public static void set_horizontal(String[][][] horizontal) {
		Controller.horizontal = horizontal;
	}
	public static void set_vertical(String[][][] vertical) {
		Controller.vertical = vertical;
	}
	public static boolean[][] get_table() {
		return table;
	}
	public static boolean[][] get_reverse_h() {
		return reverse_h;
	}
	public static boolean[][] get_reverse_v() {
		return reverse_v;
	}
	public static String[][][] get_horizontal() {
		return horizontal;
	}
	public static String[][][] get_vertical() {
		return vertical;
	}
}
