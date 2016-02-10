package jp.gr.java_conf.syanidar.reversi.reviser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jp.gr.java_conf.syanidar.reversi.DataBasePlayer;
import jp.gr.java_conf.syanidar.reversi.DataBaseReviser;
import jp.gr.java_conf.syanidar.reversi.GameKeeper;
import jp.gr.java_conf.syanidar.reversi.InvisibleViewer;
import jp.gr.java_conf.syanidar.reversi.MobilityEvaluator;
import jp.gr.java_conf.syanidar.reversi.Viewer;

public class RecordReviser {

	public static final void main(String[] args)throws IOException, InterruptedException{
		BufferedReader reader = null;
		int depthMiddle = 0;
		int depthEnd = 0;
		int minTurnRevised = 0;
		try {
			depthMiddle = Integer.parseInt(args[0]);
			depthEnd = Integer.parseInt(args[1]);
			minTurnRevised = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			throw new RuntimeException();
		}
		final BufferedWriter writer = new BufferedWriter(new FileWriter("records" + File.separator + "revised.txt"));;
		try {
			int idCurrentGame = 0;
			reader = new BufferedReader(new FileReader("records" + File.separator + "base.txt"));
			for(String line = DataBasePlayer.readNextValidLine(reader); line != null; line = DataBasePlayer.readNextValidLine(reader)){
				System.out.println("Revising " + (++idCurrentGame) + "th game.");
				Viewer viewer = new InvisibleViewer();
				DataBaseReviser reviser = new DataBaseReviser(viewer, MobilityEvaluator.INSTANCE, depthMiddle, depthEnd, line, minTurnRevised);
				GameKeeper keeper = new GameKeeper(reviser, reviser);
				try {
					keeper.excute();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new AssertionError();
				}
				String revisedGame = reviser.generateGameRecord();
				try {
					writer.write(revisedGame);
					writer.write(System.lineSeparator());
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Could't find \"base.txt\" in \"record\" folder");
		}finally{
			reader.close();
			writer.close();
		}
	}
}
