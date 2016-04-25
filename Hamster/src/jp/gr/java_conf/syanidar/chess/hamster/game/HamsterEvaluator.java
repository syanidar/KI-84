package jp.gr.java_conf.syanidar.chess.hamster.game;

import java.util.function.ToIntFunction;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.PieceEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class HamsterEvaluator implements Evaluator<ChessPosition, CentiPawn> {
	private static final HamsterEvaluator INSTANCE = new HamsterEvaluator();
	private HamsterEvaluator(){}
	
	@Override
	public CentiPawn evaluate(ChessPosition p) {		
		ToIntFunction<Square> func = s -> {
			switch(s.piece().get().toEnum()){
			case PAWN:
				for(Square e : p.board().squaresMatch(square -> square.isOnFile(s.file()))){
					if(e.piece().filter(piece -> piece.isEqualTo(PieceEnum.PAWN)).isPresent())
						return 50;
				}
				return 100;
			case KNIGHT:	return 300;
			case BISHIP:	return 300;
			case ROOK:		return 500;
			case QUEEN:		return 900;
			default:		return 0;
			}
		};
		Board board = p.board();
		int white = board.squaresMatch(s -> s.isOccupiedBy(ColorEnum.WHITE)).stream().mapToInt(func).sum();
		int black = board.squaresMatch(s -> s.isOccupiedBy(ColorEnum.BLACK)).stream().mapToInt(func).sum();
		return new CentiPawn(white - black + p.mobility());
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
