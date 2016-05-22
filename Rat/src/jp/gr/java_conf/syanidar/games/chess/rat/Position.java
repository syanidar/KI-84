package jp.gr.java_conf.syanidar.games.chess.rat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import jp.gr.java_conf.syanidar.util.bitwise.LongBits;
import jp.gr.java_conf.syanidar.util.collection.Range;
import jp.gr.java_conf.syanidar.util.function.Undoable;

public final class Position implements jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position{			
	static final class PositionBuilder{
		private boolean whiteCanCastleOnKingside;
		private boolean whiteCanCastleOnQueenside;
		private boolean blackCanCastleOnKingside;
		private boolean blackCanCastleOnQueenside;
		private long enPassantSquare;
		private int halfMoveClock;
		
		private final int color;
		private final int fullMoveNumber;
		private final long[][] army;
		
		PositionBuilder(int color, int fullMoveNumber, long[][] army){
			assert fullMoveNumber > 0;
			this.color = color;
			this.fullMoveNumber = fullMoveNumber;
			this.army = army;
			
			whiteCanCastleOnKingside = true;
			whiteCanCastleOnQueenside = true;
			blackCanCastleOnKingside = true;
			blackCanCastleOnQueenside = true;
		}
		PositionBuilder disableWhiteKingsideCastling(){
			whiteCanCastleOnKingside = false;
			return this;
		}
		PositionBuilder disableWhiteQueensideCastling(){
			whiteCanCastleOnQueenside = false;
			return this;
		}
		PositionBuilder disableBlackKingsideCastling(){
			blackCanCastleOnKingside = false;
			return this;
		}
		PositionBuilder disableBlackQueensideCastling(){
			blackCanCastleOnQueenside = false;
			return this;
		}
		PositionBuilder enPassantSquare(long e){
			enPassantSquare = e;
			return this;
		}
		PositionBuilder halfMoveClock(int h){
			halfMoveClock = h;
			return this;
		}
		Position build(){
			return new Position(this);
		}
	}
	
	
	
	
	
	
	
	
	
	
	static final int PAWN =	0;//These constants are used to specify the bit board in the array.
	static final int KNIGHT =	1;
	static final int BISHOP =	2;
	static final int ROOK =	3;
	static final int QUEEN =	4;
	static final int KING =	5;
	static final int EMPTY =	6;
	static final int[] PIECES = {PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING};
	static final int[] PROMOTEES = {KNIGHT, BISHOP, ROOK, QUEEN};
	
	static final int WHITE =	0;//These constants are used to point the array in which the bit boards of that color are stored.
	static final int BLACK =	1;
	static final int[] COLORS = {WHITE, BLACK};
	
	static final long A_FILE =							0x8080808080808080L;
	static final long B_FILE =							0x4040404040404040L;
	static final long C_FILE =							0x2020202020202020L;
	static final long D_FILE =							0x1010101010101010L;
	static final long E_FILE =							0x808080808080808L;
	static final long F_FILE =							0x404040404040404L;
	static final long G_FILE =							0x202020202020202L;
	static final long H_FILE =							0x101010101010101L;
	static final long INITIAL_WHITE_KING =				0x8;//These constants are the values which represent an area or a location of a piece that are frequently referred to.
	static final long INITIAL_BLACK_KING =				0x800000000000000L;
	static final long INITIAL_WHITE_KING_SIDE_ROOK =	0x1;
	static final long INITIAL_WHITE_QUEEN_SIDE_ROOK =	0x80;
	static final long INITIAL_BLACK_KING_SIDE_ROOK =	0x100000000000000L;
	static final long INITIAL_BLACK_QUEEN_SIDE_ROOK =	0x8000000000000000L;
	static final long INITIAL_WHITE_PAWNS =				0xFF00;
	static final long INITIAL_BLACK_PAWNS =				0xFF000000000000L;
	static final long BACK_RANKS =						0xFF000000000000FFL;
	static final long WHITE_KING_SIDE_CASTLING_WAY =	0x6;
	static final long WHITE_QUEEN_SIDE_CASTLING_WAY =	0x70;
	static final long BLACK_KING_SIDE_CASLING_WAY =		0x600000000000000L;
	static final long BLACK_QUEEN_SIDE_CASLING_WAY =	0x7000000000000000L;
	static final long WHITE_SQUARES =						0xAAAAAAAAAAAAAAAAL;
	static final long BLACK_SQUARES =						0x5555555555555555L;
	
	
	
	private static final long[] RANDOMS = randoms(0x0120442012L);

	
	
