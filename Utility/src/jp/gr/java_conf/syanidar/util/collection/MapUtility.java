package jp.gr.java_conf.syanidar.util.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MapUtility {
	private MapUtility(){}
	
	public static final <K, V extends Comparable<? super V>> Map<K, V> sortByValues(Map<K, V> map){
		List<K> keys = new ArrayList<>(map.keySet());
		List<V> values = new ArrayList<>(map.values());
		ListUtility.sort(values, keys);
		Map<K, V> result = new LinkedHashMap<>();
		for(int i : Range.of(keys.size())){
			result.put(keys.get(i), values.get(i));
		}
		return result;
	}
}
