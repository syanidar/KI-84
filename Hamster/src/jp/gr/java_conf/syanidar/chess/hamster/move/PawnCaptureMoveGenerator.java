package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Bishop;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Knight;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Queen;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Rook;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class PawnCaptureMoveGenerator implements MoveGenerator {
	private final MoveRecorder recorder;
	private final DirectionEnum direction;
	private final Square square;
	
	PawnCaptureMoveGenerator(MoveRecorder r, DirectionEnum d, Square s) {
		recorder = r;
		direction = d;
		square = s;
	}
	@Override
	public List<Move> generate(ColorEnum color) {
		List<Move> result = new ArrayList<>();
		Optional<Square> next = square.next(direction);
		
		next = next.filter(n -> n.isOccupiedBy(color.opposite()));
		if(!next.isPresent())return result;
		
		if (next.get().coordinates().row() != (color == ColorEnum.WHITE ? 7 : 0)) {
			Move move = new Move(recorder, new Elimination(next.get()), new Walk(square, next.get()));
			result.add(move);
		}else{
			Move queen = new Move(recorder, new Elimination(next.get()), new Walk(square, next.get()), new Promotion(next.get(), square.piece().get(), new Queen(color)));
			Move rook = new Move(recorder, new Elimination(next.get()), new Walk(square, next.get()), new Promotion(next.get(), square.piece().get(), new Rook(color)));
			Move bishop = new Move(recorder, new Elimination(next.get()), new Walk(square, next.get()), new Promotion(next.get(), square.piece().get(), new Bishop(color)));
			Move knight =new Move(recorder, new Elimination(next.get()), new Walk(square, next.get()), new Promotion(next.get(), square.piece().get(), new Knight(color)));
			
			result.add(queen);
			result.add(rook);
			result.add(bishop);
			result.add(knight);
		}
		return result;
	}
}
