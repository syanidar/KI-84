package jp.gr.java_conf.syanidar.chess.hamster.game;

import java.util.function.Predicate;

import jp.gr.java_conf.syanidar.algorithm.mosquito.game.AIPlayer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.HumanPlayer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.MoveSelector;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Player;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaSetting;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.MinimaxResult;

public class ChessUtility {
	private ChessUtility(){}
	
	public static final Player<ChessPosition, MinimaxResult<CentiPawn>> ai(int depth){
		HamsterEvaluator he = HamsterEvaluator.getInstance();
		AlphaBetaAnalyzer<ChessPosition, CentiPawn> aba = new AlphaBetaAnalyzer<>(he);
		AlphaBetaSetting<ChessPosition, CentiPawn> abs = new AlphaBetaSetting<>(depth, he);
		return new AIPlayer<>(aba, abs, he);
	}
	public static final Player<ChessPosition, MinimaxResult<CentiPawn>> ai(int depth, Predicate<ChessPosition> predicate){
		HamsterEvaluator he = HamsterEvaluator.getInstance();
		AlphaBetaAnalyzer<ChessPosition, CentiPawn> aba = new AlphaBetaAnalyzer<>(he);
		AlphaBetaSetting<ChessPosition, CentiPawn> abs = new AlphaBetaSetting<>(depth, he, predicate);
		return new AIPlayer<>(aba, abs, he);
	}
	public static final Player<ChessPosition, MinimaxResult<CentiPawn>> human(MoveSelector selector){
		return new HumanPlayer<>(selector);
	}
}
