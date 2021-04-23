package breakout.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import breakout.controller.DialogNextAction;
import breakout.controller.MyWindowListenr;
import net.miginfocom.swing.MigLayout;

public class StartDialog extends JDialog {

	private static final long serialVersionUID = -6201131415378233954L;
	
	private JTextField tfSpeed = new JTextField("", 5);
	private JComboBox<String> cb = new JComboBox<String>();

	public StartDialog() {
		
		setTitle("Choose parameters");
		setUndecorated(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setModal(true);
		setResizable(false);
		
		setLayout(new MigLayout("align 50% 50%", "[]push[]", "[][]" + 10 + "[]"));
		
		add(new JLabel("Speed (recommended 0.7 - 1.3): "), "cell 0 0");
		add(tfSpeed, "cell 1 0");
		add(new JLabel("Rotating polygons speed:"), "cell 0 1");
		cb.addItem("Very slow");	
		cb.addItem("Slow");
		cb.addItem("Normal");
		cb.addItem("Fast");
		cb.addItem("Very fast");
		cb.setSelectedIndex(2);
		add(cb, "cell 1 1");
		
		add(new JButton(new DialogNextAction()), "cell 1 2");
		addWindowListener(new MyWindowListenr());
		
		pack();
		setLocationRelativeTo(null);
		
		setVisible(true);
	
	}
	
	public JTextField getTfSpeed() {
		return tfSpeed;
	}
	
	public String getSelectedItem() {
		return (String)cb.getSelectedItem();
	} 
	
}
