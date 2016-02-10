package jp.gr.java_conf.syanidar.reversi.positionserializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;

import jp.gr.java_conf.syanidar.reversi.Board;

public class PositionSerializer {

	public static void main(String[] args) {
		File directory = new File(args[0]);
		File[] files = directory.listFiles();
		File destination = new File(args[1]);
		BufferedReader reader = null;
		try {
			for(File file : files){
				reader = new BufferedReader(new FileReader(file));
				String position = reader.readLine();
				long black = Long.parseUnsignedLong(position.chars().map(c -> c == 'X' ? 1 : 0).collect(
						() -> new StringBuilder(),
						(builder, bit) -> builder.append(bit),
						(builder1, builder2) -> builder1.append(builder2)).toString(), 2);
				long white = Long.parseUnsignedLong(position.chars().map(c -> c == 'O' ? 1 : 0).collect(
						() -> new StringBuilder(),
						(builder, bit) -> builder.append(bit),
						(builder1, builder2) -> builder1.append(builder2)).toString(), 2);
				int colorToPlay = reader.readLine().equals("Black") ? Board.BLACK : Board.WHITE;
				Board board = new Board(colorToPlay, black, white);
				File output = new File(destination, new StringTokenizer(file.getName(), ".").nextToken() + ".ser");
				ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(output));
				stream.writeObject(board);
				stream.close();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid position file.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("File not found.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("An exception occured while operating files");
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
