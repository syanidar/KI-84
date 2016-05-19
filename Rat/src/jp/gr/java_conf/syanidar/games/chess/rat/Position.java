package jp.gr.java_conf.syanidar.games.chess.rat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import jp.gr.java_conf.syanidar.util.bitwise.Bits;
import jp.gr.java_conf.syanidar.util.collection.Range;
import jp.gr.java_conf.syanidar.util.encryption.BitSequence;
import jp.gr.java_conf.syanidar.util.function.Undoable;

public final class Position implements jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position<Move>{			
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
		else return Optional.of(new Chessman(Bits.intersects(square, occupancies[WHITE])? Color.WHITE : Color.BLACK, Piece.values()[piece]));
	}
	public Optional<Chessman> pieceOn(Square square){
		return pieceOn(square.file(), square.rank());
	}
	@Override
	public List<Move> moves() {
		RawMove[] longMoves = generateRawMoves();
		Comparator<RawMove> c = DefaultMoveOrderer.getInstance();
		Arrays.sort(longMoves, c);
		List<Move> result = new ArrayList<>(longMoves.length);
		for(RawMove m : longMoves){
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
	public boolean theFirstPlayerHasTheMove() {
		return color == WHITE;
	}
	public boolean isTerminated(){
		return moves().size() == 0;
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
	public boolean canCastleOnKingside(){
		if(theFirstPlayerHasTheMove())
		return whiteCanCastleOnKingside &&
				!Bits.intersects(occupancies[WHITE] | occupancies[BLACK], WHITE_KING_SIDE_CASTLING_WAY) &&
				!piecesAttackTheSquareOf(INITIAL_WHITE_KING | (INITIAL_WHITE_KING >>> 1));
		else
			return blackCanCastleOnKingside &&
					!Bits.intersects(occupancies[WHITE] | occupancies[BLACK], BLACK_KING_SIDE_CASLING_WAY) &&
					!piecesAttackTheSquareOf(INITIAL_BLACK_KING | (INITIAL_BLACK_KING >>> 1));
	}
	public boolean canCastleOnQueenside(){
		if(theFirstPlayerHasTheMove())
			return whiteCanCastleOnQueenside &&
					!Bits.intersects(occupancies[WHITE] | occupancies[BLACK], WHITE_QUEEN_SIDE_CASTLING_WAY) &&
					!piecesAttackTheSquareOf(INITIAL_WHITE_KING | (INITIAL_WHITE_KING << 1));
		else
			return blackCanCastleOnQueenside &&
					!Bits.intersects(occupancies[WHITE] | occupancies[BLACK], BLACK_QUEEN_SIDE_CASLING_WAY) &&
					!piecesAttackTheSquareOf(INITIAL_BLACK_KING | (INITIAL_BLACK_KING << 1));
	}
	public long hash(){
		return hash;
	}
	public FENManager createFENManager(){
		StringBuilder sb = new StringBuilder();
		long occupancy = occupancies[WHITE] | occupancies[BLACK];
		for(int row = 7; row > -1; row--){
			byte rank = (byte)((occupancy >>> row * 8) & 0xFF);
			parseRank(sb, row, rank);
			if(row != 0){
				sb.append('/');
			}
		}
		sb.append(' ');
		sb.append(theFirstPlayerHasTheMove()? 'w' : 'b');
		sb.append(' ');
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
		sb.append(' ');
		sb.append(enPassantSquare == 0? "-" : toSquareString(enPassantSquare));
		sb.append(' ');
		sb.append(String.valueOf(halfMoveClock));
		sb.append(' ');
		sb.append(fullMoveNumber());
		return new FENManager(sb.toString());
	}



	int pieceOn(long square){
		for(int color : COLORS){
			for(int piece : PIECES){
				if(Bits.intersects(army[color][piece], square))return piece;
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
	
	
	
	
	
	
	
	
	
	
	private final class StatusChange implements Undoable{
		private final RawMove move;
		private final long prevEnPassant;
		private final boolean prevWhiteCanCastleOnKingside;
		private final boolean prevWhiteCanCastleOnQueenside;
		private final boolean prevBlackCanCastleOnKingside;
		private final boolean prevBlackCanCastleOnQueenside;
		private final int prevHalfMoveClock;
		private final long prevHash;

		
		
		private StatusChange(RawMove move){
			this.move = move;
			prevEnPassant = enPassantSquare;
			prevWhiteCanCastleOnKingside = whiteCanCastleOnKingside;
			prevWhiteCanCastleOnQueenside = whiteCanCastleOnQueenside;
			prevBlackCanCastleOnKingside = blackCanCastleOnKingside;
			prevBlackCanCastleOnQueenside = blackCanCastleOnQueenside;
			prevHalfMoveClock = halfMoveClock;
			prevHash = hash;
		}
		
		
		
		@Override
		public void apply() {
			plyCount++;
			if(move.piece == PAWN || move.captures()){
				halfMoveClock = 0;
			}else{
				halfMoveClock++;
			}
			long hashChanges = 0;
			if(move.isInitialPawnPush()){
				enPassantSquare = move.enPassantSquare();
				hashChanges ^= getRandomLong(getFile(enPassantSquare));
			}else{
				enPassantSquare = 0;
				if(move.disablesWhiteKingSideCastling()){
					whiteCanCastleOnKingside = false;
					hashChanges ^= getRandomLong('K');
				}
				if(move.disablesWhiteQueenSideCastling()){
					whiteCanCastleOnQueenside = false;
					hashChanges ^= getRandomLong('Q');
				}
				if(move.disablesBlackKingSideCastling()){
					blackCanCastleOnKingside = false;
					hashChanges ^= getRandomLong('k');
				}
				if(move.disablesBlackQueenSideCastling()){
					blackCanCastleOnQueenside = false;
					hashChanges ^= getRandomLong('q');
				}
			}
			hash ^= hashChanges;
		}
		@Override
		public void undo() {
			plyCount--;
			halfMoveClock = prevHalfMoveClock;
			enPassantSquare = prevEnPassant;
			whiteCanCastleOnKingside = prevWhiteCanCastleOnKingside;
			whiteCanCastleOnQueenside = prevWhiteCanCastleOnQueenside;
			blackCanCastleOnKingside = prevBlackCanCastleOnKingside;
			blackCanCastleOnQueenside = prevBlackCanCastleOnQueenside;
			hash = prevHash;
		}
		@Override
		public String toString() {
			return "";
		}
		
		
		
		private final char getFile(long square){
			if(Bits.intersects(square, A_FILE))return 'a';
			if(Bits.intersects(square, B_FILE))return 'b';
			if(Bits.intersects(square, C_FILE))return 'c';
			if(Bits.intersects(square, D_FILE))return 'd';
			if(Bits.intersects(square, E_FILE))return 'e';
			if(Bits.intersects(square, F_FILE))return 'f';
			if(Bits.intersects(square, G_FILE))return 'g';
			if(Bits.intersects(square, H_FILE))return 'h';				
			throw new AssertionError();
		}
	}
	
	
	
	
	
	
	
	
	
	
	 final class RawMove{
		private final int piece;
		private final long origin;
		private final long destination;
		private final long enemy;
		private final int promotee;
				

		
		RawMove(int piece, long origin, long destination, long enemy, int promotee){
			this.piece = piece;
			this.origin = origin;
			this.destination = destination;
			this.enemy = enemy;
			this.promotee = promotee;
		}
				
		
		
		final boolean captures(){
			return Bits.intersects(destination, enemy) || isEnPassant();
		}
		final boolean isEnPassant(){
			if(piece != Position.PAWN)return false;
			if(destination == enPassantSquare)return true;
			
			return false;
		}
		final boolean promotes(){
			return promotee != EMPTY;
		}
		final boolean castles(){
			if(piece != KING)return false;
			return !Bits.intersects(destination, PositionUtility.kingTerritory(origin));
		}
		final boolean castlesOnKingSide(){
			return destination == (origin >>> 2) && castles();
		}
		final boolean castlesOnQueenSide(){
			return destination == (origin << 2) && castles();
		}
		final long enPassantSquare(){
			long result = ((origin << 8) | (origin >>> 8)) & ((destination << 8) | (destination >> 8));
			return result;
		}
		final boolean isInitialPawnPush(){
			if(piece != Position.PAWN)return false;
			return !Bits.intersects(destination, PositionUtility.kingTerritory(origin));
		}
		final boolean disablesWhiteKingSideCastling(){
			if(!whiteCanCastleOnKingside)return false;
			if(color == WHITE){
				if(piece == KING)return true;
				return origin == INITIAL_WHITE_KING_SIDE_ROOK;
			}else return destination == INITIAL_WHITE_KING_SIDE_ROOK;
		}
		final boolean disablesWhiteQueenSideCastling(){
			if(!whiteCanCastleOnQueenside)return false;
			if(color == WHITE){
				if(piece == KING)return true;
				return origin == INITIAL_WHITE_QUEEN_SIDE_ROOK;
			}else return destination == INITIAL_WHITE_QUEEN_SIDE_ROOK;
		}
		final boolean disablesBlackKingSideCastling(){
			if(!blackCanCastleOnKingside)return false;
			if(color == WHITE)return destination == INITIAL_BLACK_KING_SIDE_ROOK;
			else{
				if(piece == KING)return true;
				return origin == INITIAL_BLACK_KING_SIDE_ROOK;
			}
		}
		final boolean disablesBlackQueenSideCastling(){
			if(!blackCanCastleOnQueenside)return false;
			if(color == WHITE)return destination == INITIAL_BLACK_QUEEN_SIDE_ROOK;
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
			
			switch(pieceOn(destination)){
			case PAWN:return 1;
			case KNIGHT:return 3;
			case BISHOP:return 3;
			case ROOK:return 5;
			case QUEEN:return 9;
			case KING:return 100;
			default:throw new AssertionError();
			}
		}
		final Move createMove(){
			switch(piece){
			case PAWN:{
				if(isEnPassant()){
					long pawnPassing = ((enPassantSquare << 8) | (enPassantSquare >>> 8)) & enemy;
					
					return new Move(new StatusChange(this)
							.andThen(elimination(PAWN, pawnPassing))
							.andThen(transfer(PAWN, origin, destination))
							.andThen(changeOfTheSide())
							.andThen(historyUpdate()));
				}
				return new Move(new StatusChange(this)
						.ifTrueThen(captures(), elimination(pieceOn(destination), destination))
						.andThen(transfer(PAWN, origin, destination))
						.ifTrueThen(promotes(), promotion(promotee, destination))
						.andThen(changeOfTheSide())
						.andThen(historyUpdate()));
			}
			case KING:{
				if(castlesOnKingSide()){
					long rookOrigin = destination >>> 1;
					long rookDestination = destination << 1;
					
					return new Move(new StatusChange(this)
							.andThen(transfer(KING, origin, destination))
							.andThen(transfer(ROOK, rookOrigin, rookDestination))
							.andThen(changeOfTheSide())
							.andThen(historyUpdate()));
				}else if(castlesOnQueenSide()){
					long rookOrigin = destination << 2;
					long rookDestination = destination >>> 1;
					
					return new Move(new StatusChange(this)
							.andThen(transfer(KING, origin, destination))
							.andThen(transfer(ROOK, rookOrigin, rookDestination))
							.andThen(changeOfTheSide())
							.andThen(historyUpdate()));
				}
				
				return new Move(new StatusChange(this)
						.ifTrueThen(captures(), elimination(pieceOn(destination), destination))
						.andThen(transfer(KING, origin, destination))
						.andThen(changeOfTheSide())
						.andThen(historyUpdate()));
			}
			case KNIGHT:
			case BISHOP:
			case ROOK:
			case QUEEN:
				
				return new Move(new StatusChange(this)
						.ifTrueThen(captures(), elimination(pieceOn(destination), destination))
						.andThen(transfer(piece, origin, destination))
						.andThen(changeOfTheSide())
						.andThen(historyUpdate()));
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
		for(long square : Bits.of(Long.MAX_VALUE)){
			int piece = pieceOn(square);
			if(piece != EMPTY){
				int color = Bits.intersects(occupancies[WHITE], square)? WHITE : BLACK; 
				hash ^= getRandomLong(color,piece, square);
			}
		}
	}
	private RawMove[] generateRawMoves(){
		RawMove[] buf = new RawMove[200];
		
		int index = 0;
		for(int piece : PIECES){
			for(long origin : Bits.of(army[color][piece])){
				long moves = PositionUtility.moves(piece, color, origin, enPassantSquare, occupancies[color], occupancies[oppositeColor], canCastleOnKingside(), canCastleOnQueenside());
				for(long destination : Bits.of(moves)){
					for(int promotee : PROMOTEES){
						if(piece != PAWN | !Bits.intersects(destination, BACK_RANKS)){
							buf[index++] = new RawMove(piece, origin, destination, occupancies[oppositeColor], EMPTY);
							break;
						}else{
							buf[index++]= new RawMove(PAWN, origin, destination, occupancies[oppositeColor], promotee);
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
		if(queensAttackTheSquareOf(square))return true;
		if(rooksAttackTheSquareOf(square))return true;
		if(knightsAttackTheSquareOf(square))return true;
		if(bishopsAttackTheSquareOf(square))return true;
		if(pawnsAttackTheSquareOf(square))return true;
		if(Bits.intersects(PositionUtility.kingTerritory(army[oppositeColor][KING]), square))return true;

		return false;
	}
	private boolean pawnsAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][PAWN];
		for(long pawn = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, pawn = Long.lowestOneBit(squares)){
			if(Bits.intersects(PositionUtility.pawnTerritory(oppositeColor, pawn), square))return true;
		}
		return false;
	}
	private boolean knightsAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][KNIGHT];
		for(long knight = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, knight = Long.lowestOneBit(squares)){
			if(Bits.intersects(PositionUtility.knightTerritory(knight), square))return true;
		}
		return false;
	}
	private boolean bishopsAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][BISHOP];
		for(long bishop = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, bishop = Long.lowestOneBit(squares)){
			if(Bits.intersects(PositionUtility.bishopMoves(bishop, occupancies[oppositeColor], occupancies[color]), square))return true;
		}
		return false;
	}
	private boolean rooksAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][ROOK];
		for(long rook = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, rook = Long.lowestOneBit(squares)){
			if(Bits.intersects(PositionUtility.rookMoves(rook, occupancies[oppositeColor], occupancies[color]), square))return true;
		}
		return false;
	}
	private boolean queensAttackTheSquareOf(long square) {
		long squares = army[oppositeColor][QUEEN];
		for(long queen = Long.lowestOneBit(squares); squares != 0; squares &= squares - 1, queen = Long.lowestOneBit(squares)){
			if(Bits.intersects(PositionUtility.queenMoves(queen, occupancies[oppositeColor], occupancies[color]), square))return true;
		}
		return false;
	}
	private final void parseRank(StringBuilder sb, int row, byte rank){
		BitSequence bs = BitSequence.of(rank);
		int[] numbers = bs.successiveBitCounts();
		int sum = 0;
		for(int i : Range.of(numbers.length)){
			if(i % 2 == (bs.firstBitIsSet() ? 0 : 1)){
				for(int j : Range.of(numbers[i])){
					int column = sum + j;
					int index = row * 8 + 7 - column;
					if(Bits.intersects(occupancies[WHITE], 1L << index)){
						switch(pieceOn(1L << index)){
						case PAWN:sb.append('P'); break;
						case KNIGHT:sb.append('N'); break;
						case BISHOP:sb.append('B'); break;
						case ROOK:sb.append('R'); break;
						case QUEEN:sb.append('Q'); break;
						case KING:sb.append('K'); break;
						default: throw new AssertionError();
						}
					}else if(Bits.intersects(occupancies[BLACK], 1L << index)){
						switch(pieceOn(1L << index)){
						case PAWN:sb.append('p'); break;
						case KNIGHT:sb.append('n'); break;
						case BISHOP:sb.append('b'); break;
						case ROOK:sb.append('r'); break;
						case QUEEN:sb.append('q'); break;
						case KING:sb.append('k'); break;
						default: throw new AssertionError();
						}
					}
				}
			}else{
				sb.append(numbers[i]);
			}
			sum += numbers[i];
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
		for(long bit : Bits.of(square)){
			int index = Long.bitCount(bit - 1);
			int column = index & 0x7;
			int row = index >>> 3;
			sb.append((char)('h' - column));
			sb.append((char)('1' + row));
		}
		return sb.toString();
	}
}
