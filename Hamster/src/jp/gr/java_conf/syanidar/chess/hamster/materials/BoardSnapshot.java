package jp.gr.java_conf.syanidar.chess.hamster.materials;

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
