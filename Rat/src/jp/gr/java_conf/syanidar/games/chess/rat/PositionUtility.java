package jp.gr.java_conf.syanidar.games.chess.rat;

import java.util.Optional;

import jp.gr.java_conf.syanidar.util.bitwise.Bits;
import jp.gr.java_conf.syanidar.util.collection.Range;

final class PositionUtility{
	private PositionUtility(){}
	
	private static final long[] FILES = files();// a -> h
	private static final long[] RANKS = ranks();// 1st -> 8th
	private static final long[] ASCENDING_DIAGONALS = aDiagonals();// a7-b8 -> g1-h2
	private static final long[] DESCENDING_DIAGONALS = dDiagonals();//a2-b1 -> g8-h7
	
	private static final long[][] PAWN_TERRITORIES = pawnTerritories();
	private static final long[] KNIGHT_TERRITORIES = knightTerritories();
	private static final long[] BISHOP_TERRITORIES = bishopTerritories();
	private static final long[] ROOK_TERRITORIES = rookTerritories();
	private static final long[] QUEEN_TERRITORIES = queenTerritories();
	private static final long[] KING_TERRITORIES = kingTerritories();
	
	private static final long[] BISHOP_MAGIC_FACTORS = new long[]{
			4614016985292886530L, 
			4508137327641632L, 
			4507998752350272L, 
			289405831391215616L, 
			-8639025449717989372L, 
			243352727285924000L, 
			288304061149610752L, 
			1477321999458001920L, 
			1161687990403280L, 
			243300038254609088L, 
			3395430423527970L, 
			1163059018995859456L, 
			19163393529348098L, 
			5476942304734642179L, 
			9289774968094722L, 
			71472702816784L, 
			-9205161890881732096L, 
			598987685113119296L, 
			2603661195488542728L, 
			1299289059453045760L, 
			289079199665750016L, 
			288337029855453185L, 
			18295959721214464L, 
			432698612787970448L, 
			-9162569042763959808L, 
			2306269637446730240L, 
			293173782712189218L, 
			-8628892485838434272L, 
			145136952549376L, 
			1227238047432573056L, 
			749849481873264896L, 
			5233253685509359616L, 
			2594394795017248776L, 
			1316182494026171392L, 
			41095415353327680L, 
			4672926566056320L, 
			293860975468872000L, 
			4787282223073280L, 
			2891594643529540865L, 
			1304029380494596L, 
			2306142110803337748L, 
			1420571707966020L, 
			-8935088608998553600L, 
			600745799860482L, 
			72084124118286400L, 
			4505867386882120L, 
			1195706268472590915L, 
			4613401363959779584L, 
			40675337991881728L, 
			221838017052033028L, 
			2237682680320L, 
			4692756352261554688L, 
			79235737848138L, 
			72094036869120064L, 
			306262921183297553L, 
			4630281582165426432L, 
			2393094843532288L, 
			301759319257460753L, 
			-7782113363546306552L, 
			35184523348225L, 
			141115715094016L, 
			2469173357522977024L, 
			1153134328859983936L, 
			1134730644857344L, 
	};
	private static final long[] ROOK_MAGIC_FACTORS = {
			36028935531696136L, 
			18015051881385984L, 
			5800640924260769792L, 
			144117524807041028L, 
			144132797578100994L, 
			1297037861048058372L, 
			36030996113817856L, 
			252204053579174144L, 
			4611826773919809538L, 
			45247111633072128L, 
			148759593962971136L, 
			108227162906034182L, 
			1154891898297516564L, 
			36873230606198785L, 
			4644339279987200L, 
			144255935246795008L, 
			21499300749000705L, 
			846624223969924L, 
			540504523188240400L, 
			2253449224065024L, 
			2889061360251183113L, 
			282574893745152L, 
			9895705329796L, 
			585609238804447488L, 
			1603914813958791184L, 
			5699870427972624L, 
			-9205340044009668600L, 
			2287302014403648L, 
			333275864303939744L, 
			5188155571122208770L, 
			2361020909332202145L, 
			141014513754368L, 
			90142361836849040L, 
			1143766979190785L, 
			35197416378368L, 
			94593184469878784L, 
			4611694816718226448L, 
			567352328466440L, 
			18691706062336L, 
			425236809908354L, 
			36029441801076736L, 
			-4607182349543653376L, 
			144133061584486408L, 
			722845332781694980L, 
			4538852853284869L, 
			308017875714050L, 
			2260666924793860L, 
			4612557522061033474L, 
			36033884411957504L, 
			145404509345022208L, 
			148973380758018560L, 
			76561228026347584L, 
			-9222243628667371392L, 
			6597086806144L, 
			4683749488316154368L, 
			-9223335752966561664L, 
			288407397800886282L, 
			79714865643553L, 
			4919232617273624769L, 
			289365090407288834L, 
			-9079146278738198015L, 
			9007852360564994L, 
			4565176577950082L, 
			7061644766043193361L, 
	};
	private static final long[][] BISHOP_MOVES = bishopMoves();
	private static final long[][] ROOK_MOVES = rookMoves();
	
