package jp.gr.java_conf.syanidar.games.chess.rat;

import java.awt.Toolkit;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Game;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.NoMoveHandler;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Player;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Viewer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.MinimaxResult;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;
import jp.gr.java_conf.syanidar.util.collection.MapUtility;

public class Test {
//	private static final MoveSelector MOVE_SELECTOR = new MoveSelector(){
//		@Override
//		public String select(List<String> moves) {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//			
//			System.out.println(moves);
//			System.out.print("Select the move >");
//			
//			String input = null;
//			try {
//				input = reader.readLine();
//			} catch (IOException e) {
//				e.printStackTrace();
//				throw new RuntimeException();
//			}
//			return input;
//		}
//	};
	private static final Viewer<Position, MinimaxResult<CentiPawn>> VIEWER = new Viewer<Position, MinimaxResult<CentiPawn>>(){
		@Override
		public void updateBoard(Position position) {
			Iterator<Square> iterator = Square.fromA8ToH1();
			while(iterator.hasNext()){
				Square square = iterator.next();
				Optional<Chessman> piece = position.pieceOn(square);
				if(piece.isPresent()){
					System.out.print(piece.get());
				}else{
					System.out.print(" ");
				}
				if(square.file() == File.H){
					System.out.println();
				}
			}
			System.out.println();
			System.out.println(position.createFENManager());
		}
		@Override
		public void updateResults(Map<String, MinimaxResult<CentiPawn>> results) {
			if (!results.isEmpty()) {
				System.out.println(results);
				System.out.println(MapUtility.sortByValues(results));
			}
		}
	};
	private static final NoMoveHandler<Position> NO_MOVE_HANDLER = new NoMoveHandler<Position>(){
		
		@Override
		public void handle(Position position) {
			throw new RuntimeException();
		}
	};
	private Test(){}
	
	public static final void main(String[] args){
		Position p = new Position();
		Player<Position, MinimaxResult<CentiPawn>> white = ChessUtility.ai(5);
		Player<Position, MinimaxResult<CentiPawn>> black = ChessUtility.ai(5);
		Game<Position, MinimaxResult<CentiPawn>> game = new Game<>(p, white, black, VIEWER);
		
		while(!p.isTerminated()){
			game.play(NO_MOVE_HANDLER);
			Toolkit.getDefaultToolkit().beep();
		}
		VIEWER.updateBoard(p);
	}
}
