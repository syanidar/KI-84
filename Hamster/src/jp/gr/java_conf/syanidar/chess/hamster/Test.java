package jp.gr.java_conf.syanidar.chess.hamster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.AlphaBetaSetting;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;
import jp.gr.java_conf.syanidar.chess.hamster.game.ChessMove;
import jp.gr.java_conf.syanidar.chess.hamster.game.ChessPosition;
import jp.gr.java_conf.syanidar.chess.hamster.game.HamsterEvaluator;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.utils.collection.ArrayUtility;
import jp.gr.java_conf.syanidar.utils.collection.ListUtility;

public class Test {

	public static void main(String[] args) throws IOException {
		AlphaBetaAnalyzer<ChessPosition, CentiPawn> ms = new AlphaBetaAnalyzer<>(HamsterEvaluator.getInstance());
		ChessPosition cp = new ChessPosition();
		while(true){
			System.out.println(cp);
			List<ChessMove> moveList = cp.moves();
			if(moveList.isEmpty())break;
			if (cp.colorToPlay() == ColorEnum.BLACK || cp.colorToPlay() == ColorEnum.WHITE) {
				ChessMove[] moves = moveList.toArray(new ChessMove[0]);
				int length = moves.length;
				CentiPawn[] evaluations = new CentiPawn[length];
				int bestIndex = -1;
				List<String> sequence = new ArrayList<>();
				for (int i = 0; i < length; i++) {
					List<String> buf = new ArrayList<>();
					moves[i].play();
					evaluations[i] = ms.evaluate(cp, new AlphaBetaSetting(2), buf);
					moves[i].undo();
					if(bestIndex == -1 || evaluations[i].isBetterThan(evaluations[bestIndex])){
						bestIndex = i;
						sequence.clear();
						sequence.add(moves[i].toString());
						sequence.addAll(buf);
					}
				}		
				moves[bestIndex].play();
				ArrayUtility.sort(evaluations, moves);
				System.out.println(Arrays.asList(moves));
				System.out.println(Arrays.asList(evaluations));
				System.out.println(sequence);
				
				System.out.println();
			}else{
				select(moveList).play();	
				System.out.println();
			}
		}
	}
	
	private static final ChessMove select(List<ChessMove> moves) throws IOException{
		System.out.println(Arrays.asList(moves));
		System.out.print("Select the move > ");
		List<String> notations = ListUtility.map(moves, m -> m.toString());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		for(int i = 0, n = moves.size(); i < n; i++){
			if(input.equals(notations.get(i)))return moves.get(i);
		}
		System.out.println("Invalid");
		return select(moves);
	}
}
