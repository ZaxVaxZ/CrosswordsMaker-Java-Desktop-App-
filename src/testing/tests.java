package testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class tests {
	public static void main(String args[]) {
		System.out.println(hexify((byte)171));
		System.out.println(hexify((byte)237));
		if(true) return;
		String s = "123";
		System.out.println(s.substring(2, 3));
		if(true) return;
//		if(true) return;
		FileOutputStream fw = null;
		try {
			s = new String("⁄„«œ".getBytes("UTF-16"), StandardCharsets.UTF_16);
			System.out.println(s.length());
			if(true) return;
			System.out.println(s.getBytes("UTF-16").length);
			fw = new FileOutputStream(new File("Test.txt"));
			fw.write(s.getBytes("UTF-16"));
			fw.flush();
		} catch(Exception e) {
			System.err.println(e);
		} finally {
			try {
				if(fw!=null) fw.close();
			} catch(Exception e) {
				System.err.println(e);
			}
		}
		if(true) return;
//		String s = "";
////		if(true) return;
//		FileOutputStream fw = null;
//		OutputStreamWriter osw = null;
//		try {
//			s = new String(" ‰„".getBytes("UTF-16"), StandardCharsets.UTF_16);
//			System.out.println(s);
//			fw = new FileOutputStream(new File("Test.txt"));
//			osw = new OutputStreamWriter(fw, StandardCharsets.UTF_16);
//			osw.write(s);
//			osw.flush();
//		} catch(Exception e) {
//			System.err.println(e);
//		} finally {
//			try {
//				if(fw!=null) fw.close();
//				if(osw!=null) {
//					osw.close();
//				}
//			} catch(Exception e) {
//				System.err.println(e);
//			}
//		}
//		if(true) return;
		FileInputStream fr = null;
//		InputStreamReader isr = null;
		try {
			fr = new FileInputStream(new File("Test.txt"));
//			isr = new InputStreamReader(fr, StandardCharsets.UTF_8);
			byte[] b = new byte[10];
			fr.read(b);
			System.out.println(new String(b, StandardCharsets.UTF_16));
		} catch(Exception e) {
			System.err.println(e);
		} finally {
			try {
				if(fr!=null) fr.close();
			} catch(Exception e) {
				System.err.println(e);
			}
		}
		return;
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
	private static byte[] bytify(int x) {
		byte[] byarr = new byte[4];
		for(int i=0;i<4;i++) {
			byarr[i] = (byte)(0xFF&(x>>(8*(3-i))));
		}
		return byarr;
	}
	private static int makeInteger(byte[] byarr) {
		int x=0;
		for(int i=0;i<4;i++) {
			x += ((byarr[i]<0?256+byarr[i]:byarr[i])<<(8*(3-i)));
		}
		return x;
	}
	private static byte[] bytify(String x) {
		byte[] byarr = new byte[x.length()];
		for(int i=0;i<x.length();i++) {
			byarr[i] = (byte)x.charAt(i);
		}
		return byarr;
	}
	private static String makeString(byte[] byarr) {
		String s="";
		for(int i=0;i<byarr.length;i++) {
			s += (char) byarr[i];
		}
		return s;
	}
	private static int[] int_arr_from_bool(boolean[][] arr) {
		int HEIGHT = arr.length;
		int WIDTH = arr[0].length;
		int num, table_rows[] = new int[HEIGHT];
		for(int i=0;i<HEIGHT;i++) {
			num = 0;
			for(int j=0;j<WIDTH;j++) {
				if(arr[i][j]) {
					num += Math.pow(2, j);
				}
			}
			table_rows[i] = num;
		}
		return table_rows;
	}
	public static boolean[][] get_Table_rows_as_bool(int row_width, int[] rows) {
		int h = rows.length;
		int w = row_width;
		boolean[][] barr = new boolean[h][w];
		for(int i=0;i<h;i++) {
			for(int j=0;j<w;j++) {
				barr[i][j] = ((rows[i]>>j)&1)==1;
			}
		}
		return barr;
	}
}
