package jp.gr.java_conf.syanidar.chess.hamster;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Game;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.MoveSelector;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.NoMoveHandler;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Player;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Viewer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.MinimaxResult;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;
import jp.gr.java_conf.syanidar.chess.hamster.game.ChessPosition;
import jp.gr.java_conf.syanidar.chess.hamster.game.ChessUtility;

public class Test {
	
	public static final void main(String...strings){
		
		ChessPosition position = new ChessPosition();
		Player<ChessPosition, MinimaxResult<CentiPawn>> white = strings[0].equals("AI") ? ChessUtility.ai(3, p -> p.isQuiet()) : ChessUtility.human(new ChessMoveSelector());
		Player<ChessPosition, MinimaxResult<CentiPawn>> black = strings[1].equals("AI") ? ChessUtility.ai(3, p -> p.isQuiet()) : ChessUtility.human(new ChessMoveSelector());
		Game<ChessPosition, MinimaxResult<CentiPawn>> game = new Game<>(position, white, black, new ChessViewer());
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
	private static class ChessViewer implements Viewer<ChessPosition, MinimaxResult<CentiPawn>>{
		@Override
		public void drawBoard(ChessPosition position) {
			System.out.println(position);
		}
		@Override
		public void drawResults(Map<String, MinimaxResult<CentiPawn>> results) {				
			List<Entry<String, MinimaxResult<CentiPawn>>> list = new ArrayList<>(results.entrySet());
			Collections.sort(list, (e0, e1) -> e0.getValue().evaluation().compareTo(e1.getValue().evaluation()));
			for(Entry<String, MinimaxResult<CentiPawn>> e : list){
				System.out.println(e.getKey() + ":");
				System.out.println("	" + e.getValue().expectedLine());
				System.out.println("	" + e.getValue().evaluation());
			}
		}
	}
}
