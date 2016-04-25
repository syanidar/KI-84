package jp.gr.java_conf.syanidar.utils.function;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class FunctionUtility {
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
}
