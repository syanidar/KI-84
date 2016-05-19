package jp.gr.java_conf.syanidar.games.chess.rat;
import static jp.gr.java_conf.syanidar.games.chess.rat.Position.*;

import java.util.Random;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;

public class MaterialEvaluator implements Evaluator<Position, CentiPawn> {
	private static final Random RANDOM = new Random();
	private static final MaterialEvaluator INSTANCE = new MaterialEvaluator();
	private static final CentiPawn LOWER_BOUND = CentiPawn.MIN_VALUE;
	private static final CentiPawn UPPER_BOUND = CentiPawn.MAX_VALUE;
	private static final CentiPawn ZERO = new CentiPawn(0);
	private MaterialEvaluator(){}
	public static final MaterialEvaluator getInstance(){
		return INSTANCE;
	}
	@Override
	public CentiPawn evaluate(Position position) {
		if(position.duplicatesThePreviousPositions())return ZERO;
		if(position.fiftyMoveRuleCanBeApplied())return ZERO;
		int result = 0;
		for (int color : COLORS) {
			long occupancy = position.occupancy(color);
			for (long square = Long.lowestOneBit(occupancy); occupancy != 0; occupancy &= occupancy - 1, square = Long
					.lowestOneBit(occupancy)) {
				int piece = position.pieceOn(square);
				int sign = color == WHITE? 1 : -1;
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
		return new CentiPawn(result + RANDOM.nextInt(3) - 1);
	}
	@Override
	public CentiPawn lowerBound() {
		return LOWER_BOUND;
	}
	@Override
	public CentiPawn upperBound() {
		return UPPER_BOUND;
	}
	@Override
	public CentiPawn evaluateIfTerminated(Position position, int depth) {
		int sign = position.theFirstPlayerHasTheMove()? 1 : -1;
		if(position.playerIsInCheck())return new CentiPawn(sign * (-10000 - depth));
		if(position.playerChecks())return new CentiPawn(sign * (10000 + depth));
		return ZERO;
	}
}
