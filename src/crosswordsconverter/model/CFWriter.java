package crosswordsconverter.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

import crosswordsconverter.structures.CrosswordsFileObject;

public class CFWriter {
	public static void Write(String full_path, CrosswordsFileObject cfo) throws Exception {
		File f = new File(full_path);
		FileOutputStream fw = new FileOutputStream(f);
		fw.write(cfo.get_File_signature());
		fw.write(bytify(cfo.get_Width()));
		fw.write(bytify(cfo.get_Height()));
		for(int row: cfo.get_Table_rows_as_int()) {
			fw.write(bytify(row));
		}
		for(int rev_h: cfo.get_reversed_h_as_int()) {
			fw.write(bytify(rev_h));
		}
		for(int rev_v: cfo.get_reversed_v_as_int()) {
			fw.write(bytify(rev_v));
		}
		for(int i=0;i<cfo.get_Height();i++) {
			int row_words = cfo.get_Row_words()[i];
			fw.write(bytify(row_words));
			for(int j=0;j<row_words;j++) {
				for(int k=0;k<3;k++) {
					fw.write(bytify(cfo.get_Horizontal()[i][j][k].getBytes("UTF-16").length));
					fw.write(cfo.get_Horizontal()[i][j][k].getBytes("UTF-16"));
				}
			}
		}
		for(int j=0;j<cfo.get_Width();j++) {
			int col_words = cfo.get_Col_words()[j];
			fw.write(bytify(col_words));
			for(int i=0;i<col_words;i++) {
				for(int k=0;k<3;k++) {
					fw.write(bytify(cfo.get_Vertical()[i][j][k].getBytes("UTF-16").length));
					fw.write(cfo.get_Vertical()[i][j][k].getBytes("UTF-16"));
				}
			}
		}
		fw.write(cfo.get_File_end_signature());
		fw.flush();
		fw.close();		
	}
	private static byte[] bytify(int x) {
		byte[] byarr = new byte[4];
		for(int i=0;i<4;i++) {
			byarr[i] = (byte)(0xFF&(x>>(8*(3-i))));
		}
		return byarr;
	}
	public static String toTXTfile(String path) {
		int ind = path.lastIndexOf(".");
		return path.substring(0, ind)+".txt";
	}
	public static String hexify(byte b) {
		String s = "";
		int n=0;
		for(int i=4;i<8;i++) {
			n+=((b>>i)&1)*Math.pow(2, i-4);
		}
		if(n > 9) {
			s += (char)(65+n-10);
		}
		else {
			s += n;
		}
		n=0;
		for(int i=0;i<4;i++) {
			n+=((b>>i)&1)*Math.pow(2, i);
		}
		if(n > 9) {
			s += (char)(65+n-10);
		}
		else {
			s += n;
		}
		return s;
	}
	public static void WriteHTML(String path, CrosswordsFileObject cfo) throws Exception {
		String txt_path = toTXTfile(path);
		File ccfo = new File(path);
		File txt = new File(txt_path);
		byte[] contents = new byte[(int)ccfo.length()];
		FileInputStream fr = new FileInputStream(ccfo);
		fr.read(contents);
		fr.close();
		FileWriter fw = new FileWriter(txt);
		fw.write("<script>\n");
		fw.append("var ccfo = \n\"");
		for(int i=0;i<contents.length;i++) {
			if(i%32==0&&i>0) {
				fw.append("\" +\n\"");
			}
			fw.append(hexify(contents[i]));
		}
		fw.append("\";\n");
		fw.append("</script>");
		fw.close();
	}
}
