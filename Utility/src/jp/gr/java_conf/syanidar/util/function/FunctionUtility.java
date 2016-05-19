package jp.gr.java_conf.syanidar.util.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class FunctionUtility {
	private FunctionUtility(){}
	
	public static final <T> void iterate(T input, Consumer<T> c, int times){
		for(int i = 0; i < times; i++){
			c.accept(input);
		}
	}
	public static final <T> T iterate(T input, UnaryOperator<T> u, int times){
		for(int i = 0; i < times; i++){
			input = u.apply(input);
		}
		return input;
	}
	public static final <T> Consumer<T> merge(Consumer<T> c, int times){
		Consumer<T> result = c;
		for(int i = 0, n = times - 1; i < n; i++){
			result = result.andThen(c);
		}
		return result;
	}
	public static final <T> UnaryOperator<T> merge(UnaryOperator<T> u, int times){
		Function<T, T> func = u;
		for(int i = 0, n = times - 1; i < n; i++){
			func = func.andThen(u);
		}
		Function<T, T> result = func;
		return t -> result.apply(t);
	}
}