	private boolean whiteCanCastleOnKingside;
	private boolean whiteCanCastleOnQueenside;
	private boolean blackCanCastleOnKingside;
	private boolean blackCanCastleOnQueenside;
	private long enPassantSquare;
	private int color;
	private int oppositeColor;
	private int halfMoveClock;
	private int plyCount;
	private long hash;
	private final long[][] army;//stores bit boards in which the most significant bit points at "a8" and the least significant bit does so at "h1".
	private final long[] occupancies;
	private final Deque<Long> hashies;
	
	
	
	public Position(){
		oppositeColor = BLACK;
		whiteCanCastleOnKingside = true;
		whiteCanCastleOnQueenside = true;
		blackCanCastleOnKingside = true;
		blackCanCastleOnQueenside = true;
		army = new long[2][6];
		
		
		army[WHITE][PAWN] = 0xFF00;
		army[WHITE][KNIGHT] = 0x42;
		army[WHITE][BISHOP] = 0x24;
		army[WHITE][ROOK] = 0x81;
		army[WHITE][QUEEN] = 0x10;
		army[WHITE][KING] = 0x8;
		army[BLACK][PAWN] = 0x00FF000000000000L;
		army[BLACK][KNIGHT] = 0x4200000000000000L;
		army[BLACK][BISHOP] = 0x2400000000000000L;
		army[BLACK][ROOK] = 0x8100000000000000L;
		army[BLACK][QUEEN] = 0x1000000000000000L;
		army[BLACK][KING] = 0x0800000000000000L;
		
		occupancies = new long[2];
		occupancies[WHITE] = 0xFFFFL;
		occupancies[BLACK] = 0xFFFF000000000000L;
		
		initializeHash();
		hashies = new ArrayDeque<>(150);
		hashies.addLast(hash);
	}
	private Position(PositionBuilder pb){
		whiteCanCastleOnKingside = pb.whiteCanCastleOnKingside;
		whiteCanCastleOnQueenside = pb.whiteCanCastleOnQueenside;
		blackCanCastleOnKingside = pb.blackCanCastleOnKingside;
		blackCanCastleOnQueenside = pb.blackCanCastleOnQueenside;
		enPassantSquare = pb.enPassantSquare;
		color = pb.color;
		oppositeColor = color == WHITE? BLACK : WHITE;
		halfMoveClock = pb.halfMoveClock;
		plyCount = (pb.fullMoveNumber - 1) * 2 + (color == BLACK? 1 : 0);
		army = pb.army;
		
		occupancies = new long[2];
		for(int color : COLORS){
			for(int piece : PIECES){
				occupancies[color] |= army[color][piece];
			}
		}
		
		initializeHash();
		hashies = new ArrayDeque<>(150);
		hashies.addLast(hash);
	}
	
	
	public Optional<Chessman> pieceOn(File file, Rank rank){
		int index = (7 - file.ordinal()) + rank.ordinal() * 8;
		long square = 1L << index;
		int piece = pieceOn(square);
		
		if(piece == EMPTY)return Optional.empty();
		else return Optional.of(new Chessman(LongBits.intersects(square, occupancies[WHITE])? Color.WHITE : Color.BLACK, Piece.values()[piece]));
	}
	public Optional<Chessman> pieceOn(Square square){
		return pieceOn(square.file(), square.rank());
	}
	public Color sideToMove(){
		return color == WHITE? Color.WHITE : Color.BLACK;
	}
	@Override
	public List<Move> moves() {
		RawMove[] rawMoves = generateRawMoves();
		Comparator<RawMove> c = DefaultMoveOrderer.getInstance();
		Arrays.sort(rawMoves, c);
		List<Move> result = new ArrayList<>(rawMoves.length);
		for(RawMove m : rawMoves){
			Move move = m.createMove();
			move.play();
			boolean isIllegal = playerChecks();
			move.undo();
			if(!isIllegal){
				result.add(move);
			}
		}
		return result;
	}
	@Override
	public Iterator<Move> moveIterator(boolean isMoveOrderingRequired) {
		RawMove[] rawMoves = generateRawMoves();
		Comparator<RawMove> c = DefaultMoveOrderer.getInstance();
		if(isMoveOrderingRequired){Arrays.sort(rawMoves, c);}
		
		return new Iterator<Move>(){
			private int index = 0;
			private final int size = rawMoves.length;
			private Move current;
			private Optional<Move> next = findNextLegalMove();
			@Override
			public boolean hasNext() {
				return next.isPresent();
			}
			@Override
			public Move next() {
				current = next.get();
				next = findNextLegalMove();
				return current;
			}
			private Optional<Move> findNextLegalMove(){				
				Move move = null;
				while(index < size){
					Move buf = rawMoves[index++].createMove();
					buf.play();
					boolean foundLegalMove = !playerChecks();
					buf.undo();
					if(foundLegalMove){move = buf;	break;}
				}
				return Optional.ofNullable(move);
			}
		};
	}
	@Override
	public boolean theFirstPlayerHasTheMove() {
		return color == WHITE;
	}
	@Override
	public boolean isDrawForced() {
		if(!hasSufficientMatingMaterial())return true;
		if(halfMoveClock > 75)return true;
		if(halfMoveClock == 75 && !playerIsMated())return true;
		return false;
	}
	@Override
	public boolean playerHasRightToDraw() {
		if(fiftyMoveRuleCanBeApplied())return true;
		if(hasRepeatedThreefold())return true;
		
		return false;
	}
	public boolean isQuiet(){
		if(piecesAttackTheSquareOf(occupancies[color]))return false;
		changeTheSide();
		boolean buf = piecesAttackTheSquareOf(occupancies[color]);
		changeTheSide();
		return !buf;
	}
	public boolean isTerminated(){
		return isDrawForced() || !moveIterator(false).hasNext();
	}
	public boolean hasMoves(){
		return moveIterator(false).hasNext();
	}
	public boolean playerIsMated(){
		return playerIsInCheck() && !moveIterator(false).hasNext();
	}
	public boolean isStalemate(){
		return !playerIsInCheck() && !moveIterator(false).hasNext();
	}
	public boolean playerIsInCheck(){
		return piecesAttackTheSquareOf(army[color][KING]);
	}
	public boolean playerChecks(){
		changeTheSide();
		boolean result = playerIsInCheck();
		changeTheSide();
		return result;
	}
	public int fullMoveNumber(){
		return plyCount / 2 + 1;
	}
	public int halfMoveClock(){
		return halfMoveClock;
	}
	public boolean fiftyMoveRuleCanBeApplied(){
		return halfMoveClock >= 50;
	}
	public boolean duplicatesThePreviousPositions(){
		hashies.removeLast();
		boolean result = hashies.contains(hash);
		hashies.addLast(hash);
		return result;
	}
	public boolean hasRepeatedThreefold(){
		if(!duplicatesThePreviousPositions())return false;
		Deque<Long> copy = new LinkedList<>(hashies);
		copy.removeLast();
		copy.remove(hash);
		return copy.contains(hash);
	}
	public boolean hasSufficientMatingMaterial(){
		long occupancy = occupancies[WHITE] | occupancies[BLACK];
		int numOfPieces = Long.bitCount(occupancy);
		
		if(numOfPieces == 2)return false;	//King versus King draws.
		
		long piecesOtherThanKings = occupancy & ~(army[WHITE][KING] | army[BLACK][KING]);
		
		if(numOfPieces == 3){
			int piece = pieceOn(piecesOtherThanKings);
			if(piece == KNIGHT)return false;	//King and Knight versus King draws.
			return true;
		}
		
		long bishops = army[WHITE][BISHOP] | army[BLACK][BISHOP];
		if(piecesOtherThanKings == bishops){	//No pieces but bishops and kings are present.
			if(!LongBits.intersects(WHITE_SQUARES, bishops))return false;		//All the bishops are on black squares.
			if(!LongBits.intersects(BLACK_SQUARES, bishops))return false;	
		}
		
		return true;
	}
	public boolean canCastleOnKingside(){
		if(theFirstPlayerHasTheMove())
		return whiteCanCastleOnKingside &&
				!LongBits.intersects(occupancies[WHITE] | occupancies[BLACK], WHITE_KING_SIDE_CASTLING_WAY) &&
				!piecesAttackTheSquareOf(INITIAL_WHITE_KING | (INITIAL_WHITE_KING >>> 1) | (INITIAL_WHITE_KING >>> 2));
		else
			return blackCanCastleOnKingside &&
					!LongBits.intersects(occupancies[WHITE] | occupancies[BLACK], BLACK_KING_SIDE_CASLING_WAY) &&
					!piecesAttackTheSquareOf(INITIAL_BLACK_KING | (INITIAL_BLACK_KING >>> 1) | (INITIAL_BLACK_KING >>> 2));
	}
	public boolean canCastleOnQueenside(){
		if(theFirstPlayerHasTheMove())
			return whiteCanCastleOnQueenside &&
					!LongBits.intersects(occupancies[WHITE] | occupancies[BLACK], WHITE_QUEEN_SIDE_CASTLING_WAY) &&
					!piecesAttackTheSquareOf(INITIAL_WHITE_KING | (INITIAL_WHITE_KING << 1) | (INITIAL_WHITE_KING << 2));
		else
			return blackCanCastleOnQueenside &&
					!LongBits.intersects(occupancies[WHITE] | occupancies[BLACK], BLACK_QUEEN_SIDE_CASLING_WAY) &&
					!piecesAttackTheSquareOf(INITIAL_BLACK_KING | (INITIAL_BLACK_KING << 1) | (INITIAL_BLACK_KING << 2));
	}
	public int mobility(){
		int result = 0;
		for(int pieceType : PIECES){
			long pieces = army[WHITE][pieceType];
			for(long origin = Long.lowestOneBit(pieces); pieces != 0; pieces &= pieces - 1, origin = Long.lowestOneBit(pieces)){
				long moves = PositionUtility.moves(pieceType, color, origin, enPassantSquare, occupancies[color], occupancies[oppositeColor], false, false);
				result += Long.bitCount(moves);
			}
		}
		for(int pieceType : PIECES){
			long pieces = army[BLACK][pieceType];
			for(long origin = Long.lowestOneBit(pieces); pieces != 0; pieces &= pieces - 1, origin = Long.lowestOneBit(pieces)){
				long moves = PositionUtility.moves(pieceType, color, origin, enPassantSquare, occupancies[color], occupancies[oppositeColor], false, false);
				result -= Long.bitCount(moves);
			}
		}
		return result;
	}
	public long hash(){
		return hash;
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		appendPiecePlacementString(sb);
		sb.append(' ');
		sb.append(theFirstPlayerHasTheMove()? 'w' : 'b');
		sb.append(' ');
		appendCastlingAvailabilityString(sb);
		sb.append(' ');
		sb.append(enPassantSquare == 0? "-" : toSquareString(enPassantSquare));
		sb.append(' ');
		sb.append(String.valueOf(halfMoveClock));
		sb.append(' ');
		sb.append(fullMoveNumber());
		return sb.toString();
	}
	public FENManager createFENManager(){
		return new FENManager(toString());
	}
	


