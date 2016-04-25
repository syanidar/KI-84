package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.Optional;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
import jp.gr.java_conf.syanidar.chess.hamster.materials.PieceEnum;

public class MoveNotation {
	private final Piece piece;
	private final Optional<Piece> target;
	private final Optional<Piece> promotee;
	private final Optional<Piece> rook;
	private final Coordinates origin;
	private final Coordinates destination;
	
	static final class MoveNotationBuilder{
		private final Piece piece;
		private final Coordinates origin;
		private final Coordinates destination;
		private Piece target;
		private Piece promotee;
		private Piece rook;
		
		MoveNotationBuilder(Piece p, Coordinates o, Coordinates d){
			piece = p;
			origin = o;
			destination = d;
		}
		MoveNotationBuilder target(Piece t){
			target = t;
			return this;
		}
		MoveNotationBuilder promotee(Piece p){
			promotee = p;
			return this;
		}
		MoveNotationBuilder rook(Piece r){
			rook = r;
			return this;
		}
		MoveNotation build(){
			return new MoveNotation(this);
		}
	}
	private MoveNotation(MoveNotationBuilder builder){
		piece = builder.piece;
		origin = builder.origin;
		destination = builder.destination;
		target = Optional.ofNullable(builder.target);
		promotee = Optional.ofNullable(builder.promotee);
		rook = Optional.ofNullable(builder.rook);
	}
	public String toPureCoordinateNotation(){
		StringBuilder sb = new StringBuilder();
		sb.append(origin.toAlgebraicNotation());
		sb.append("-");
		sb.append(destination.toAlgebraicNotation());
		promotee.ifPresent(p -> sb.append(p.toAlgebraicNotation()));
		return sb.toString();
	}
	
	boolean hasMoved(Piece p){
		return piece == p;
	}
	boolean isInitialPawnPush(){
		if(!piece.isEqualTo(PieceEnum.PAWN))return false;
		int diff = origin.row() - destination.row();
		return diff == -2 || diff == 2;
	}
	
	boolean isTargetPresent(){
		return target.isPresent();
	}
	boolean hasMoved(PieceEnum p){
		return piece.isEqualTo(p);
	}
}
