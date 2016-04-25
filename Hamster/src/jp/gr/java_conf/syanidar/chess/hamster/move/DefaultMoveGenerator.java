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
	
	public DefaultMoveGenerator(Board b, ScoreSheet s){
		if(b == null || s == null)throw new IllegalArgumentException();
		board = b;
		ss = s;
	}
	@Override
	public List<Move> generate(ColorEnum color) {
		if(color == null)throw new IllegalArgumentException();
		Square kingsRook = color == ColorEnum.WHITE ? board.squareAt(new Coordinates("h1")) : board.squareAt(new Coordinates("h8"));
		Square queensRook = color == ColorEnum.WHITE ? board.squareAt(new Coordinates("a1")) : board.squareAt(new Coordinates("a8"));
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
	public List<Move> generateLegalMoves(LegalityTester lt, ColorEnum color) {
		return generate(color).stream().filter(m -> lt.isLegal(m, color)).collect(Collectors.toList());
	}
}