	int pieceOn(long square){
		for(int color : COLORS){
			for(int piece : PIECES){
				if(LongBits.intersects(army[color][piece], square))return piece;
			}
		}
		return EMPTY;
	}
	long occupancy(int color){
		return occupancies[color];
	}
	Undoable elimination(int piece, long square) {
		return new Undoable(){
			@Override
			public void apply() {
				army[oppositeColor][piece] -= square;
				occupancies[oppositeColor] -= square;
				hash ^= getRandomLong(oppositeColor, piece, square);
			}
			@Override
			public void undo() {
				army[oppositeColor][piece] |= square;
				occupancies[oppositeColor] |= square;
				hash ^= getRandomLong(oppositeColor, piece, square);
			}
			@Override
			public String toString() {
				return "";
			}
		};
	}
	Undoable transfer(int piece, long origin, long destination) {
		long relevantSquares = origin | destination;
		return new Undoable(){
			@Override
			public void apply() {
				army[color][piece] ^= relevantSquares;
				occupancies[color] ^= relevantSquares;
				hash ^= getRandomLong(color, piece, origin);
				hash ^= getRandomLong(color, piece, destination);
			}
			@Override
			public void undo() {
				army[color][piece] ^= relevantSquares;
				occupancies[color] ^= relevantSquares;
				hash ^= getRandomLong(color, piece, origin);
				hash ^= getRandomLong(color, piece, destination);
			}
			@Override
			public String toString() {
				return String.join("-", toSquareString(origin), toSquareString(destination));
			}
		};
	}
	Undoable promotion(int piece, long square) {
		return new Undoable(){
			@Override
			public void apply() {
				army[color][PAWN] -= square;
				army[color][piece] |= square;
				hash ^= getRandomLong(color, PAWN, square);
				hash ^= getRandomLong(color, piece, square);
			}
			@Override
			public void undo() {
				army[color][PAWN] |= square;
				army[color][piece] -= square;
				hash ^= getRandomLong(color, PAWN, square);
				hash ^= getRandomLong(color, piece, square);
			}
			@Override
			public String toString() {
				switch(piece){
				case KNIGHT:return "N";
				case BISHOP: return"B";
				case ROOK: return "R";
				case QUEEN: return "Q";
				default: throw new AssertionError();
				}
			}
		};
	}
	Undoable changeOfTheSide(){
		return new Undoable(){
			@Override
			public void apply() {
				changeTheSide();
				hash ^= getRandomLong();
			}
			@Override
			public void undo() {
				changeTheSide();
				hash ^= getRandomLong();
			}
			@Override
			public String toString() {
				return "";
			}
		};
	}
	Undoable historyUpdate(){
		return new Undoable(){
			@Override
			public void apply() {
				hashies.addLast(hash);
			}
			@Override
			public void undo() {
				hashies.removeLast();
			}
			@Override
			public String toString() {
				return "";
			}
		};
	}
	
	
	
	
	
	
	
	
	
	
	private static final class StatusChange implements Undoable{
		private final Position position;
		private final RawMove move;
		private final long prevEnPassant;
		private final boolean prevWhiteCanCastleOnKingside;
		private final boolean prevWhiteCanCastleOnQueenside;
		private final boolean prevBlackCanCastleOnKingside;
		private final boolean prevBlackCanCastleOnQueenside;
		private final int prevHalfMoveClock;
		private final long prevHash;

		
		
