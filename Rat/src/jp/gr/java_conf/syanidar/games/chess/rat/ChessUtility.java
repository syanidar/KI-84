package jp.gr.java_conf.syanidar.games.chess.rat;

import java.util.function.Predicate;

import jp.gr.java_conf.syanidar.algorithm.mosquito.game.AIPlayer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.HumanPlayer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.MoveSelector;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Player;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaResult;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaSetting;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;

public class ChessUtility {
	private ChessUtility(){}
	public static final Player<Position, AlphaBetaResult<CentiPawn>> ai(int depth){
		MaterialEvaluator he = MaterialEvaluator.getInstance();
		AlphaBetaAnalyzer<Position, CentiPawn> aba = new AlphaBetaAnalyzer<>(he);
		AlphaBetaSetting<Position, CentiPawn> abs = new AlphaBetaSetting<>(depth - 1, he);
		return new AIPlayer<>(aba, abs, he);
	}
	public static final Player<Position, AlphaBetaResult<CentiPawn>> ai(int minDepth, int maxDepth, Predicate<Position> predicate){
		MaterialEvaluator he = MaterialEvaluator.getInstance();
		AlphaBetaAnalyzer<Position, CentiPawn> aba = new AlphaBetaAnalyzer<>(he);
		AlphaBetaSetting<Position, CentiPawn> abs = new AlphaBetaSetting<>(minDepth, maxDepth, he, predicate);
		return new AIPlayer<>(aba, abs, he);
	}

	public static final Player<Position, AlphaBetaResult<CentiPawn>> human(MoveSelector selector){
		return new HumanPlayer<>(selector);
	}
}
