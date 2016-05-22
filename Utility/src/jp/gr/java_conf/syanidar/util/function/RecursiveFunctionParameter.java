package jp.gr.java_conf.syanidar.util.function;

public interface RecursiveFunctionParameter<T extends RecursiveFunctionParameter<T>>{
	public T next();
}
