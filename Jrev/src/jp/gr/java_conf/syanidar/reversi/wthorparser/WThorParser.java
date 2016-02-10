package jp.gr.java_conf.syanidar.reversi.wthorparser;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class WThorParser {
	public static final void main(String args[]){
		String name = args[0];
	
		FileInputStream input = null;
		BufferedWriter writer = null;
		try {
			input = new FileInputStream(name + ".wtb");
			writer = new BufferedWriter(new FileWriter(name + ".txt", true));
			
			input.skip(16);
			
			for(int i = input.read(); i != -1;i = input.read()){
				input.skip(7);
				StringBuilder builder = new StringBuilder();
				for(int j = 0; j < 60; j++){
					int move = input.read();
					builder.append(parseMove(move));
				}
				builder.append("\n");
				writer.write(builder.toString());
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				input.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	}
	private static final String parseMove(int move){
		if(move == 0)return "";
		int row = move % 10;
		int column = (move - row) / 10;
		
		char[] result = new char[2];
		result[0] = (char)('A' + row - 1);
		result[1] = (char)('1' + column - 1);
				
		return new String(result);
	}
}