	static final long pawnTerritory(int color, long square){
		return PAWN_TERRITORIES[color][Long.numberOfTrailingZeros(square)];
	}
	static final long knightTerritory(long square){
		return KNIGHT_TERRITORIES[Long.numberOfTrailingZeros(square)];
	}
	static final long bishopTerritory(long square){
		return BISHOP_TERRITORIES[Long.numberOfTrailingZeros(square)];
	}
	static final long rookTerritory(long square){
		return ROOK_TERRITORIES[Long.numberOfTrailingZeros(square)];
	}
	static final long queenTerritory(long square){
		return QUEEN_TERRITORIES[Long.numberOfTrailingZeros(square)];
	}
	static final long kingTerritory(long square){
		return KING_TERRITORIES[Long.numberOfTrailingZeros(square)];
	}
	static final long pawnMoves(int color, long square, long enPassantSquare, long friendly, long enemy){
		int squareIndex = Long.numberOfTrailingZeros(square);
		long moves = PAWN_TERRITORIES[color][squareIndex];
		moves &= enemy | enPassantSquare;	
		long occupancy = friendly | enemy;
		long front = 0;
		long initialPawnPush = 0;
		if(color == Position.WHITE){
			front = (square << 8) & ~occupancy;
			initialPawnPush = Bits.intersects(square, Position.INITIAL_WHITE_PAWNS) ? (front << 8) & ~occupancy: 0;
		}else{
			front = (square >>> 8) & ~occupancy;
			initialPawnPush = Bits.intersects(square, Position.INITIAL_BLACK_PAWNS) ? (front >>> 8) & ~occupancy: 0;
		}
		moves |= front;
		moves |= initialPawnPush;
		return moves;
	}
	static final long knightMoves(long square, long friendly){
		int squareIndex = Long.numberOfTrailingZeros(square);
		long moves = KNIGHT_TERRITORIES[squareIndex];
		return moves & ~friendly;
	}
	static final long bishopMoves(long square, long friendly, long enemy){
		int squareIndex = Long.numberOfTrailingZeros(square);
		long relevantOccupancy = friendly | enemy;
		long mask = bishopRelevantOccupancy(BISHOP_TERRITORIES[squareIndex]);
		relevantOccupancy &= mask;
		long moves = BISHOP_MOVES[squareIndex][(int)(relevantOccupancy * BISHOP_MAGIC_FACTORS[squareIndex] >>> (64 - Long.bitCount(mask)))];
		moves &= ~friendly;
		return moves;
	}
	static final long rookMoves(long square, long friendly, long enemy){
		int squareIndex = Long.numberOfTrailingZeros(square);
		long relevantOccupancy = friendly | enemy;
		long mask = rookRelevantOccupancy(square, squareIndex, ROOK_TERRITORIES[squareIndex]);
		relevantOccupancy &= mask;
		long moves = ROOK_MOVES[squareIndex][(int)(relevantOccupancy * ROOK_MAGIC_FACTORS[squareIndex] >>> (64 - Long.bitCount(mask)))];
		moves &= ~friendly;
		return moves;
	}
	static final long queenMoves(long square, long friendly, long enemy){
		int squareIndex = Long.numberOfTrailingZeros(square);
		long relevantOccupancy = friendly | enemy;
		long mask = bishopRelevantOccupancy(BISHOP_TERRITORIES[squareIndex]);
		relevantOccupancy &= mask;
		long moves = BISHOP_MOVES[squareIndex][(int)(relevantOccupancy * BISHOP_MAGIC_FACTORS[squareIndex] >>> (64 - Long.bitCount(mask)))];

		relevantOccupancy = friendly | enemy;
		mask = rookRelevantOccupancy(square, squareIndex, ROOK_TERRITORIES[squareIndex]);
		relevantOccupancy &= mask;
		moves |= ROOK_MOVES[squareIndex][(int)(relevantOccupancy * ROOK_MAGIC_FACTORS[squareIndex] >>> (64 - Long.bitCount(mask)))];
		moves &= ~friendly;
		return moves;
	}
	static final long kingMoves(long square, long friendly){
		int squareIndex = Long.numberOfTrailingZeros(square);
		long moves = KING_TERRITORIES[squareIndex];
		return moves & ~friendly;
	}
	static final long castlingMoves(int color, long square, boolean canCastleOnKingSide, boolean canCastleOnQueenSide){
		long moves = 0;
		
		if(canCastleOnKingSide){
			moves |= square >>> 2;
		}
		if(canCastleOnQueenSide){
			moves |= square << 2;
		}
		return moves;
	}
	static final long moves(int piece, int color, long square, long enPassantSquare,long friendly, long enemy, boolean canCastleOnKingSide, boolean canCastleOnQueenSide){
		switch(piece){
		case Position.PAWN:
			return pawnMoves(color, square, enPassantSquare, friendly, enemy);
		case Position.KNIGHT:
			return knightMoves(square, friendly);
		case Position.BISHOP:
			return bishopMoves(square, friendly, enemy);
		case Position.ROOK:
			return rookMoves(square, friendly, enemy);
		case Position.QUEEN:
			return queenMoves(square, friendly, enemy);
		case Position.KING:
			return kingMoves(square, friendly) | castlingMoves(color, square, canCastleOnKingSide, canCastleOnQueenSide);
		default:
			throw new AssertionError();
		}
	}
	static final String toBitBoardString(long board){
		StringBuilder sb = new StringBuilder();
		for(int i : Range.of(64)){
			long bit = 0x8000000000000000L >>> i;
			if(i % 8 == 0){
				sb.append(System.lineSeparator());
			}
			sb.append((board & bit) == 0 ? 0 : 1);
			board -= bit;
		}
		return sb.toString();
	}
	
	
	
	
	
	
	
	
	
	
	private static final long[] files(){
		long aFile = 0x8080808080808080L;
		long[] result = new long[8];
		for(int file : Range.of(8)){
			result[file] = aFile >>> file;
		}
		return result;
	}
	private static final long[] ranks(){
		long FirstRank = 0x00000000000000FFL;
		long[] result = new long[8];
		for(int rank : Range.of(8)){
			result[rank] = FirstRank << (rank * 8);
		}
		return result;
	}
	private static final long[] aDiagonals(){
		long[] result = new long[15];
		result[0] = 	0x8000000000000000L;
		result[1] = 	0x4080000000000000L;
		result[2] = 	0x2040800000000000L;
		result[3] = 	0x1020408000000000L;
		result[4] = 	0x0810204080000000L;
		result[5] = 	0x0408102040800000L;
		result[6] = 	0x0204081020408000L;
		result[7] = 	0x0102040810204080L;
		result[8] = 	0x0001020408102040L;
		result[9] = 	0x0000010204081020L;
		result[10] = 	0x0000000102040810L;
		result[11] =	0x0000000001020408L;
		result[12] =	0x0000000000010204L;
		result[13] = 	0x0000000000000102L;
		result[14] = 	0x0000000000000001L;
		return result;
	}
	private static final long[] dDiagonals(){
		long[] result = new long[15];
		result[0] = 	0x0000000000000080L;
		result[1] = 	0x0000000000008040L;
		result[2] = 	0x0000000000804020L;
		result[3] =		0x0000000080402010L;
		result[4] =	 	0x0000008040201008L;
		result[5] = 	0x0000804020100804L;
		result[6] = 	0x0080402010080402L;
		result[7] = 	0x8040201008040201L;
		result[8] = 	0x4020100804020100L;
		result[9] = 	0x2010080402010000L;
		result[10] = 	0x1008040201000000L;
		result[11] = 	0x0804020100000000L;
		result[12] = 	0x0402010000000000L;
		result[13] = 	0x0201000000000000L;
		result[14] = 	0x0100000000000000L;
		return result;
	}
	
