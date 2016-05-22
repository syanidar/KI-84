package jp.gr.java_conf.syanidar.games.chess.rat;

import static jp.gr.java_conf.syanidar.games.chess.rat.Position.BISHOP;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.BLACK;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.KING;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.KNIGHT;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.PAWN;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.QUEEN;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.ROOK;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.WHITE;

import java.util.StringTokenizer;

import jp.gr.java_conf.syanidar.games.chess.rat.Position.PositionBuilder;
import jp.gr.java_conf.syanidar.util.collection.Range;

public class FENManager {
	private static final int K = 0;
	private static final int Q = 1;
	private static final int k = 2;
	private static final int q = 3;

	

	private final String fen;
	private final String placement;
	private final String color;
	private final String castlingAvailability;
	private final String enPassantSquare;
	private final String halfMoveClock;
	private final String fullMoveNumber;
			
	
	
	public FENManager(String arg){
		if(arg == null)throw new IllegalArgumentException("arg = null");
		StringTokenizer st = new StringTokenizer(arg, " ");
		if(st.countTokens() != 6)throw new IllegalArgumentException("For input string = " + arg);
		
		placement = st.nextToken();
		color = st.nextToken();		
		castlingAvailability = st.nextToken();
		enPassantSquare = st.nextToken();
		halfMoveClock = st.nextToken();
		fullMoveNumber = st.nextToken();

		if(color.length() != 1 || !(color.equals("w") || color.equals("b")))throw new IllegalArgumentException("color = " + color);
		if(castlingAvailability.length() > 4)throw new IllegalArgumentException("castling availability = " + castlingAvailability);
		if(!enPassantSquare.equals("-") && enPassantSquare.length() != 2)throw new IllegalArgumentException("en passant target square = " + enPassantSquare);
		if(!isNaturalNumber(halfMoveClock))throw new IllegalArgumentException("half move clock = " + halfMoveClock);
		if(!isNaturalNumber(fullMoveNumber))throw new IllegalArgumentException("full move number = " + fullMoveNumber);
		fen = arg;
	}
	
	

	public final Position newPosition(){
		Position.PositionBuilder pb = new PositionBuilder(color(), fullMoveNumber(), army());
		boolean[] castlingAvailability = castlingAvailability();
		if(!castlingAvailability[K]){pb = pb.disableWhiteKingsideCastling();}
		if(!castlingAvailability[Q]){pb = pb.disableWhiteQueensideCastling();}
		if(!castlingAvailability[k]){pb = pb.disableBlackKingsideCastling();}
		if(!castlingAvailability[q]){pb = pb.disableBlackQueensideCastling();}
		return pb.enPassantSquare(enPassantSquare()).halfMoveClock(halfMoveClock()).build();
	}
	@Override
	public String toString(){
		return fen;
	}


	
	private final long[][] army(){
		int index = 63;
		char[] chars = placement.toCharArray();
		long[][] result = new long[2][6];
		for(int i : Range.of(chars.length)){
			long square = 1L << index;
			switch(chars[i]){
			case 'P':result[WHITE][PAWN] |= square;	index--;	break;
			case 'N':result[WHITE][KNIGHT] |= square;	index--;	break;
			case 'B':result[WHITE][BISHOP] |= square;	index--;	break;
			case 'R':result[WHITE][ROOK] |= square;	index--;	break;
			case 'Q':result[WHITE][QUEEN] |= square;	index--;	break;
			case 'K':result[WHITE][KING] |= square;	index--;	break;
			case 'p':result[BLACK][PAWN] |= square;	index--;	break;
			case 'n':result[BLACK][KNIGHT] |= square;	index--;	break;
			case 'b':result[BLACK][BISHOP] |= square;	index--;	break;
			case 'r':result[BLACK][ROOK] |= square;	index--;	break;
			case 'q':result[BLACK][QUEEN] |= square;	index--;	break;
			case 'k':result[BLACK][KING] |= square;	index--;	break;
			default:
				if(Character.isDigit(chars[i])){index -= (int)(chars[i] - '0');}
				else if(chars[i] != '/')throw new IllegalArgumentException("piece placement = " + placement);
				break;
			}
		}
		return result;
	}
	private final int color(){
		return color.equals("w") ? WHITE : BLACK;
	}
	private final boolean[] castlingAvailability(){
		boolean[] result = new boolean[4];
		for(char c : castlingAvailability.toCharArray()){
			switch(c){
			case 'K':result[K] = true;	break;
			case 'Q':result[Q] = true;	break;
			case 'k':result[k] = true;	break;
			case 'q':result[q] = true;	break;
			case '-':					break;
			default:throw new IllegalArgumentException("castling availability = " + castlingAvailability);
			}
		}
		return result;
	}
	private final long enPassantSquare(){
		if(enPassantSquare.equals("-"))return 0;
		char[] chars = enPassantSquare.toCharArray();
		long result = 1;
		if(chars[0] < 'a' || chars[0] > 'h')throw new IllegalArgumentException("en pasant target square = " + enPassantSquare);
		result <<= (int)(7 - chars[0] + 'a');
		if(chars[1] < '1' || chars[1] > '8')throw new IllegalArgumentException("en pasant target square = " + enPassantSquare);
		result <<= (int)(chars[1] - '1') * 8;
		
		return result;
	}
	private final int halfMoveClock(){
		if(halfMoveClock.equals("-"))return 0;
		return Integer.valueOf(halfMoveClock);
	}
	private final int fullMoveNumber(){
		return Integer.valueOf(fullMoveNumber);
	}
	private static final boolean isNaturalNumber(String s){
		for(char c : s.toCharArray()){
			if(!Character.isDigit(c))return false;
		}
		return true;
	}
}