		private StatusChange(Position position, RawMove move){
			this.move = move;
			this.position = position;
			prevEnPassant = position.enPassantSquare;
			prevWhiteCanCastleOnKingside = position.whiteCanCastleOnKingside;
			prevWhiteCanCastleOnQueenside = position.whiteCanCastleOnQueenside;
			prevBlackCanCastleOnKingside = position.blackCanCastleOnKingside;
			prevBlackCanCastleOnQueenside = position.blackCanCastleOnQueenside;
			prevHalfMoveClock = position.halfMoveClock;
			prevHash = position.hash;
		}
		
		
		
		@Override
		public void apply() {
			position.plyCount++;
			if(move.piece == PAWN || move.captures()){
				position.halfMoveClock = 0;
			}else{
				position.halfMoveClock++;
			}
			long hashChanges = 0;
			if(move.isDoublePush()){
				position.enPassantSquare = move.enPassantSquare();
				hashChanges ^= getRandomLong(getFile(position.enPassantSquare));
			}else{
				position.enPassantSquare = 0;
				if(move.disablesWhiteKingSideCastling()){
					position.whiteCanCastleOnKingside = false;
					hashChanges ^= getRandomLong('K');
				}
				if(move.disablesWhiteQueenSideCastling()){
					position.whiteCanCastleOnQueenside = false;
					hashChanges ^= getRandomLong('Q');
				}
				if(move.disablesBlackKingSideCastling()){
					position.blackCanCastleOnKingside = false;
					hashChanges ^= getRandomLong('k');
				}
				if(move.disablesBlackQueenSideCastling()){
					position.blackCanCastleOnQueenside = false;
					hashChanges ^= getRandomLong('q');
				}
			}
			position.hash ^= hashChanges;
		}
		@Override
		public void undo() {
			position.plyCount--;
			position.halfMoveClock = prevHalfMoveClock;
			position.enPassantSquare = prevEnPassant;
			position.whiteCanCastleOnKingside = prevWhiteCanCastleOnKingside;
			position.whiteCanCastleOnQueenside = prevWhiteCanCastleOnQueenside;
			position.blackCanCastleOnKingside = prevBlackCanCastleOnKingside;
			position.blackCanCastleOnQueenside = prevBlackCanCastleOnQueenside;
			position.hash = prevHash;
		}
		@Override
		public String toString() {
			return "";
		}
		
		
		
