package breakout.controller;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import breakout.view.BreakoutFrame;
import breakout.view.StartDialog;

public class DialogNextAction extends AbstractAction {

	private static final long serialVersionUID = 9222620180691289007L;

	public DialogNextAction() {
		putValue(NAME, "Next");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		JButton b = (JButton) o;
		Container c = new Container();
		c = b;
		while (!(c instanceof StartDialog )) {
			c = c.getParent();
		}
		
		StartDialog sd = (StartDialog) c;
		
		String text = sd.getTfSpeed().getText();
		
		if (isNumeric(text)) {
			String str = sd.getSelectedItem();
			
			switch(str) {
				case "Very slow":
					BreakoutFrame.getInstance().getGamePan().setValues(36);
					break;
				case "Slow":
					BreakoutFrame.getInstance().getGamePan().setValues(30);
					break;
				case "Normal":
					BreakoutFrame.getInstance().getGamePan().setValues(24);
					break;
				case "Fast":
					BreakoutFrame.getInstance().getGamePan().setValues(18);
					break;
				case "Very fast":
					BreakoutFrame.getInstance().getGamePan().setValues(12);
					break;
			}
			sd.dispose();
		} else {
			JOptionPane.showMessageDialog(sd, "Not a valid speed");
		}
	}
	
	private static boolean isNumeric(String str) { 
		double d;
		try {  
			d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe) {  
			return false;  
		} 
	  
		BreakoutFrame.getInstance().getGamePan().setSpeed(d);
		
		return true;  
	}
}
