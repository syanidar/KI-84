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

class PawnPushMoveGenerator implements MoveGenerator {
	private final MoveRecorder recorder;
	private final Square square;
	
	PawnPushMoveGenerator(MoveRecorder m, Square s){
		recorder = m;
		square = s;
	}
	@Override
	public List<Move> generate(ColorEnum color) {
		if(color == null)throw new IllegalArgumentException();

		List<Move> result = new ArrayList<>();
		DirectionEnum direction = color == ColorEnum.WHITE ? DirectionEnum.NORTH : DirectionEnum.SOUTH; 
		Optional<Square> next = square.next(direction).filter(n -> !n.isOccupied());
		
		if(next.isPresent()){
			if (next.get().coordinates().row() != (color == ColorEnum.WHITE ? 7 : 0)) {
				Move move = new Move(recorder, new Walk(square, next.get()));
				result.add(move);
			}else{
				Move queen = new Move(recorder, new Walk(square, next.get()), new Promotion(next.get(), square.piece().get(), new Queen(color)));
				Move rook = new Move(recorder, new Walk(square, next.get()), new Promotion(next.get(), square.piece().get(), new Rook(color)));
				Move bishop = new Move(recorder, new Walk(square, next.get()), new Promotion(next.get(), square.piece().get(), new Bishop(color)));
				Move knight =new Move(recorder, new Walk(square, next.get()), new Promotion(next.get(), square.piece().get(), new Knight(color)));
				result.add(queen);
				result.add(rook);
				result.add(bishop);
				result.add(knight);
			}
		}
		if(square.coordinates().row() == (color == ColorEnum.WHITE ? 1 : 6)){
			next.flatMap(n -> n.next(direction)).filter(n -> !n.isOccupied()).ifPresent(n -> result.add(new Move(recorder, new Walk(square, n))));
		}

		return result;
	}
}
