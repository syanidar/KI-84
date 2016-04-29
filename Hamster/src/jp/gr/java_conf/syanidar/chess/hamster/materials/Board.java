package jp.gr.java_conf.syanidar.chess.hamster.materials;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public class Board{
	private final Square[] squares;

	public Board(){
		squares = new Square[64];
		for(int column = 0; column < 8; column++){
			for(int row = 0; row < 8; row++){
				squares[column * 8 + row] = new Square(column, row, (column + row) % 2 == 1 ? WHITE : BLACK, this);
			}
		}
		squares[0].put(new Rook(WHITE));
		squares[1].put(new Pawn(WHITE));
		squares[8].put(new Knight(WHITE));
		squares[9].put(new Pawn(WHITE));
		squares[16].put(new Bishop(WHITE));
		squares[17].put(new Pawn(WHITE));
		squares[24].put(new Queen(WHITE));
		squares[25].put(new Pawn(WHITE));
		squares[32].put(new King(WHITE));
		squares[33].put(new Pawn(WHITE));
		squares[40].put(new Bishop(WHITE));
		squares[41].put(new Pawn(WHITE));
		squares[48].put(new Knight(WHITE));
		squares[49].put(new Pawn(WHITE));
		squares[56].put(new Rook(WHITE));
		squares[57].put(new Pawn(WHITE));
		
		squares[7].put(new Rook(BLACK));
		squares[6].put(new Pawn(BLACK));
		squares[15].put(new Knight(BLACK));
		squares[14].put(new Pawn(BLACK));
		squares[23].put(new Bishop(BLACK));
		squares[22].put(new Pawn(BLACK));
		squares[31].put(new Queen(BLACK));
		squares[30].put(new Pawn(BLACK));
		squares[39].put(new King(BLACK));
		squares[38].put(new Pawn(BLACK));
		squares[47].put(new Bishop(BLACK));
		squares[46].put(new Pawn(BLACK));
		squares[55].put(new Knight(BLACK));
		squares[54].put(new Pawn(BLACK));
		squares[63].put(new Rook(BLACK));
		squares[62].put(new Pawn(BLACK));
	}
	public Board(String fen){
		squares = new Square[64];
		for(int column = 0; column < 8; column++){
			for(int row = 0; row < 8; row++){
				squares[column * 8 + row] = new Square(column, row, (column + row) % 2 == 1 ? WHITE : BLACK, this);
			}
		}
		int currentFenIndex = 0;
		IntUnaryOperator conv = i -> {
			int column = i % 8;
			int row =  7 - (i - column) / 8;
			return column * 8 + row;
		};
		char[] chars = fen.toCharArray();
		for(char c : chars){
			switch(c){
			case 'K':
				squares[conv.applyAsInt(currentFenIndex)].put(new King(WHITE));
				currentFenIndex++;
				break;
			case 'Q':
				squares[conv.applyAsInt(currentFenIndex)].put(new Queen(WHITE));
				currentFenIndex++;
				break;
			case 'R':
				squares[conv.applyAsInt(currentFenIndex)].put(new Rook(WHITE));
				currentFenIndex++;
				break;
			case 'B':
				squares[conv.applyAsInt(currentFenIndex)].put(new Bishop(WHITE));
				currentFenIndex++;
				break;
			case 'N':
				squares[conv.applyAsInt(currentFenIndex)].put(new Knight(WHITE));
				currentFenIndex++;
				break;
			case 'P':
				squares[conv.applyAsInt(currentFenIndex)].put(new Pawn(WHITE));
				currentFenIndex++;
				break;
			case 'k':
				squares[conv.applyAsInt(currentFenIndex)].put(new King(BLACK));
				currentFenIndex++;
				break;
			case 'q':
				squares[conv.applyAsInt(currentFenIndex)].put(new Queen(BLACK));
				currentFenIndex++;
				break;
			case 'r':
				squares[conv.applyAsInt(currentFenIndex)].put(new Rook(BLACK));
				currentFenIndex++;
				break;
			case 'b':
				squares[conv.applyAsInt(currentFenIndex)].put(new Bishop(BLACK));
				currentFenIndex++;
				break;
			case 'n':
				squares[conv.applyAsInt(currentFenIndex)].put(new Knight(BLACK));
				currentFenIndex++;
				break;
			case 'p':
				squares[conv.applyAsInt(currentFenIndex)].put(new Pawn(BLACK));
				currentFenIndex++;
				break;
			case '/':
				break;
			default:
				if(Character.isDigit(c)){
					currentFenIndex += c - '0';
				}else throw new IllegalArgumentException(fen);
			}
		}
	}
	public List<Square> squaresMatch(Predicate<Square> p){
		assert p != null;
		List<Square> result = new ArrayList<>();
		for(Square square : squares){
			if(p.test(square)){
				result.add(square);
			}
		}
		return result;
	}
	public Optional<Square> squareMatchs(Predicate<Square> p){
		assert p != null;
		for(Square square : squares){
			if(p.test(square)){
				return Optional.of(square);
			}
		}
		return Optional.empty();
	}
	public Square squareAt(Coordinates c){
		assert c != null;
		return squares[c.column() * 8 + c.row()];
	}
	public BoardSnapshot snapshot(){
		long wp = 0;
		long wn = 0;
		long wb = 0;
		long wr = 0;
		long wq = 0;
		long wk = 0;
		long bp = 0;
		long bn = 0;
		long bb = 0;
		long br = 0;
		long bq = 0;
		long bk = 0;
		for(int i = 0; i < 64; i++){
			Square s = squares[i];
			if(s.isOccupied()){
				Piece p = s.piece().get();
				switch(p.toEnum()){
				case PAWN:
					if(p.color() == WHITE){
						wp |= 1 << i;
					}else{
						bp |= 1 << i;
					}
					break;
				case KNIGHT:
					if(p.color() == WHITE){
						wn |= 1 << i;
					}else{
						bn |= 1 << i;
					}
					break;
				case BISHIP:
					if(p.color() == WHITE){
						wb |= 1 << i;
					}else{
						bb |= 1 << i;
					}
					break;
				case ROOK:
					if(p.color() == WHITE){
						wr |= 1 << i;
					}else{
						br |= 1 << i;
					}
					break;
				case QUEEN:
					if(p.color() == WHITE){
						wq |= 1 << i;
					}else{
						bq |= 1 << i;
					}
					break;
				case KING:
					if(p.color() == WHITE){
						wk |= 1 << i;
					}else{
						bk |= 1 << i;
					}
					break;
				}
			}
		}
		return new BoardSnapshot(wp, wn, wb, wr, wq, wk, bp, bn, bb, br, bq, bk);
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int row = 7; row >= 0; row--){
			for(int column = 0; column < 8; column++){
				sb.append(squares[column * 8 + row]);
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	public String toIcon(){
		StringBuilder sb = new StringBuilder();
		for(int row = 7; row >= 0; row--){
			for(int column = 0; column < 8; column++){
				sb.append(squares[column * 8 + row].toIcon());
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
