package crosswordsconverter.structures;

public class CrosswordsFileObject {
	private byte[] file_signature;		 ////
	private int height, width;			 ////
	private int[] table_rows;			 ////
	private int[] row_words;			 ////
	private int[] col_words;			 ////
	private int[] reversed_h;
	private int[] reversed_v;
	private String[][][] horizontal;	 ////
	private String[][][] vertical;		 ////
	private byte[] file_end_signature;	 ////
	public CrosswordsFileObject(boolean blacked[][], String text_horizontal[][][], String text_vertical[][][], boolean rev_h[][], boolean rev_v[][]) {
		byte[] fsig = {0x00, 0x00, 0x45, 0x4A, 0x43, 0x46};
		file_signature = fsig;
		byte[] fesig = {0x46, 0x43, 0x4A, 0x45, 0x00, 0x00};
		file_end_signature = fesig;
		final int HEIGHT = blacked.length;
		final int WIDTH = blacked[0].length;
		width = WIDTH;
		height = HEIGHT;
		int num;
		table_rows = new int[HEIGHT];
		for(int i=0;i<HEIGHT;i++) {
			num = 0;
			for(int j=0;j<WIDTH;j++) {
				if(blacked[i][j]) {
					num += Math.pow(2, j);
				}
			}
			table_rows[i] = num;
		}
		reversed_h = new int[HEIGHT];
		for(int i=0;i<HEIGHT;i++) {
			num = 0;
			for(int j=0;j<WIDTH;j++) {
				if(rev_h[i][j]) {
					num += Math.pow(2, j);
				}
			}
			reversed_h[i] = num;
		}
		reversed_v = new int[HEIGHT];
		for(int i=0;i<HEIGHT;i++) {
			num = 0;
			for(int j=0;j<WIDTH;j++) {
				if(rev_v[i][j]) {
					num += Math.pow(2, j);
				}
			}
			reversed_v[i] = num;
		}
		row_words = new int[HEIGHT];
		col_words = new int[WIDTH];
		for(int i=0;i<HEIGHT;i++) {
			row_words[i] = 0;
			for(int j=0;j<WIDTH;j++) {
				if(!text_horizontal[i][j][0].equals("")) {
					row_words[i]++;
				}
				if(!text_vertical[i][j][0].equals("")) {
					col_words[j]++;
				}
			}
		}
		horizontal = text_horizontal.clone();
		vertical = text_vertical.clone();
	}
	public byte[] get_File_signature() {
		return file_signature;
	}
	public int get_Height() {
		return height;
	}
	public int get_Width() {
		return width;
	}
	public int[] get_Table_rows_as_int() {
		return table_rows;
	}
	public boolean[][] get_reversed_h_as_bool() {
		boolean[][] barr = new boolean[height][width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				barr[i][j] = ((reversed_h[i]>>j)&1)==1;
			}
		}
		return barr;
	}
	public int[] get_reversed_h_as_int() {
		return reversed_h;
	}
	public boolean[][] get_reversed_v_as_bool() {
		boolean[][] barr = new boolean[height][width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				barr[i][j] = ((reversed_v[i]>>j)&1)==1;
			}
		}
		return barr;
	}
	public int[] get_reversed_v_as_int() {
		return reversed_v;
	}
	public boolean[][] get_Table_rows_as_bool() {
		boolean[][] barr = new boolean[height][width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				barr[i][j] = ((table_rows[i]>>j)&1)==1;
			}
		}
		return barr;
	}
	public static boolean[][] int_arr_to_bool(int row_width, int[] rows) {
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
	public int[] get_Row_words() {
		return row_words;
	}
	public int[] get_Col_words() {
		return col_words;
	}
	public String[][][] get_Horizontal() {
		return horizontal;
	}
	public String[][][] get_Vertical() {
		return vertical;
	}
	public byte[] get_File_end_signature() {
		return file_end_signature;
	}
}
