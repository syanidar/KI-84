package jp.gr.java_conf.syanidar.chess.hamster.game;

import java.util.Random;
import java.util.function.ToIntFunction;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.PieceEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class HamsterEvaluator implements Evaluator<ChessPosition, CentiPawn> {
	private static final HamsterEvaluator INSTANCE = new HamsterEvaluator();
	private static final Random RANDOM = new Random();
	private HamsterEvaluator(){}
	
	@Override
	public CentiPawn evaluate(ChessPosition p) {		
		ToIntFunction<Square> func = s -> {
			switch(s.piece().get().toEnum()){
			case PAWN:
				int result = 100;
				for(Square e : p.board().squaresMatch(square -> square.isOnFile(s.file()))){
					if(e.piece().filter(piece -> piece.isEqualTo(PieceEnum.PAWN)).isPresent()){
						result = 50;
						break;
					}
				}
				if(s.piece().get().color() == ColorEnum.WHITE)return result + (s.row() - 1) * 8;
				else return result + (6 - s.row()) * 8;
			case KNIGHT:	
				boolean isOnTheCenter = 1 < s.column() && s.column() < 6 && 1 < s.row() && s.row() < 6;
				return 300 + (isOnTheCenter ? 20 : 0);
			case BISHIP:	return 310;
			case ROOK:	{
					boolean gripsAFile = true;
					for(Square e : p.board().squaresMatch(square -> square.isOnFile(s.file()))){
						if(e.piece().filter(piece -> piece.isEqualTo(PieceEnum.PAWN)).isPresent()){
							gripsAFile = false;
							break;
						}
					}
					return 500 + (gripsAFile ? 50 : 0) + (s.row() == 6 ? 50 : 0);
				}
			case QUEEN:	{
					boolean gripsAFile = true;
					for(Square e : p.board().squaresMatch(square -> square.isOnFile(s.file()))){
						if(e.piece().filter(piece -> piece.isEqualTo(PieceEnum.PAWN)).isPresent()){
							gripsAFile = false;
							break;
						}
					}
					return 900 + (gripsAFile ? 50 : 0) + (s.row() == 6 ? 50 : 0);
				}
			default:		return 0;
			}
		};
		Board board = p.board();
		int white = board.squaresMatch(s -> s.isOccupiedBy(ColorEnum.WHITE)).stream().mapToInt(func).sum();
		int black = board.squaresMatch(s -> s.isOccupiedBy(ColorEnum.BLACK)).stream().mapToInt(func).sum();
		return new CentiPawn(white - black + RANDOM.nextInt(11) - 5);
	}
	@Override
	public CentiPawn lowerBound(){
		return new CentiPawn(Integer.MIN_VALUE / 2);
	}
	@Override
	public CentiPawn upperBound(){
		return new CentiPawn(Integer.MAX_VALUE / 2);
	}
	@Override
	public CentiPawn evaluateIfTerminated(ChessPosition p, int depth){
		if(p.isInCheck())return p.theFirstPlayerHasTheMove() ? new CentiPawn(-100000 - depth) : new CentiPawn(100000 + depth);
		return new CentiPawn(0);
	}
	public static final HamsterEvaluator getInstance(){
		return INSTANCE;
	}
}
