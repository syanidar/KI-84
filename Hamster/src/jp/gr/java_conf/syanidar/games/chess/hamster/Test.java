package jp.gr.java_conf.syanidar.games.chess.hamster;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Game;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.MoveSelector;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.NoMoveHandler;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Player;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Viewer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaResult;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;
import jp.gr.java_conf.syanidar.chess.hamster.game.ChessPosition;
import jp.gr.java_conf.syanidar.chess.hamster.game.ChessUtility;
import jp.gr.java_conf.syanidar.util.collection.MapUtility;

public class Test {
	
	public static final void main(String...strings){
		
		ChessPosition position = new ChessPosition();
		Player<ChessPosition, AlphaBetaResult<CentiPawn>> white = strings[0].equals("AI") ? ChessUtility.ai(0, 3, p -> p.isQuiet()) : ChessUtility.human(new ChessMoveSelector());
		Player<ChessPosition, AlphaBetaResult<CentiPawn>> black = strings[1].equals("AI") ? ChessUtility.ai(0, 3, p -> p.isQuiet()) : ChessUtility.human(new ChessMoveSelector());
		Game<ChessPosition, AlphaBetaResult<CentiPawn>> game = new Game<>(position, white, black, new ChessViewer());
		ChessNoMoveHandler ch = new ChessNoMoveHandler();
		
		while(!ch.terminated){
			game.play(ch);
			Toolkit.getDefaultToolkit().beep();
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
	private static class ChessViewer implements Viewer<ChessPosition, AlphaBetaResult<CentiPawn>>{
		@Override
		public void updateBoard(ChessPosition position) {
			System.out.println(position);
		}
		@Override
		public void updateResults(Map<String, AlphaBetaResult<CentiPawn>> results) {				
			if(!results.isEmpty()){
				System.out.println(MapUtility.sortByValues(results));
			}
		}
	}
}
