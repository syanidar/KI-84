package jp.gr.java_conf.syanidar.util.function;

import java.util.function.Function;

public interface RecursiveFunction<T extends RecursiveFunctionParameter<T>, R> extends Function<T, R>{
	@Override
	default R apply(T param) {
		if(returnsValue(param))return value(param);
		return apply(param.next());
	}
	public boolean returnsValue(T param);
	public R value(T param);
}
