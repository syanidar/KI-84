package jp.gr.java_conf.syanidar.chess.hamster.materials;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.*;

import java.util.Optional;

public class Square extends Coordinates{

	private final ColorEnum color;
	private final Board board;
	
	private Optional<Piece> piece;
	
	Square(int column, int row, ColorEnum c, Board b) {
		super(column, row);
		assert c != null;
		assert b != null;
		color = c;
		board = b;
		piece = Optional.empty();
	}
	
	public ColorEnum color(){return color;}
	public void put(Piece p){
		assert !piece.isPresent();
		assert p != null;
		
		piece = Optional.of(p);
	}
	public Board board(){return board;}
	public Piece remove(){
		assert piece.isPresent();
		Piece p = piece.get();
		piece = Optional.empty();
		return p;
	}
	public Optional<Piece> piece(){
		return piece;
	}
	public boolean isOccupied(){return piece.isPresent();}
	public boolean isOccupiedBy(ColorEnum color){
		return piece.isPresent() && piece.get().color() == color;
	}
	public Optional<Square> next(DirectionEnum d){
		assert d != null;
		Optional<Coordinates> location = d.increment().apply(this);
		return location.flatMap(l -> Optional.of(board.squareAt(l)));
	}
	@Override
	public String toString(){
		Optional<String> result = piece.flatMap(p -> Optional.of(p.toString()));
		return result.orElse(color == WHITE ? "□" : "■");
	}
}
