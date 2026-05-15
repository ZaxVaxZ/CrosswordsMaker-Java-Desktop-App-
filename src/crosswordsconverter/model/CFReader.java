package crosswordsconverter.model;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import crosswordsconverter.structures.CrosswordsFileObject;

public class CFReader {
	public static CrosswordsFileObject Read(String full_path) throws Exception {
		File f = new File(full_path);
		byte[] signature = {0x00, 0x00, 0x45, 0x4A, 0x43, 0x46}; 
		byte[] end_signature = {0x46, 0x43, 0x4A, 0x45, 0x00, 0x00};
		byte[] sig_reader = new byte[6];
		byte[] int_reader = new byte[4];
		int[] table_rows, reversed_hi, reversed_vi;
		int width, height;
		boolean[][] table_bool, reverse_h, reverse_v;
		String[][][] horizontal;
		String[][][] vertical;
		FileInputStream fr = new FileInputStream(f);
		fr.read(sig_reader);
		for(int i=0;i<6;i++) {
			if(sig_reader[i]!=signature[i]) {
				throw new Exception("The file is corrupted!");
			}
		}
		fr.read(int_reader);
		width = makeInteger(int_reader);
		fr.read(int_reader);
		height = makeInteger(int_reader);
		horizontal = new String[height][width][3];
		vertical = new String[height][width][3];
		table_rows = new int[height];
		for(int i=0;i<height;i++) {
			fr.read(int_reader);
			table_rows[i] = makeInteger(int_reader);
		}
		table_bool = CrosswordsFileObject.int_arr_to_bool(width, table_rows);
		reversed_hi = new int[height];
		for(int i=0;i<height;i++) {
			fr.read(int_reader);
			reversed_hi[i] = makeInteger(int_reader);
		}
		reverse_h = CrosswordsFileObject.int_arr_to_bool(width, reversed_hi);
		reversed_vi = new int[height];
		for(int i=0;i<height;i++) {
			fr.read(int_reader);
			reversed_vi[i] = makeInteger(int_reader);
		}
		reverse_v = CrosswordsFileObject.int_arr_to_bool(width, reversed_vi);
		for(int i=0;i<height;i++) {
			fr.read(int_reader);
			int word_count = makeInteger(int_reader);
			if(word_count==0) {
				for(int j=0;j<width;j++) {
					for(int k=0;k<3;k++) {
						horizontal[i][j][k] = "";
					}
				}
				continue;
			}
			for(int j=0;j<word_count;j++) {
				for(int k=0;k<3;k++) {
					fr.read(int_reader);
					int len = makeInteger(int_reader);
					byte[] buff = new byte[len];
					fr.read(buff);
					horizontal[i][j][k] = new String(buff, StandardCharsets.UTF_16);
				}
			}
			for(int j=word_count;j<width;j++) {
				for(int k=0;k<3;k++) {
					horizontal[i][j][k] = "";
				}
			}
		}
		for(int i=0;i<width;i++) {
			fr.read(int_reader);
			int word_count = makeInteger(int_reader);
			if(word_count==0) {
				for(int j=0;j<height;j++) {
					for(int k=0;k<3;k++) {
						vertical[j][i][k] = "";
					}
				}
				continue;
			}
			for(int j=0;j<word_count;j++) {
				for(int k=0;k<3;k++) {
					fr.read(int_reader);
					int len = makeInteger(int_reader);
					byte[] buff = new byte[len];
					fr.read(buff);
					vertical[j][i][k] = new String(buff, StandardCharsets.UTF_16);
				}
			}
			for(int j=word_count;j<height;j++) {
				for(int k=0;k<3;k++) {
					vertical[j][i][k] = "";
				}
			}
		}
		fr.read(sig_reader);
		for(int i=0;i<6;i++) {
			if(sig_reader[i]!=end_signature[i]) {
				throw new Exception("The file is corrupted!");
			}
		}
		fr.close();		
		CrosswordsFileObject cfo = new CrosswordsFileObject(table_bool, horizontal, vertical, reverse_h, reverse_v);
		return cfo;
	}
	private static int makeInteger(byte[] byarr) {
		int x=0;
		for(int i=0;i<4;i++) {
			x += ((byarr[i]<0?256+byarr[i]:byarr[i])<<(8*(3-i)));
		}
		return x;
	}
}
