package breakout.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class BreakoutFrame extends JFrame {

	private static final long serialVersionUID = 893794182077144987L;
	
	private static BreakoutFrame instance = null;
	private GamePanel gamePan = new GamePanel();
	
	public GamePanel getGamePan() {
		return gamePan;
	}

	public void setGamePan(GamePanel gamePan) {
		this.gamePan = gamePan;
	}
	
	private BreakoutFrame() {
		initialise();
	}
	
	private void initialise() {
		
		setTitle("Breakout");
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		
		setSize((int)screenSize.getWidth()/2, (int)screenSize.getHeight()*10/11);
		
		setResizable(false);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		
		setLocationRelativeTo(null);
		
		gamePan.setPreferredSize(new Dimension(getSize().width*3/4, getSize().height));
		
		add(gamePan, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		//ImageIcon ic = new ImageIcon(getClass().getResource("breakout_logo.png")); 
		//setIconImage(ic.getImage());
		
		setAlwaysOnTop(true);
	
		// Sakrivanje pokazivaca misa
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		
		// Pravljenje prikaza misa koji zapravo pokazuje nista
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		
		// Postavljanje takvog prikaza misa
		getContentPane().setCursor(blankCursor);
		
		setVisible(true);
		
	}
	
	public static BreakoutFrame getInstance() {
		
		if (instance==null) {
			instance = new BreakoutFrame();
		}
		
		return instance;
	} 
	

}
