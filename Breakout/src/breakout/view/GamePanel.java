package breakout.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import breakout.model.Brick;
import breakout.model.DoublePoint;
import breakout.model.Vector2D;

public class GamePanel extends JPanel implements KeyListener, MouseMotionListener, Runnable {

	private static final long serialVersionUID = -817882488485208527L;

	public enum State {
		START, GAME, END, RESTART
	}

	// Debljina ivica
	private int borderThickness;
	
	// Trenutno stanje
	private State state = State.START;

	// Ugaona brzina
	private double rotationSpeed;
	
	// Ugao rotacije u jednom intervalu za poligone koji rotiraju
	private double rotationAngle;
	
	// Ivice
	private Brick edge[] = new Brick[3];
	
	// Trenutna pozicija loptice
	private DoublePoint ballPos = new DoublePoint();
	
	// Brzina loptice
	private Vector2D ballVelocity = new Vector2D();
	// Intezitet vektora brzine
	private double magnitude = 0.8;
	
	// Brzina kao parametar koji zadaje korisnik
	private double speed;
	
	// Precnik loptice
	private double ballDiameter;
	
	private int arc;
	
	// Trenutno aktivnih, neunistenih blokova
	private int activeBricks = 1;

	private double fpoints = 1.0 / 180;

	// Interval u milisekundama
	// private long interval = (long) Math.floor(1000 * fpoints);
	private long interval = (long) Math.floor(500 * fpoints);

	// Loptica
	private Rectangle ball;
	
	// Palica
	//private Rectangle pad = new Rectangle(0, 0, 0, 0);
	private Brick padBrick;

	// Teksture
	private BufferedImage ballTexture = null;
	private BufferedImage padTexture = null;
	private BufferedImage metalTexture = null;
	
	private boolean collision;
	//private double colRelCenter;

	int padWidth;
	int padHeight;
	
	//private Timer timer;
	public Thread gt;

	private int col = 5;
	private int row = 13;
	
	// Promenljive za mozaik
	private Mosaic mosaic;
	public Brick map[][];

