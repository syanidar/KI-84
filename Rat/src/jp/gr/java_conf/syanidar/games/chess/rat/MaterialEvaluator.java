package jp.gr.java_conf.syanidar.games.chess.rat;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.BISHOP;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.COLORS;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.KING;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.KNIGHT;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.PAWN;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.QUEEN;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.ROOK;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.WHITE;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;

public class MaterialEvaluator implements Evaluator<Position, CentiPawn> {
	private static final MaterialEvaluator INSTANCE = new MaterialEvaluator();
	private static final CentiPawn DRAW_BORDER = new CentiPawn(-200);

	
	
	private MaterialEvaluator(){}
	public static final MaterialEvaluator getInstance(){
		return INSTANCE;
	}
	
	
	
	@Override
	public CentiPawn evaluate(Position position, int depth) {	
		if(!position.hasMoves()){
			if(position.playerIsInCheck())return new CentiPawn(position.theFirstPlayerHasTheMove()? -10000 - depth : 10000 + depth); 
			else return CentiPawn.ZERO;
		}
		int result = 0;
		for (int color : COLORS) {		
			int sign = color == WHITE? 1 : -1;
			long occupancy = position.occupancy(color);
			for (long square = Long.lowestOneBit(occupancy); occupancy != 0; occupancy &= occupancy - 1, square = Long
					.lowestOneBit(occupancy)) {
				int piece = position.pieceOn(square);
				switch (piece) {
				case PAWN:
					result += sign * 100;break;
				case KNIGHT:
					result += sign * 300;break;
				case BISHOP:
					result += sign * 300;break;
				case ROOK:
					result += sign * 500;break;
				case QUEEN:
					result += sign * 900;break;
				case KING:
					result += sign * 10000;break;
				default: throw new AssertionError();
				}
			} 
		}
		
		return new CentiPawn(result + position.mobility());
	}
	@Override
	public CentiPawn lowerBound() {
		return CentiPawn.MIN_VALUE;
	}
	@Override
	public CentiPawn upperBound() {
		return CentiPawn.MAX_VALUE;
	}
	@Override
	public CentiPawn drawEvaluation() {
		return CentiPawn.ZERO;
	}
	@Override
	public boolean claimsDraw(CentiPawn evaluation) {
		return DRAW_BORDER.isBetterThan(evaluation);
	}
}
