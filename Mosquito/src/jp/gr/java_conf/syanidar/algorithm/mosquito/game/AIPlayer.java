package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Analyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.LookAheadAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Setting;

public class AIPlayer<P extends Position<?>, E extends Evaluation<E>, S extends Setting> implements Player<P> {
	private final Analyzer<P, S, E> analyzer;
	private final Optional<LookAheadAnalyzer<P, S, E>> laa;
	private final S setting;
	private final Evaluator<P, E> evaluator;
	
	private Map<String, E> evaluationMap;
	
	public AIPlayer(Analyzer<P, S, E> analyzer, S setting, Evaluator<P, E> evaluator){
		this.analyzer = analyzer;
		this.setting = setting;
		this.evaluator = evaluator;
		
		if(analyzer instanceof LookAheadAnalyzer){
			laa = Optional.of((LookAheadAnalyzer<P, S, E>)analyzer);
		}else{
			laa = Optional.empty();
		}
	}
	@Override
	public Optional<List<String>> play(P position, NoMoveHandler<P> handler) {
		setting.initialize();
		List<? extends Move> moves = position.moves();
		if(moves.size() == 0){
			handler.handle(position);
			return Optional.empty();
		}
		
		evaluationMap = new HashMap<>();
		Optional<List<String>> line = Optional.ofNullable(laa.isPresent() ? new ArrayList<>() : null);
		E best = evaluator.lowerBound();
		Move bestMove = null;
		for(Move move : moves){
			List<String> newLine = laa.isPresent() ? new ArrayList<>() : null;
			move.play();
			E evaluation = laa.isPresent() ? laa.get().evaluate(position, setting, newLine) : analyzer.evaluate(position, setting);
			move.undo();
			
			evaluationMap.put(move.toString(), evaluation);
			if(evaluation.isBetterThan(best)){
				best = evaluation;
				bestMove = move;
				line.ifPresent(l -> {l.clear(); l.add(move.toString()); l.addAll(newLine);});
			}
		}
		bestMove.play();
		return line;
	}
	public Map<String, E> evaluationMap(){
		return evaluationMap;
	}
}