		private final char getFile(long square){
			if(LongBits.intersects(square, A_FILE))return 'a';
			if(LongBits.intersects(square, B_FILE))return 'b';
			if(LongBits.intersects(square, C_FILE))return 'c';
			if(LongBits.intersects(square, D_FILE))return 'd';
			if(LongBits.intersects(square, E_FILE))return 'e';
			if(LongBits.intersects(square, F_FILE))return 'f';
			if(LongBits.intersects(square, G_FILE))return 'g';
			if(LongBits.intersects(square, H_FILE))return 'h';				
			throw new AssertionError();
		}
	}
	
	
	
	
	
	
	
	
	
	
	 static final class RawMove{
		private final Position position;
		private final int piece;
		private final long origin;
		private final long destination;
		private final long enemy;
		private final int promotee;
				

		
		private RawMove(Position position, int piece, long origin, long destination, long enemy, int promotee){
			this.position = position;
			this.piece = piece;
			this.origin = origin;
			this.destination = destination;
			this.enemy = enemy;
			this.promotee = promotee;
		}
				
		
		
		final boolean captures(){
			return LongBits.intersects(destination, enemy) || isEnPassant();
		}
		final boolean isEnPassant(){
			if(piece != Position.PAWN)return false;
			if(destination == position.enPassantSquare)return true;
			
			return false;
		}
		final boolean promotes(){
			return promotee != EMPTY;
		}
		final boolean castles(){
			if(piece != KING)return false;
			return !LongBits.intersects(destination, PositionUtility.kingTerritory(origin));
		}
		final boolean castlesOnKingSide(){
			return destination == (origin >>> 2) && castles();
		}
		final boolean castlesOnQueenSide(){
			return destination == (origin << 2) && castles();
		}
		final boolean checks(){
			long attacks = PositionUtility.moves(piece, position.color, destination, position.enPassantSquare, position.occupancies[position.color], position.occupancies[position.oppositeColor], position.canCastleOnKingside(), position.canCastleOnQueenside());
			for(long attack = Long.lowestOneBit(attacks); attacks != 0; attacks &= attacks - 1, attack = Long.lowestOneBit(attacks)){
				if(attack == position.army[position.oppositeColor][KING])return true;
			}
			return false;
		}
		final long enPassantSquare(){
			long result = ((origin << 8) | (origin >>> 8)) & ((destination << 8) | (destination >> 8));
			return result;
		}
		final boolean isDoublePush(){
			if(piece != Position.PAWN)return false;
			return !LongBits.intersects(destination, PositionUtility.kingTerritory(origin));
		}
		final boolean disablesWhiteKingSideCastling(){
			if(!position.whiteCanCastleOnKingside)return false;
			if(position.color == WHITE){
				if(piece == KING)return true;
				return origin == INITIAL_WHITE_KING_SIDE_ROOK;
			}else return destination == INITIAL_WHITE_KING_SIDE_ROOK;
		}
		final boolean disablesWhiteQueenSideCastling(){
			if(!position.whiteCanCastleOnQueenside)return false;
			if(position.color == WHITE){
				if(piece == KING)return true;
				return origin == INITIAL_WHITE_QUEEN_SIDE_ROOK;
			}else return destination == INITIAL_WHITE_QUEEN_SIDE_ROOK;
		}
		final boolean disablesBlackKingSideCastling(){
			if(!position.blackCanCastleOnKingside)return false;
			if(position.color == WHITE)return destination == INITIAL_BLACK_KING_SIDE_ROOK;
			else{
				if(piece == KING)return true;
				return origin == INITIAL_BLACK_KING_SIDE_ROOK;
			}
		}
		final boolean disablesBlackQueenSideCastling(){
			if(!position.blackCanCastleOnQueenside)return false;
			if(position.color == WHITE)return destination == INITIAL_BLACK_QUEEN_SIDE_ROOK;
			else{
				if(piece == KING)return true;
				return origin == INITIAL_BLACK_QUEEN_SIDE_ROOK;
			}			
		}
		final int materialOfThePiece(){
			switch(piece){
			case PAWN:return 1;
			case KNIGHT:return 3;
			case BISHOP:return 3;
			case ROOK:return 5;
			case QUEEN:return 9;
			case KING:return 100;
			default:throw new AssertionError();
			}
		}
		final int materialGain(){
			if(!captures())return 0;
			if(isEnPassant())return 1;
			
			int result = 0;
			switch(position.pieceOn(destination)){
			case PAWN:result += 1;		break;
			case KNIGHT:result += 3;	break;
			case BISHOP:result += 3;	break;
			case ROOK:result += 5;		break;
			case QUEEN:result += 9;		break;
			default:throw new AssertionError();
			}
			if(promotes()){
				switch(promotee){
				case KNIGHT:result += 2;	break;
				case BISHOP:result += 2;	break;
				case ROOK:result += 4;		break;
				case QUEEN:result += 8;		break;
				default:throw new AssertionError();
				}
			}
			return result;
		}
		final Move createMove(){
			switch(piece){
			case PAWN:{
				if(isEnPassant()){
					long pawnPassing = ((position.enPassantSquare << 8) | (position.enPassantSquare >>> 8)) & enemy;
					
					return new Move(new StatusChange(position, this)
							.andThen(position.elimination(PAWN, pawnPassing))
							.andThen(position.transfer(PAWN, origin, destination))
							.andThen(position.changeOfTheSide())
							.andThen(position.historyUpdate()));
				}
				return new Move(new StatusChange(position, this)
						.ifTrueThen(captures(), position.elimination(position.pieceOn(destination), destination))
						.andThen(position.transfer(PAWN, origin, destination))
						.ifTrueThen(promotes(), position.promotion(promotee, destination))
						.andThen(position.changeOfTheSide())
						.andThen(position.historyUpdate()));
			}
			case KING:{
				if(castlesOnKingSide()){
					long rookOrigin = destination >>> 1;
					long rookDestination = destination << 1;
					
					return new Move(new StatusChange(position, this)
							.andThen(position.transfer(KING, origin, destination))
							.andThen(position.transfer(ROOK, rookOrigin, rookDestination))
							.andThen(position.changeOfTheSide())
							.andThen(position.historyUpdate()));
				}else if(castlesOnQueenSide()){
					long rookOrigin = destination << 2;
					long rookDestination = destination >>> 1;
					
					return new Move(new StatusChange(position, this)
							.andThen(position.transfer(KING, origin, destination))
							.andThen(position.transfer(ROOK, rookOrigin, rookDestination))
							.andThen(position.changeOfTheSide())
							.andThen(position.historyUpdate()));
				}
				
				return new Move(new StatusChange(position, this)
						.ifTrueThen(captures(), position.elimination(position.pieceOn(destination), destination))
						.andThen(position.transfer(KING, origin, destination))
						.andThen(position.changeOfTheSide())
						.andThen(position.historyUpdate()));
			}
			case KNIGHT:
			case BISHOP:
			case ROOK:
			case QUEEN:
				
				return new Move(new StatusChange(position, this)
						.ifTrueThen(captures(), position.elimination(position.pieceOn(destination), destination))
						.andThen(position.transfer(piece, origin, destination))
						.andThen(position.changeOfTheSide())
						.andThen(position.historyUpdate()));
			}
			throw new AssertionError();
		}
	}
	
	
	
	
	
	
	
	
	
	
	private void changeTheSide(){
		int tmp = color;
		color = oppositeColor;
		oppositeColor = tmp;
	}
	private void initializeHash(){
		for(long square : LongBits.of(Long.MAX_VALUE)){
			int piece = pieceOn(square);
			if(piece != EMPTY){
				int color = LongBits.intersects(occupancies[WHITE], square)? WHITE : BLACK; 
				hash ^= getRandomLong(color,piece, square);
			}
		}
	}
	private RawMove[] generateRawMoves(){
		RawMove[] buf = new RawMove[218];
		
		int index = 0;
		for(int piece : PIECES){
			long pieces = army[color][piece];
			for(long origin = Long.lowestOneBit(pieces); origin != 0; pieces &= pieces - 1, origin = Long.lowestOneBit(pieces)){
				long moves = PositionUtility.moves(piece, color, origin, enPassantSquare, occupancies[color], occupancies[oppositeColor], canCastleOnKingside(), canCastleOnQueenside());
				for(long destination = Long.lowestOneBit(moves); moves != 0; moves &= moves - 1, destination = Long.lowestOneBit(moves)){
					for(int promotee : PROMOTEES){
						if(piece != PAWN | !LongBits.intersects(destination, BACK_RANKS)){
							buf[index++] = new RawMove(this, piece, origin, destination, occupancies[oppositeColor], EMPTY);
							break;
						}else{
							buf[index++]= new RawMove(this, PAWN, origin, destination, occupancies[oppositeColor], promotee);
						}
					}
				}
			}
		}
		RawMove[] result = new RawMove[index];
		System.arraycopy(buf, 0, result, 0, index);
		return result;
	}
	private boolean piecesAttackTheSquareOf(long square){
		if(knightsAttackTheSquareOf(square))return true;
		if(bishopsAttackTheSquareOf(square))return true;
		if(rooksAttackTheSquareOf(square))return true;
		if(pawnsAttackTheSquareOf(square))return true;
		if(queensAttackTheSquareOf(square))return true;
		if(LongBits.intersects(PositionUtility.kingTerritory(army[oppositeColor][KING]), square))return true;

		return false;
	}
	private boolean pawnsAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][PAWN];
		for(long pawn = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, pawn = Long.lowestOneBit(squares)){
			if(LongBits.intersects(PositionUtility.pawnTerritory(oppositeColor, pawn), square))return true;
		}
		return false;
	}
	private boolean knightsAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][KNIGHT];
		for(long knight = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, knight = Long.lowestOneBit(squares)){
			if(LongBits.intersects(PositionUtility.knightTerritory(knight), square))return true;
		}
		return false;
	}
	private boolean bishopsAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][BISHOP];
		for(long bishop = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, bishop = Long.lowestOneBit(squares)){
			if(LongBits.intersects(PositionUtility.bishopMoves(bishop, occupancies[oppositeColor], occupancies[color]), square))return true;
		}
		return false;
	}
	private boolean rooksAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][ROOK];
		for(long rook = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, rook = Long.lowestOneBit(squares)){
			if(LongBits.intersects(PositionUtility.rookMoves(rook, occupancies[oppositeColor], occupancies[color]), square))return true;
		}
		return false;
	}
	private boolean queensAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][QUEEN];
		for(long queen = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, queen = Long.lowestOneBit(squares)){
			if(LongBits.intersects(PositionUtility.queenMoves(queen, occupancies[oppositeColor], occupancies[color]), square))return true;
		}
		return false;
	}
	private void appendPiecePlacementString(StringBuilder sb) {
		long occupancy = occupancies[WHITE] | occupancies[BLACK];
		for(int row = 7; row > -1; row--){
			long rank = occupancy & (0xFFL << row * 8);
			int prevIndex = 8;
			for(Iterator<Long> iterator = LongBits.of(rank).highestToLowest(); iterator.hasNext();){
				long bit = iterator.next();
				int index = Long.numberOfTrailingZeros(bit) - row * 8;
				int numberOfSuccessiveEmptySquares = prevIndex - index - 1;
				if(numberOfSuccessiveEmptySquares != 0){sb.append(numberOfSuccessiveEmptySquares);}
				sb.append(getSymbol(LongBits.intersects(occupancies[WHITE], bit)? WHITE : BLACK, pieceOn(bit)));
				prevIndex = index;
			}
			if(prevIndex != 0){sb.append(prevIndex);}
			if(row != 0){sb.append('/');}
		}
	}
	private void appendCastlingAvailabilityString(StringBuilder sb) {
		if(!whiteCanCastleOnKingside && !whiteCanCastleOnQueenside && !blackCanCastleOnKingside && !blackCanCastleOnQueenside){
			sb.append('-');
		}else{
			if(whiteCanCastleOnKingside){
				sb.append('K');
			}if(whiteCanCastleOnQueenside){
				sb.append('Q');
			}if(blackCanCastleOnKingside){
				sb.append('k');
			}if(blackCanCastleOnQueenside){
				sb.append('q');
			}
		}
	}

	
	
	static final long getRandomLong(int color, int piece, long square){
		int index = Long.numberOfTrailingZeros(square);
		
		return RANDOMS[(color + 1) * (piece + 1) * index];
	}
	static final long getRandomLong(){
		return RANDOMS[768];// = 12 * 64
	}
	static final long getRandomLong(char letter){
		switch(letter){
		case 'K':return RANDOMS[769];
		case 'Q':return RANDOMS[770];
		case 'k':return RANDOMS[771];
		case 'q':return RANDOMS[772];
		case 'a':return RANDOMS[773];
		case 'b':return RANDOMS[774];
		case 'c':return RANDOMS[775];
		case 'd':return RANDOMS[776];
		case 'e':return RANDOMS[777];
		case 'f':return RANDOMS[778];
		case 'g':return RANDOMS[779];
		case 'h':return RANDOMS[780];
		default:throw new AssertionError();
		}
	}
	static final long[] randoms(long seed){
		Random rnd = new Random(seed);
		long[] result = new long[12 * 64 + 1 + 4 + 8];
		for(int i : Range.of(result.length)){
			result[i] = rnd.nextLong();
		}
		return result;
	}
	static final String toSquareString(long square){
		StringBuilder sb = new StringBuilder();
		for(long bit : LongBits.of(square)){
			int index = Long.bitCount(bit - 1);
			int column = index & 0x7;
			int row = index >>> 3;
			sb.append((char)('h' - column));
			sb.append((char)('1' + row));
		}
		return sb.toString();
	}
	
	
	
	private static final char getSymbol(int color, int piece){
		char result = 0;
		switch(piece){
		case PAWN:result = 'P'; break;
		case KNIGHT:result = 'N'; break;
		case BISHOP:result = 'B'; break;
		case ROOK:result = 'R'; break;
		case QUEEN:result = 'Q'; break;
		case KING:result = 'K'; break;
		default:throw new AssertionError();
		}
		return color == BLACK? Character.toLowerCase(result) : result;
	}
}
