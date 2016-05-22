package jp.gr.java_conf.syanidar.games.chess.rat;

import java.awt.Toolkit;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import jp.gr.java_conf.syanidar.algorithm.mosquito.game.AIPlayer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Game;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.NoMoveHandler;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Player;
import jp.gr.java_conf.syanidar.algorithm.mosquito.game.Viewer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaResult;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaSetting;
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
	static boolean isTerminated;
	private static final Viewer<Position, AlphaBetaResult<CentiPawn>> VIEWER = new Viewer<Position, AlphaBetaResult<CentiPawn>>(){
		@Override
		public void updateBoard(Position position) {
			System.out.println();
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
			System.out.println(position);
		}
		@Override
		public void updateResults(Map<String, AlphaBetaResult<CentiPawn>> results) {
			if (!results.isEmpty()) {
				long leafNodes = results.values().stream().mapToLong(r -> r.leafNodes()).sum();
				System.out.println();
				System.out.printf("The number of leaf nodes is %d.\n", leafNodes);
				System.out.println(results);
				System.out.println(MapUtility.sortByValues(results));
			}
		}
	};
	private static final NoMoveHandler<Position> NO_MOVE_HANDLER = new NoMoveHandler<Position>(){
		
		@Override
		public void handle(Position position) {
			isTerminated = true;
			if(position.isTerminated()){
				if(position.playerIsInCheck()){System.out.println(position.sideToMove().opposite() + " wins!!");}
				else{System.out.println("The game has drawn.");}
			}else{System.out.println(position.sideToMove() + " has claimed to draw.");}
		}
	};
	private Test(){}
	
	public static final void main(String[] args){
		Position position = new Position();
		MaterialEvaluator evaluator = MaterialEvaluator.getInstance();
		AlphaBetaAnalyzer<Position, CentiPawn> analyzer = new AlphaBetaAnalyzer<>(evaluator);
		AlphaBetaSetting<Position, CentiPawn> quiescence = new AlphaBetaSetting.Builder<>(2, evaluator).maxDepthMoveOrderingApplied(3).build();
		AlphaBetaSetting<Position, CentiPawn> normal = new AlphaBetaSetting.Builder<>(4, evaluator).build();
		Player<Position, AlphaBetaResult<CentiPawn>> qPlayer = new AIPlayer<>(analyzer, quiescence, evaluator);
		Player<Position, AlphaBetaResult<CentiPawn>> nPlayer = new AIPlayer<>(analyzer, normal, evaluator);
		Player<Position, AlphaBetaResult<CentiPawn>> white = qPlayer;
		Player<Position, AlphaBetaResult<CentiPawn>> black = nPlayer;
		Game<Position, AlphaBetaResult<CentiPawn>> game = new Game<>(position, white, black, VIEWER);
		
		while(!isTerminated){
			game.play(NO_MOVE_HANDLER);
			Toolkit.getDefaultToolkit().beep();
		}
	}
}
