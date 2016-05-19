package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
import jp.gr.java_conf.syanidar.chess.hamster.materials.PieceEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;
import jp.gr.java_conf.syanidar.chess.hamster.tools.AttackDetector;

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
	public String toAlgebraicNotation(Board board, boolean isACheck, boolean isACheckmate){
		AttackDetector ad = AttackDetector.getInstance(board);
		String notation = null;
		if (!rook.isPresent()) {
			StringBuilder sb = new StringBuilder();
			sb.append(piece.isEqualTo(PieceEnum.PAWN) ? target.isPresent() ? origin.file() : "" : piece.toAlgebraicNotation());
			List<Square> duplicacyCandidates = ad.piecesWhichAttackTheSquareOf(piece.color().opposite(), destination);
			duplicacyCandidates.remove(board.squareAt(origin));
			List<Square> candidates = duplicacyCandidates.stream()
					.filter(s -> !s.piece().get().isEqualTo(PieceEnum.PAWN))
					.filter(s -> s.piece().get().isEqualTo(piece.toEnum())).collect(Collectors.toList());
			boolean needsRank = candidates.stream().filter(c -> piece.isEqualTo(PieceEnum.QUEEN) || piece.isEqualTo(PieceEnum.ROOK)).anyMatch(c -> c.coordinates().isOnFile(origin.file()));
			if(!candidates.isEmpty()){
				if(needsRank){
					sb.append(origin.rank());
				}else{
					sb.append(origin.file());
				}
			}
			target.ifPresent(t -> sb.append("x"));
			sb.append(destination.toAlgebraicNotation());
			promotee.ifPresent(p -> sb.append(p.toAlgebraicNotation()));
			notation = sb.toString();
		}else{
			notation = destination.column() == 2 ? "0-0-0" : "0-0";
		}
		if(isACheckmate)return String.join("", notation, "#");
		if(isACheck)return String.join("", notation, "+");
		
		return notation;
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