	private static final long[][] pawnTerritories(){
		long[][] result = new long[2][64];
		for(int i : Range.of(64)){
			int column = i & 7; // column == 0 -> h file
			long territory = 0x500L;
			territory = i > 1 ? territory << (i - 1) : territory >>> (1 - i);
			long mask = column == 0 ? ~FILES[0] : column == 7 ? ~FILES[7] : -1;
			result[0][i] = territory & mask;
		}
		for(int i : Range.of(64)){
			int column = i & 7; // column == 0 -> h file
			long territory = 0x5;
			territory = i > 9 ? territory << (i - 9) : territory >>> (9 - i);
			long mask = column == 0 ? ~FILES[0] : column == 7 ? ~FILES[7] : -1;
			result[1][i] = territory & mask;
		}
		return result;
	}
	private static final long[] knightTerritories(){
		long[] result = new long[64];
		for(int i : Range.of(64)){
			int column = i & 7; // column == 0 -> h file
			long territory = 0xA1100110AL;
			territory = i > 18 ? territory << (i - 18) : territory >>> (18 - i);
			long mask = column < 2 ? ~(FILES[0] | FILES[1]) : column > 5 ? ~(FILES[7] | FILES[6]) : -1;
			result[i] = territory & mask;
		}
		return result;
	}
	private static final long[] bishopTerritories(){
		long[] result = new long[64];
		for(int i : Range.of(64)){
			int column = i & 7; // column == 0 -> h file
			int row = (i - column) / 8;
			long ascending = ASCENDING_DIAGONALS[14 - column - row];
			long descending = DESCENDING_DIAGONALS[7 - column + row];
			result[i] = ascending | descending;
		}
		return result;
	}
	private static final long[] rookTerritories(){
		long[] result = new long[64];
		for(int i : Range.of(64)){
			int column = i & 7; // column == 0 -> h file
			int row = (i - column) / 8;
			long file= FILES[7 - column];
			long rank = RANKS[row];
			result[i] = file | rank;
		}
		return result;
	}
	private static final long[] queenTerritories(){
		long[] result = new long[64];
		for(int i : Range.of(64)){
			result[i] = BISHOP_TERRITORIES[i] | ROOK_TERRITORIES[i];
		}
		return result;
	}
	private static final long[] kingTerritories(){
		long[] result = new long[64];
		for(int i : Range.of(64)){
			int column = i & 7; // column == 0 -> h file
			long territory = 0x70707L;
			territory = i > 9 ? territory << (i - 9) : territory >>> (9 - i);
			long mask = column == 0 ? ~FILES[0] : column == 7 ? ~FILES[7] : -1;
			result[i] = territory & mask;
		}
		return result;
	}
	private static final long bishopRelevantOccupancy(long territory){
		return territory & 0x7E7E7E7E7E7E00L;
	}
	private static final long rookRelevantOccupancy(long square, int squareIndex, long territory){
		territory &= ~(((square & ~FILES[0]) >>> squareIndex) * FILES[0]);
		territory &= ~(((square & ~FILES[7]) >>> squareIndex) * FILES[7]);
		territory &= ~(((square & ~RANKS[0]) >>> squareIndex) * RANKS[0]);
		territory &= ~(((square & ~RANKS[7]) >>> squareIndex) * RANKS[7]);
		return territory;
	}
	private static final long[][] bishopMoves(){
		long[][] result = new long[64][];
		for(int i : Range.of(64)){
			long bishop = 1L << i;
			long mask = bishopRelevantOccupancy(BISHOP_TERRITORIES[i]);
			
			int numOfBits = Long.bitCount(mask);
			int size = (int)Math.pow(2, numOfBits);
			result[i] = new long[size];
			long[] relevantOccupancies = new long[size];
			for(int s : Range.of(size)){
				relevantOccupancies[s] = generateRelevantOccupancy(mask, s);
			}
			for(int j : Range.of(size)){
				long pieces = findRelevantOccupancy(relevantOccupancies, numOfBits, BISHOP_MAGIC_FACTORS[i], j);
				result[i][j] = generateBishopMoves(bishop, pieces);
			}
		}
		return result;
	}
	private static final long[][] rookMoves(){
		long[][] result = new long[64][];
		for(int i : Range.of(64)){
			long square = 1L << i;
			long mask = rookRelevantOccupancy(square, i, ROOK_TERRITORIES[i]);
			
			int numOfBits = Long.bitCount(mask);
			int size = (int)Math.pow(2, numOfBits);
			result[i] = new long[size];
			long[] relevantOccupancies = new long[size];
			for(int s : Range.of(size)){
				relevantOccupancies[s] = generateRelevantOccupancy(mask, s);
			}
			for(int j : Range.of(size)){
				long pieces = findRelevantOccupancy(relevantOccupancies, numOfBits, ROOK_MAGIC_FACTORS[i], j);
				result[i][j] = generateRookMoves(square, pieces);
			}
		}
		return result;
	}
	private static final long findRelevantOccupancy(long[] relevantOccupancies, int numOfBits, long magic, int index){
		for(long relevantOccupancy : relevantOccupancies){
			int generatedIndex = (int)(relevantOccupancy * magic >>> (64 - numOfBits));
			if(index == generatedIndex)return relevantOccupancy;
		}
		throw new IllegalArgumentException();
	}
	private static final long generateBishopMoves(long square, long pieces){
		long moves = 0;
		moves |= generateRayMoves(square, pieces, -9);
		moves |= generateRayMoves(square, pieces, -7);
		moves |= generateRayMoves(square, pieces, 7);
		moves |= generateRayMoves(square, pieces, 9);
		
		return moves;
	}
	private static final long generateRookMoves(long square, long pieces){
		long moves = 0;
		moves |= generateRayMoves(square, pieces, -8);
		moves |= generateRayMoves(square, pieces, -1);
		moves |= generateRayMoves(square, pieces, 1);
		moves |= generateRayMoves(square, pieces, 8);
		
		return moves;
	}
	private static final long generateRelevantOccupancy(long mask, long index){
		int size = Long.bitCount(mask);
		long result = 0;
		for(int i : Range.of(size)){
			long bit = 1L << i;
			result |= ((bit & index) >>> i) * Long.lowestOneBit(mask);
			mask &= mask - 1;
		}
		return result;
	}
	private static final long generateRayMoves(long square, long pieces, int direction){
		Optional<Long> next = next(square, direction);
		long moves = 0;
		while(next.isPresent()){
			moves |= next.get();
			next = next.filter(n -> (n & pieces) == 0).flatMap(n -> next(n, direction));
		}
		return moves;
	}
	private static final Optional<Long> next(long square, int direction){
		long blackSquares = 0xAA55AA55AA55AA55L;
		long whiteSquares = 0x55AA55AA55AA55AAL;
		
		long mask = 0;
		long result = 0;
		switch(direction){
		case -9:
			mask = (whiteSquares & square) == 0 ? blackSquares : whiteSquares;
			result = (square << 9) & mask;
			return result == 0 ? Optional.empty() : Optional.of(result);
		case -8:
			result = square << 8;
			return result == 0 ? Optional.empty() : Optional.of(result);
		case -7:
			mask = (whiteSquares & square) == 0 ? blackSquares : whiteSquares;
			result = (square << 7) & mask;
			return result == 0 ? Optional.empty() : Optional.of(result);
		case -1:
			mask = ~FILES[7];
			result = (square << 1) & mask;
			return result == 0 ? Optional.empty() : Optional.of(result);
		case 1:
			mask = ~FILES[0];
			result = (square >>> 1) & mask;
			return result == 0 ? Optional.empty() : Optional.of(result);
		case 7:
			mask = (whiteSquares & square) == 0 ? blackSquares : whiteSquares;
			result = (square >>> 7) & mask;
			return result == 0 ? Optional.empty() : Optional.of(result);
		case 8:
			result = square >>> 8;
			return result == 0 ? Optional.empty() : Optional.of(result);
		case 9:
			mask = (whiteSquares & square) == 0 ? blackSquares : whiteSquares;
			result = (square >>> 9) & mask;
			return result == 0 ? Optional.empty() : Optional.of(result);
		default:
			throw new IllegalArgumentException();
		}
	}
}
