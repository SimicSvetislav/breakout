package breakout;

import breakout.view.BreakoutFrame;
import breakout.view.StartDialog;

public class Application {

	public static void main(String[] args) {
		
		new StartDialog();
		BreakoutFrame frame = BreakoutFrame.getInstance();
		frame.getGamePan().gt.run();
	}

}
