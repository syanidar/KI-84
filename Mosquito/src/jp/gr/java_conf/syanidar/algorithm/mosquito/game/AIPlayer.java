package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Analyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.LookAheadAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Setting;

public class AIPlayer<P extends Position<?>, E extends Evaluation<E>, S extends Setting> implements Player<P> {
	private final Analyzer<P, S, E> analyzer;
	private final Optional<LookAheadAnalyzer<P, S, E>> laa;
	private final S setting;
	private final Evaluator<P, E> evaluator;
	
	private Map<E, String> evaluationMap;
	
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
		
		evaluationMap = new TreeMap<>();
		Optional<List<String>> line = Optional.ofNullable(laa.isPresent() ? new ArrayList<>() : null);
		E best = evaluator.lowerBound();
		Move bestMove = null;
		for(Move move : moves){
			List<String> newLine = laa.isPresent() ? new ArrayList<>() : null;
			move.play();
			E evaluation = laa.isPresent() ? laa.get().evaluate(position, setting, newLine) : analyzer.evaluate(position, setting);
			move.undo();
			
			evaluationMap.put(evaluation, move.toString());
			if(evaluation.isBetterThan(best)){
				best = evaluation;
				bestMove = move;
				line.ifPresent(l -> {l.clear(); l.add(move.toString()); l.addAll(newLine);});
			}
		}
		bestMove.play();
		return line;
	}
	public Map<E, String> evaluationMap(){
		return evaluationMap;
	}
}
