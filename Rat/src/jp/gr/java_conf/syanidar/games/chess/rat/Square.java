package jp.gr.java_conf.syanidar.games.chess.rat;

import java.util.Iterator;

public final class Square {
	private final File file;
	private final Rank rank;
	
	
	
	public Square(File file, Rank rank){
		this.file = file;
		this.rank = rank;
	}
	
	
	
	public File file(){return file;}
	public Rank rank(){return rank;}
	@Override
	public String toString(){
		return file.toString() + rank().toString();
	}
	
	
	
	public static final Iterator<Square> fromA8ToH1(){
		return new Iterator<Square>(){
			private int index = 0;
			@Override
			public boolean hasNext() {
				return index < 64;
			}
			@Override
			public Square next() {
				Square result = new Square(File.values()[file()], Rank.values()[rank()]);
				index++;
				return result;
			}
			
			
			
			private int file(){
				return index & 7;
			}
			private int rank(){
				return 7 - index / 8;
			}
		};
	}
}
