package jp.gr.java_conf.syanidar.chess.hamster.materials;

public enum ColorEnum {
	WHITE{
		@Override
		public ColorEnum opposite() {return BLACK;}
		
	},BLACK{
		@Override
		public ColorEnum opposite() {return WHITE;}
	};
	public abstract ColorEnum opposite();
}
