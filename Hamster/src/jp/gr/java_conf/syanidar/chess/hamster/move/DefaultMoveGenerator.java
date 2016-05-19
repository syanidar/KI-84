package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class DefaultMoveGenerator implements LegalMoveGenerator{
	private final Board board;
	private final ScoreSheet ss;
	private final LegalityTester lt;
	
	public DefaultMoveGenerator(Board b, ScoreSheet s, LegalityTester t){
		if(b == null || s == null || t == null)throw new IllegalArgumentException();
		board = b;
		ss = s;
		lt = t;
	}
	@Override
	public List<Move> generate(ColorEnum color) {
		if(color == null)throw new IllegalArgumentException();
		Square kingsRook = color == ColorEnum.WHITE ? board.squareAt(Coordinates.of(7, 0)) : board.squareAt(Coordinates.of(7, 7));
		Square queensRook = color == ColorEnum.WHITE ? board.squareAt(Coordinates.of(0, 0)) : board.squareAt(Coordinates.of(0, 7));
		List<Square> pieces = board.squaresMatch(s -> s.isOccupied() && s.piece().get().color() == color);
		List<Move> result = new ArrayList<>();
		for(Square piece : pieces){
			List<MoveGenerator> gens = PieceStateGenerator.generate(new MoveRecorder(){
				@Override
				public void record(Move move) {
					ss.add(move);
				}
				@Override
				public void undo() {
					ss.removeLast();
				}
			}, board, piece, kingsRook, queensRook, ss).moveGenerators();
			gens.forEach(g -> result.addAll(g.generate(color)));
		}
		return result;
	}
	@Override
	public List<Move> generateLegalMoves(ColorEnum color) {
		return generate(color).stream().filter(m -> lt.isLegal(m, color)).collect(Collectors.toList());
	}
}