	public GamePanel() {
		setFocusTraversalKeysEnabled(false);
		setFocusable(true);
		addKeyListener(this);
		addMouseMotionListener(this);

		// Tekstura za lopticu
		try {
			ballTexture = ImageIO.read(getClass().getResourceAsStream("ball.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Tekstura za palicu
		try {
			padTexture = ImageIO.read(getClass().getResourceAsStream("pad.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Tekstura za fiksne prepreke
		try {
			metalTexture = ImageIO.read(getClass().getResourceAsStream("metalTexture.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		gt = new Thread(this);
	}

	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g.create();
		String message = null;
		
		if (state == State.START || state == State.RESTART) {
			
			map = new Brick[row][col];
			mosaic = new Mosaic(row, col, map, this);
		
			borderThickness = getSize().height / 200;
			
			// Modelovanje ivica
			
			edge[0] = new Brick(4, 0, -1);
			edge[1] = new Brick(4, 0, -1);
			edge[2] = new Brick(4, 0, -1);
			
			Rectangle rec = null;
			for (int a = 1; a <= 3; a++) {
				switch (a) {
				case 1:
					rec = new Rectangle(0, 0, getSize().width, borderThickness);
					break;
				case 2:
					rec = new Rectangle(0, 0, borderThickness, getSize().height);
					break;
				
				case 3:
					rec = new Rectangle((int) getSize().getWidth() - borderThickness, 0, getSize().width, getSize().height);
					break;

				}
				edge[a-1].vertices[0] = new DoublePoint(rec.x, rec.y);
				edge[a-1].vertices[1] = new DoublePoint(rec.x + rec.width, rec.y);
				edge[a-1].vertices[2] = new DoublePoint(rec.x + rec.width, rec.y + rec.height);
				edge[a-1].vertices[3] = new DoublePoint(rec.x, rec.y + rec.height);
			}
			
			arc = getSize().height / 50;

			padWidth = (int) Math.ceil(getSize().width * 2 / 15);
			padHeight = (int) Math.ceil(getSize().height / 64);

			int paddlePositionX = (int) Math.round(getSize().getWidth() / 2) - padWidth / 2;
			int paddlePositionY = getSize().height - padHeight * 2;
		
			// Modelovanje palice
			padBrick = new Brick(4, 0, -2);
			padBrick.vertices[0] = new DoublePoint(paddlePositionX, paddlePositionY);
			padBrick.vertices[1] = new DoublePoint(paddlePositionX + padWidth, paddlePositionY);
			padBrick.vertices[2] = new DoublePoint(paddlePositionX + padWidth, paddlePositionY + padHeight);
			padBrick.vertices[3] = new DoublePoint(paddlePositionX, paddlePositionY + padHeight);
			padBrick.polCenter = new DoublePoint(paddlePositionX + padWidth/2, paddlePositionY + padHeight);
			
			//pad = new Rectangle((int)paddlePositionX, (int)paddlePositionY, padWidth, padHeight);
		
			
			// Pocetna pozicija loptice
			ballPos.x = getSize().getWidth() * 3 / 5;
			ballPos.y = getSize().getHeight() * 4 / 5;

			// Inicijalne brzine po osama
			ballVelocity.x = speed * (0);
			ballVelocity.y = speed * (-0.8);
			ballVelocity = ballVelocity.setLength(speed*magnitude);

			ballDiameter = (int) Math.ceil(getSize().height / 40);
			
			mosaic.draw(g2d, map, state); 
			
			if (state == State.RESTART) {
				System.out.println("GAME");
				state = State.GAME;
			}
		}
		
		// Pozadina
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, getSize().width, getSize().height);

		// Ivice, sve sem donje
		g2d.setColor(Color.RED);
		for (Brick b : edge) {
			Rectangle r = new Rectangle ((int)b.vertices[0].getX(), (int) b.vertices[0].getY(), (int) (b.vertices[1].getX()-b.vertices[0].getX()), (int) (b.vertices[3].getY()-b.vertices[0].getY()));
			b.polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);		
			g2d.fillRect(r.x, r.y, r.width, r.height);
		}
		
		// Mozaik
		activeBricks = mosaic.draw(g2d, map, state);
		TexturePaint tp = new TexturePaint(ballTexture,
		new Rectangle((int) Math.round(ballPos.x), (int) Math.round(ballPos.y), (int) ballDiameter, (int) ballDiameter));

		// Crtanje loptice
		g2d.setPaint(tp);
		g2d.fillOval((int) Math.round(ballPos.x), (int) Math.round(ballPos.y), (int) ballDiameter, (int) ballDiameter);
		
		// Crtanje palice
		padBrick.vertices[1].x = padBrick.vertices[0].x + padWidth;
		padBrick.vertices[2].x = padBrick.vertices[0].x + padWidth;
		padBrick.vertices[3].x = padBrick.vertices[0].x;
		padBrick.polCenter = new DoublePoint(padBrick.vertices[0].x + padWidth/2, padBrick.vertices[0].y + padHeight*2);
		tp = new TexturePaint(padTexture, new Rectangle((int)padBrick.vertices[0].x, (int)padBrick.vertices[0].y, (int)(padBrick.vertices[1].x - padBrick.vertices[0].x), (int)(padBrick.vertices[3].y - padBrick.vertices[0].y)));
		g2d.setPaint(tp);
		g2d.fillRoundRect((int)padBrick.vertices[0].x, (int)padBrick.vertices[0].y, (int)(padBrick.vertices[1].x - padBrick.vertices[0].x), (int)(padBrick.vertices[3].y - padBrick.vertices[0].y), arc, arc);
		
		// Ispisivanje poruke po potrebi
		Font font = new Font("Sans Serif", Font.BOLD, 30);
		g2d.setFont(font);
		g2d.setColor(Color.GREEN.darker());
		switch (state) {
			case START:
				message = "Press Enter to start";
				drawCenteredString(g2d, message, new Rectangle(0, 0, getSize().width, getSize().height), font);
				break;
			case END:
				message = "Press Enter to restart or press Esc to exit";
				drawCenteredString(g2d, message, new Rectangle(0, 0, getSize().width, getSize().height), font);
				break;
			case GAME:
			case RESTART:
			default:
				break;
		}
	}
	
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	   
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + (int)getSize().getHeight()*1/4;
	    
	    g.setFont(font);
	   
	    g.drawString(text, x, y);
	}

	private void action() {
		if (state == State.GAME) {
			// Provera sudara sa palicom
			detectCollision(padBrick);

			// Provera sudara sa blokovima
			OuterLoop: for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j].active > 0) {
						if (detectCollision(map[i][j]) && !collision) {
							map[i][j].active--;
							// Sudari su diskretni, nece se dogoditi dva sudara u istom trenutku
							break OuterLoop;
						}
					} else if (map[i][j].active==-1) {
						if (detectCollision(map[i][j])) {
							// Sudari su diskretni, nece se dogoditi dva sudara u istom trenutku
							break OuterLoop;
						}
					}	
				}
			}

			// Provera sudara sa ivicama
			for (Brick b : edge) {
				if (detectCollision(b)) {
					break;
				}
			}
			
			// Promena pozicije loptice
			ballPos.x += ballVelocity.x * interval;
			ballPos.y += ballVelocity.y * interval;
		}
		
		ball = new Rectangle((int) ballPos.x, (int) ballPos.y, (int) ballDiameter, (int) ballDiameter);
		
		if (ballPos.y > 950 && state == State.GAME) {
			state = State.END;
		}
	}

	// Za detekciju sudara koriscena je SAT (Separating Axis Theorem) metoda
	private boolean detectCollision(Brick brick) {
		if (brick.size > 0) {
			Vector2D points[] = new Vector2D[brick.size + 3];
	
			for (int l = 0; l < brick.size; l++) {
				points[l] = brick.vertices[l];
			}
	
			// Ose
			Vector2D axis[] = new Vector2D[brick.size + 2];
	
			// Ose za oblik poligona
			int k;
			for (k = 0; k < brick.size; k++) {
				axis[k] = new Vector2D(points[k].diff(points[(k + 1) % brick.size])).getNormal();
			}
			
			// Tacke kojima se aproksimira loptica
			// Uzete su tri tacke, zato sto su po dve normale jednake za kvadrat kojim je aproksimirana loptica
			// To znaci da je dovoljno ispitivati samo dve normale, a za to je potrebno tri tacke da bi se odredilo
			points[brick.size + 0] = new Vector2D(ball.x, ball.y);
			points[brick.size + 1] = new Vector2D(ball.x + ball.width, ball.y);
			points[brick.size + 2] = new Vector2D(ball.x + ball.width, ball.y + ball.height);
			
			// Dve normale na kvadrat kojim je aproksimirana loptica
			for (k = brick.size; k < brick.size + 2; k++) {
				axis[k] = new Vector2D(points[k].diff(points[k+1])).getNormal();
			}
	
			// Provera preklapanja bloka i loptice po osama
			// Petlja za ose
			for (k = 0; k < brick.size + 2; k++) {
				// Petlja za tacke poligona
				double min1 = Double.POSITIVE_INFINITY;
				double max1 = Double.NEGATIVE_INFINITY;
				for (int m = 0; m < brick.size; m++) {
					// Racunanje skalarnog proizvoda
					double temp = points[m].dotProduct(axis[k]);

					if (temp > max1)
						max1 = temp;
	
					if (temp < min1)
						min1 = temp;
				}
	
				// Petlja za tacke loptice
				double min2 = Double.POSITIVE_INFINITY;
				double max2 = Double.NEGATIVE_INFINITY;
				for (int m = brick.size; m < brick.size + 3; m++) {
					// Racunanje skalarnog proizvoda
					double temp = points[m].dotProduct(axis[k]);
	
					if (temp > max2)
						max2 = temp;
					
					if (temp < min2)
						min2 = temp;
				}
	
				if (max1 < min2 || max2 < min1) {
					// Nema sudara, dovoljno je da po jednoj osi nema preklapanja
					return false;
				}
			}
		} else {
			// Detekcija sudara sa fiksnim preprekama kruznog oblika
			double distance = Math.sqrt(Math.pow(ballPos.x + ballDiameter/2 - brick.polCenter.getX(), 2.0) + Math.pow(ballPos.y + ballDiameter/2 - brick.polCenter.getY(), 2.0));
			
			if (distance > ballDiameter/2 + mosaic.getBrickWidth()/2) {
				return false;
			} else {
				DoublePoint ballCenter = new DoublePoint(ball.x+ballDiameter/2, ball.y+ballDiameter/2);
				
				// Upad
				double pen = -distance + ballDiameter/2 + mosaic.getBrickWidth()/2;
				
				// Otklon
				// Pomera se samo loptica za ceo upad u smeru vektora od centra prepreke 
				// ka centru loptice jer je u pitanju nepokretna prepreka	
				Vector2D def = ballCenter.diff(brick.polCenter);
				def = def.setLength(pen);
				ballPos = ballPos.addP(def);
				
				// Azuriranje centra loptice nakon pomeranja
				ballCenter = new DoublePoint(ball.x+ballDiameter/2, ball.y+ballDiameter/2);
				
				// Pri sudaru sa nepokretnom kruznom preprekom vektor loptice nakon odbijanja 
				// se poklapa sa vektorom koji polazi od centra te prepreke, a zavrsava se u centru loptice  
				ballVelocity = ballCenter.diff(brick.polCenter);
				
				// Podesavanje inteziteta vektora je neophodno
				ballVelocity = ballVelocity.setLength(magnitude*speed);
			}
		}
		
		// Centar loptice
		DoublePoint ballCenter = new DoublePoint(ball.x+ballDiameter/2, ball.y+ballDiameter/2);
		
		// Trazenje najblize ivice loptici u obliku prave date formulom ax+by+c=0, sa kojom je doslo do sudara
		if (brick.size>0) {
			int minVer = 0;
			double minDis = Double.POSITIVE_INFINITY;
			for (int z = 0; z < brick.size; z++) {	
				
				
				int next = z + 1;
				if (next==brick.size){
					next = 0;
				}
				
				// Nagib prave
				double k = (brick.vertices[next].y-brick.vertices[z].y)/(brick.vertices[next].x-brick.vertices[z].x);
				
				// Koeficijenti a,b,c
				double a, b, c;
				if (brick.vertices[next].x!=brick.vertices[z].x) {
					a = k;
					b = -1;
					c = -brick.vertices[z].x * k + brick.vertices[z].y;
				} else {
					a = -1;
					b = 0;
					c = brick.vertices[next].x;
				}
				
				double distance = Math.abs(a*ballCenter.x + b*ballCenter.y + c) / (a*a + b*b);
				
				// Uzima se ona prava cije je rastojanje od centra loptice najblize velicini poluprecnika
				// posto se tu desava kontakt
				if (Math.abs(distance-ballDiameter/2) < Math.abs(minDis-ballDiameter/2)) {
					minVer = z;
					minDis = distance;
				}
			}
			
			// Upad
			double pen = ballDiameter / 2 - minDis;
			
			// Pronadjena najbliza ivica, minVer sadrzi indeks pocetne tacke za tu ivicu,
			// a (minVer+1)%brick.size drugu
			
			if (pen<0 && brick.active>0) return false;
			
			// Racunanje vektora ivice sa kojom se loptica sudarila
			Vector2D vertex = brick.vertices[(minVer+1)%brick.size].diff(brick.vertices[minVer]);
			
			// Trazenje normale na ivicu
			// Dobija se normala ka unutrasnjosti, zato sto su ivice svih poligona poredjane tako 
			// da obrazuju ciklus u smeru kazaljke na satu kada vektor ide od tacke sa indeksom i 
			// ka tacki sa indeksom i+1, sa izuzetkom poslednje tacke koja ide ka prvoj, ali i dalje postuje smer
			Vector2D norm = vertex.getNormal();
			
			// Pravljenje otklona za blokove
			if (brick.active > 0) {
				// intezitet normale se postavlja na polovinu upada da bi mogao da se uradi otklon
				Vector2D defV1 = new Vector2D(norm.setLength(pen/2));
				
				// Pomeraju se centar poligona i sva temena za polovinu otklona
				brick.polCenter = brick.polCenter.addP(defV1);
				for (int p = 0; p < brick.size; p++) {
					brick.vertices[p] = brick.vertices[p].addP(defV1);
				}
				
				// Za lopticu se uzima dijagonala koja pokazuje ka spoljasnjosti poligona
				Vector2D norm2 = new Vector2D(-norm.x, -norm.y);
				
				// intezitet normale se postavlja na polovinu upada da bi mogao da se uradi otklon
				Vector2D defV2 = norm2.setLength(pen/2);
				
				// Pomera se loptica za polovinu upada
				ballPos = ballPos.addP(defV2);
				
			} else {
				// Pravljenje otklona za ivice i palicu
				
				// Za lopticu se uzima dijagonala koja pokazuje ka spoljasnjosti poligona
				Vector2D norm2 = new Vector2D(-norm.x, -norm.y);
				
				// intezitet normale se postavlja na velicinu upada da bi mogao da se uradi otklon
				Vector2D defV = norm2.setLength(pen);
				
				// Pomera se loptica za velicinu upada
				ballPos = ballPos.addP(defV);
				
			}
			
			// Azuriranje centra loptice nakon pomeranja
			ballCenter = new DoublePoint(ball.x+ballDiameter/2, ball.y+ballDiameter/2);
			
			// Normalizovanje normale, potrebno je zbog formule
			norm = norm.normalize();
			
			if (brick.active!=-2) { 
				// Formula za racunanje brzine r=d-2*(d*n)*n, gde je:
				// r - novi vektor brzine loptice
				// d - stari vektor brzine loptice
				// n - normalizovana normala ivice sa kojom se sudarila loptica
				ballVelocity = ballVelocity.diff(norm.scalarMul(2*ballVelocity.dotProduct(norm)));
			} else {
				// Ako je odbijanje od palice
				
				// Vektor od dna sredine palice do centra loptice
				Vector2D h = ballCenter.diff(brick.polCenter);
				h = h.setLength(speed*magnitude);
				ballVelocity = h;
			}
		}
		
		return true;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:

			if (state == State.START) {
				state = State.GAME;
				break;
			}

			if (state == State.END) {
				state = State.START;
				break;
			}
			break;

		case KeyEvent.VK_ESCAPE:
			if (state == State.END) {
				BreakoutFrame.getInstance().setVisible(false);
				System.exit(0);
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			repaint();
			action();
			
			if (activeBricks==0 && state == State.GAME) {
				activeBricks = 1;
				state = State.END;
			}
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Radno cekanje umesto uspavljivanja niti
			// long now = System.currentTimeMillis();
			// while (now + interval > System.currentTimeMillis()) {}
		}
	}
	
	// Implementacija slusaca pokreta misa
	@Override
	public void mouseDragged(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int temp = arg0.getX();
		//if (temp - pad.width / 2 >= borderThickness && temp + pad.width / 2 <= getSize().width - borderThickness && state == State.GAME)
		if ((padBrick.vertices[1].x - padBrick.vertices[0].x)/ 2 >= borderThickness && temp + (padBrick.vertices[1].x - padBrick.vertices[0].x) <= getSize().width - borderThickness && state == State.GAME) {	
			padBrick.vertices[0].x = arg0.getX() - (padBrick.vertices[1].x - padBrick.vertices[1].x) / 2;
			//pad.x = arg0.getX() - pad.width / 2;
		}
	}
	
	// Pomocne metode 
	public State getState () {
		return state;
	}

	public void setSpeed(double d) {
		speed = d;
	}
	
	public double getRotationSpeed () {
		return rotationSpeed;
	}
	
	public double getRotationAngle () {
		return rotationAngle;
	}
	
	public BufferedImage getMetalTexture() {
		return metalTexture;
	}

	public void setValues(int i) {
		rotationSpeed = 2*Math.PI/i;
		rotationAngle = rotationSpeed * interval;
	}

}
