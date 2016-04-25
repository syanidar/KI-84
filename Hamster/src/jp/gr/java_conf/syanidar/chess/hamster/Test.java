package jp.gr.java_conf.syanidar.chess.hamster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

import jp.gr.java_conf.syanidar.algorithm.mosquito.game.AIPlayer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Game;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.HumanPlayer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.MoveSelector;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.NoMoveHandler;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Viewer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaSetting;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;
import jp.gr.java_conf.syanidar.chess.hamster.game.ChessPosition;
import jp.gr.java_conf.syanidar.chess.hamster.game.HamsterEvaluator;

public class Test {
	
	public static final void main(String...strings){
		HamsterEvaluator he = HamsterEvaluator.getInstance();
		AlphaBetaAnalyzer<ChessPosition, CentiPawn> aba = new AlphaBetaAnalyzer<>(he);
		AlphaBetaSetting<CentiPawn> abs = new AlphaBetaSetting<>(3, he);
		AIPlayer<ChessPosition, CentiPawn, AlphaBetaSetting<CentiPawn>> ap = new AIPlayer<>(aba, abs, he);
		HumanPlayer<ChessPosition> hp = new HumanPlayer<>(new ChessMoveSelector());
		ChessPosition position = new ChessPosition();
		Game<ChessPosition> game = new Game<>(position, hp, ap, new ChessViewer());
		ChessNoMoveHandler ch = new ChessNoMoveHandler();
		
		while(!ch.terminated){
			game.play(ch);
			if(position.theFirstPlayerHasTheMove()){
				System.out.println(ap.evaluationMap());
			}
		}
	}
	private static class ChessNoMoveHandler implements NoMoveHandler<ChessPosition>{
		private boolean terminated;
		@Override
		public void handle(ChessPosition position) {
			if(position.isInCheck()){
				System.out.println(position.colorToPlay().opposite() + " WINS THE GAME.");
			}
			terminated = true;
		}
	}
	private static class ChessMoveSelector implements MoveSelector{
		@Override
		public String select(List<String> moves) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println(moves);
			System.out.print("Select the move >");
			
			String input = null;
			try {
				input = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			return input;
		}
	}
	private static class ChessViewer implements Viewer<ChessPosition>{
		@Override
		public void drawBoard(ChessPosition position) {
			System.out.println(position);
		}
		@Override
		public void drawComputedLine(Optional<List<String>> line) {
			line.ifPresent(System.out::println);
		}
	}
}
