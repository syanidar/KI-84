package jp.gr.java_conf.syanidar.chess.hamster.materials;

import java.util.Optional;

public class Square{
	private final Coordinates coordinates;
	private final ColorEnum color;
	private final Board board;
	
	private Optional<Piece> piece;
	
	Square(Coordinates co, ColorEnum c, Board b) {
		assert co != null;
		assert c != null;
		assert b != null;
		coordinates = co;
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
	public Coordinates coordinates(){return coordinates;}
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
		Optional<Coordinates> location = d.increment(coordinates);
		return location.map(l -> board.squareAt(l));
	}
	public boolean isAt(Coordinates c){
		return coordinates.equals(c);
	}
	@Override
	public String toString(){
		Optional<String> result = piece.flatMap(p -> Optional.of(p.toString()));
		return result.orElse(".");
	}
	public String toIcon(){
		Optional<String> result = piece.map(p -> p.toIcon());
		return result.orElse(color == ColorEnum.WHITE ? "□" : "■");
	}
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Square))throw new IllegalArgumentException();
		return super.equals(o);
	}
}
