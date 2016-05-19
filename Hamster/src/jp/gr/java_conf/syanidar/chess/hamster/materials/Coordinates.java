package jp.gr.java_conf.syanidar.chess.hamster.materials;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.FileEnum.*;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.RankEnum.*;

import jp.gr.java_conf.syanidar.util.collection.Range;

public class Coordinates {
	private static final Coordinates[] INSTANCES = instances();
	private final int column;
	private final int row;
	
	private Coordinates(int column, int row){this.column = column; this.row = row;}

	public static final Coordinates of(int column, int row){
		return INSTANCES[column * 8 + row];
	}
	public final int column(){return column;}
	public final int row(){return row;}
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Coordinates))return false;
		return column == ((Coordinates)o).column && row == ((Coordinates)o).row;
	}
	@Override
	public int hashCode(){
		int result = 17;
		result = result * 31 + column;
		result = result * 31 + row;
		
		return result;
	}
	public FileEnum file(){
		switch(column){
		case 0:return A;
		case 1:return B;
		case 2:return C;
		case 3:return D;
		case 4:return E;
		case 5:return F;
		case 6:return G;
		case 7:return H;
		default:
			throw new AssertionError();
		}
	}
	public RankEnum rank(){
		switch(row){
		case 0:return FIRST;
		case 1:return SECOND;
		case 2:return THIRD;
		case 3:return FORTH;
		case 4:return FIFTH;
		case 5:return SIXTH;
		case 6:return SEVENTH;
		case 7:return EIGHTH;
		default:
			throw new AssertionError();
		}
	}
	public boolean isOnFile(FileEnum file){
		return file.involves(this);
	}
	public boolean isOnRank(RankEnum rank){
		return rank.involves(this);
	}
	public String toAlgebraicNotation(){
		StringBuilder sb = new StringBuilder();
		sb.append((char)('a' + column));
		sb.append(row + 1);
		return sb.toString();
	}
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(column);
		builder.append(", ");
		builder.append(row);
		builder.append(")");
		return builder.toString();
	}
	
	private static final Coordinates[] instances(){
		Coordinates[] result = new Coordinates[64];
		for(int column : Range.of(8)){
			for(int row : Range.of(8)){
				result[column * 8 + row] = new Coordinates(column, row);
			}
		}
		return result;
	}
}
