package jp.gr.java_conf.syanidar.chess.hamster.materials;

import java.util.function.Function;

public class BoardSnapshot {
	private final long wPawns;
	private final long wKnights;
	private final long wBishops;
	private final long wRooks;
	private final long wQueens;
	private final long wKing;
	private final long bPawns;
	private final long bKnights;
	private final long bBishops;
	private final long bRooks;
	private final long bQueens;
	private final long bKing;
	
	BoardSnapshot(long wp, long wn, long wb, long wr, long wq, long wk, long bp, long bn, long bb, long br, long bq, long bk){
		wPawns = wp;
		wKnights = wn;
		wBishops = wb;
		wRooks = wr;
		wQueens = wq;
		wKing = wk;
		bPawns = bp;
		bKnights = bn;
		bBishops = bb;
		bRooks = br;
		bQueens = bq;
		bKing = bk;	
	}
	public Board newBoard(){
		return new Board(toFEN());
	}
	public String toFEN(){
		Function<Long, String> conv = square -> {
			if((wPawns & square) != 0)return "P";
			if((wKnights & square) != 0)return "N";
			if((wBishops & square) != 0)return "B";
			if((wRooks & square) != 0)return "R";
			if((wQueens & square) != 0)return "Q";
			if((wKing & square) != 0)return "K";
			if((bPawns & square) != 0)return "p";
			if((bKnights & square) != 0)return "n";
			if((bBishops & square) != 0)return "b";
			if((bRooks & square) != 0)return "r";
			if((bQueens & square) != 0)return "q";
			if((bKing & square) != 0)return "k";
			throw new AssertionError();
		};
		long occupationMask = wPawns | wKnights | wBishops | wRooks | wQueens | wKing | bPawns | bKnights | bBishops | bRooks | bQueens | bKing;
		int emptyCount = 0;
		StringBuilder sb = new StringBuilder();
		for(long square = 0x8000000000000000L; square != 0; square = square >>> 1){
			if((occupationMask & square) != 0){
				if(emptyCount != 0){
					sb.append(emptyCount);
					emptyCount = 0;
				}
				sb.append(conv.apply(square));
			}else{
				emptyCount++;
			}
			
			if((square & 0x0101010101010100L) != 0){
				if(emptyCount != 0){
					sb.append(emptyCount);
				}
				sb.append("/");
				emptyCount = 0;
			}
		}
		return sb.toString();
	}
	@Override
	public int hashCode() {
		long result = 17;
		result = result * 31 + wPawns;
		result = result * 31 + wKnights;
		result = result * 31 + wBishops;
		result = result * 31 + wRooks;
		result = result * 31 + wQueens;
		result = result * 31 + wKing;
		result = result * 31 + bPawns;
		result = result * 31 + bKnights;
		result = result * 31 + bBishops;
		result = result * 31 + bRooks;
		result = result * 31 + bQueens;
		result = result * 31 + bKing;

		return (int)result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		if(!(obj instanceof BoardSnapshot))return false;
		
		BoardSnapshot b = (BoardSnapshot)obj;
		
		return b.wPawns == wPawns && b.wKnights == wKnights && b.wBishops == wBishops && b.wRooks == wRooks && b.wQueens == wQueens && b.wKing == wKing && b.bPawns == bPawns && b.bKnights == b.bKnights && b.bBishops == bBishops && b.bRooks == bRooks && b.bQueens == bQueens && b.bKing == bKing;
	}
	
}
