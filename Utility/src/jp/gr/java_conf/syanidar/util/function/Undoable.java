package jp.gr.java_conf.syanidar.util.function;
public interface Undoable extends Runnable{
	public void apply();
	public void undo();
	public default Undoable andThen(Undoable u){
		Undoable self = this;
		return new Undoable(){
			@Override
			public void apply() {
				self.apply();
				u.apply();
			}
			@Override
			public void undo() {
				u.undo();
				self.undo();
			}
			@Override
			public String toString(){
				return new StringBuilder(self.toString()).append(u.toString()).toString();
			}
		};
	}
	public default Undoable ifTrueThen(boolean condition, Undoable u){
		if(!condition)return this;
		
		return andThen(u);
	}
	@Override
	default void run() {
		apply();
	}
}
