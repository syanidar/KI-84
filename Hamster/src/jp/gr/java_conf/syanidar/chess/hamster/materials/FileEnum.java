package jp.gr.java_conf.syanidar.chess.hamster.materials;

public enum FileEnum {
	A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7);
	
	private int column;
	private FileEnum(int column){
		this.column = column;
	}
	boolean involves(Coordinates c){
		return c.column() == column;
	}
}
