package breakout.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;

import breakout.model.Brick;
import breakout.model.DoublePoint;
import breakout.view.GamePanel.State;

public class Mosaic {
	
	private int col;
	private int row;
	private int brickWidth;
	private int brickHeight;
	private GamePanel gamePan;
	private int activeBricks = 1;
	private int emptySpace = 6;
	
	public Mosaic(int row, int col, Brick[][] map, GamePanel gp) {
		this.row = row;
		this.col = col;
		gamePan = gp;
		brickWidth = (int) Math.round((double)BreakoutFrame.getInstance().getGamePan().getSize().width * 5/7 / col);
		brickHeight = (int) Math.round((double)gamePan.getSize().height *1/2 / row);
		for (int i=0; i<map.length; i++) {
			for (int j=0; j<map[0].length; j++) {
				map[i][j] = new Brick(4, 0, 1);
			}
		}
		
		// Posebni trouglovi za ivice
		//map[0][0].size = 3;
		map[4][0].size = 3;
		//map[0][col-1].size = 3;
		map[4][col-1].size = 3;
		map[row-1][0].size = 3;
		map[row-1][col-1].size = 3;
		
		// Prostor za nesto sto ce se rotirati u sredini
		map[2][1].active = 0;
		map[2][2].active = 0;
		map[2][3].active = 0;
		
		map[3][1].active = 0;
		map[3][2].active = 0;
		map[3][3].active = 0;
		
		map[4][1].active = 0;
		map[4][2].active = 0;
		map[4][3].active = 0;
		
		map[5][1].active = 0;
		map[5][2].active = 0;
		map[5][3].active = 0;
		
		map[6][1].active = 0;
		map[6][2].active = 0;
		map[6][3].active = 0;
		
		map[7][1].active = 0;
		map[7][2].active = 0;
		map[7][3].active = 0;
		
		map[8][1].active = 0;
		map[8][2].active = 0;
		map[8][3].active = 0;
		
		map[9][1].active = 0;
		map[9][2].active = 0;
		map[9][3].active = 0;
		
		// Promene u prva dva reda
		map[0][1].active = 0;
		map[0][2].active = 0;
		map[0][3].active = 0;
		
		map[1][1] = new Brick(5, 0, 2);
		map[1][2] = new Brick(6, 0, 3);
		map[1][3] = new Brick(5, 0, 2);
		
		// Promene u poslednja tri reda
		
		map[row-3][0].size = 3;
		map[row-2][1].size = 3;
		map[row-2][0].active = 0;
		
		
		map[row-3][col-1].size = 3;
		map[row-2][col-2].size = 3;
		map[row-2][col-1].active = 0;
		
		map[row-1][2].size = 3;
		map[row-1][col-2].active = 0;
		map[row-1][col-1].active = 0;
		map[row-1][0].active = 0;
		map[row-1][1].active = 0;
		
		// Poligoni koji se rotiraju
		
		map[4][1] = new Brick(4, 1, 1);
		// Smer suprotan kazaljci na satu (-1)
		map[4][3] = new Brick(4, -1, 1);
		map[4][2] = new Brick(3, 1, 1);
		
		// Sestouglovi koji se rotiraju
		map[8][1] = new Brick(6,-1, 1);
		map[8][3] = new Brick(6, 1, 1);
		
		// Petougao koji se rotira
		map[8][2] = new Brick(5, 1, 1);
		
		// Fiksne prepreke
		map[1][0].active = 0;
		map[2][0].active = 0;
		map[3][0].active = 0;
		
		
		map[0][0] = new Brick(-1, 0, -1);
		
		map[1][col-1].active = 0;
		map[2][col-1].active = 0;
		map[3][col-1].active = 0;
		
		map[0][col-1] = new Brick(-1, 0, -1);
		
		// "Tvrdi" blokovi
		map[6][0].active = 2;
		map[6][col-1].active = 2;
		map[8][0].active = 2;
		map[8][col-1].active = 2;
		
		map[10][2].active=2;
		map[11][1].active=2;
		map[11][3].active=2;
		map[12][2].active=2;
	}
	
	
	
	public int draw (Graphics2D g, Brick map[][], State state) {
		activeBricks = 0;
		for (int i=0; i<map.length; i++) {
			for (int j=0; j<map[0].length; j++) {
				if (map[i][j].active > 0) {
					
					if (map[i][j].size > 0)
						activeBricks++;
					
					switch (map[i][j].active) {
					case 1:
						g.setColor(Color.WHITE);
						break;
					case 2:
						g.setColor(Color.GRAY);
						break;
					case 3:
						g.setColor(Color.RED.darker());
						break;
					}
					
					if (i==row-1 && j==2) {
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x, r.y);
						map[i][j].vertices[2] = new DoublePoint(r.x + r.width * 1/2, r.y + r.height);
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y);
						
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					}
					
					if (i==row-3 && j==0) {
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x, r.y);
						map[i][j].vertices[2] = new DoublePoint(r.x + r.width, r.y + r.height);
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y);
						
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					}
					
					if (i==row-2 && j==1) {
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x, r.y);
						map[i][j].vertices[2] = new DoublePoint(r.x + r.width, r.y + r.height);
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y);
						
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					}
					
					if (i==row-3 && j==col-1) {
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x, r.y);
						map[i][j].vertices[2] = new DoublePoint(r.x, r.y + r.height);
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y);
						
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					}
					
					if (i==row-2 && j==col-2) {
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x, r.y);
						map[i][j].vertices[2] = new DoublePoint(r.x, r.y + r.height);
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y);
						
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					}		
					
					// Ako je poligon petougao
					if (map[i][j].size==5) {
						
						// Ako se petougao rotira
						if (map[i][j].direction!=0) {
							if (state == State.START || state == State.RESTART) {
								Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
								map[i][j].vertices[2] = new DoublePoint(r.x, r.y);
								map[i][j].vertices[1] = new DoublePoint(r.x + r.width*1/2, r.y + r.height);
								map[i][j].vertices[0] = new DoublePoint(r.x + r.width, r.y); 
								
								r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, (i-1)*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
								map[i][j].vertices[4] = new DoublePoint(r.x + r.width*3/4, r.y);
								map[i][j].vertices[3] = new DoublePoint(r.x + r.width/4, r.y);
								
								// Centar rotacije na istom mestu kao i kod sestougla
								// Na sredini praznog prostora izmedju elemenata mozaika map[i][j] i 
								// map[i+1][j] kada bi se tu nalazili obicni pravougaonici
								// Empirijski se pokazalo kao dovoljno dobra aproksimacija
								map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height + emptySpace/2);
								
								int xPoints[] = map[i][j].makeXPoints();
								int yPoints[] = map[i][j].makeYPoints();
								g.fillPolygon(xPoints, yPoints, map[i][j].size);
												
								continue;
								
							} else {
								map[i][j].rotate(); 
								
								//reset(map, i, j);
								
								int xPoints[] = map[i][j].makeXPoints();
								int yPoints[] = map[i][j].makeYPoints();
								g.fillPolygon(xPoints, yPoints, map[i][j].size);
										
								continue;
							}
						
						}
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[2] = new DoublePoint(r.x, r.y);
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width*1/2, r.y + r.height);
						map[i][j].vertices[0] = new DoublePoint(r.x + r.width, r.y); 
						
						
						r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, (i-1)*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[4] = new DoublePoint(r.x + r.width*3/4, r.y);
						map[i][j].vertices[3] = new DoublePoint(r.x + r.width/4, r.y);
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height + emptySpace/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					}
					
					// Ako je poligon sestougao
					if (map[i][j].size==6) {					
						// Ako se sestougao rotira
						if (map[i][j].direction!=0) {
							if (state == State.START || state == State.RESTART) {
								Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
								map[i][j].vertices[3] = new DoublePoint(r.x, r.y);
								map[i][j].vertices[2] = new DoublePoint(r.x + r.width*1/4, r.y + r.height);
								map[i][j].vertices[1] = new DoublePoint(r.x + r.width*3/4, r.y + r.height);		
								map[i][j].vertices[0] = new DoublePoint(r.x + r.width, r.y); 
								
								r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, (i-1)*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
								map[i][j].vertices[5] = new DoublePoint(r.x + r.width*3/4, r.y);
								map[i][j].vertices[4] = new DoublePoint(r.x + r.width/4, r.y);
								
								// Centar sestougla oko koga se rotira je tacno na sredini praznog prostora izmedju 
								// map[i][j] i map[i+1][j] kada bi se tu nalazili obicni pravougaonici  
								map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height + emptySpace/2);
								
								int xPoints[] = map[i][j].makeXPoints();
								int yPoints[] = map[i][j].makeYPoints();
								g.fillPolygon(xPoints, yPoints, map[i][j].size);
								
								continue;
								
							} else {
								
								map[i][j].rotate(); 
								
								//reset(map, i, j);
								
								int xPoints[] = map[i][j].makeXPoints();
								int yPoints[] = map[i][j].makeYPoints();
								g.fillPolygon(xPoints, yPoints, map[i][j].size);
								
								continue;
							}
						
						}
						
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[3] = new DoublePoint(r.x, r.y);
						map[i][j].vertices[2] = new DoublePoint(r.x + r.width*1/4, r.y + r.height);
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width*3/4, r.y + r.height);		
						map[i][j].vertices[0] = new DoublePoint(r.x + r.width, r.y); 
						
						r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, (i-1)*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[5] = new DoublePoint(r.x + r.width*3/4, r.y);
						map[i][j].vertices[4] = new DoublePoint(r.x + r.width/4, r.y);
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height + emptySpace/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						
						continue;
					}
					
					// Iscrtavanje trouglova na ivicama			
					if (i==4 && j==0) {
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x + r.width, r.y); 
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y + r.height);
						map[i][j].vertices[2] = new DoublePoint(r.x, r.y + r.height);
						
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					}
								
					if (i==4 && j==col-1) {
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x, r.y); 
						map[i][j].vertices[1] = new DoublePoint(r.x, r.y + r.height);
						map[i][j].vertices[2] = new DoublePoint(r.x + r.width, r.y + r.height);
						
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					} 			
					
					if (map[i][j].size==4) {
						
						// Pravougaonik koji se rotira
						if (map[i][j].direction!=0) {
							if (state == State.START || state == State.RESTART) {
								Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
								
								//Centar rotacije je u preseku dijagonala
								map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
			
							} else {
								map[i][j].rotate(); 
								
								//reset(map, i, j);
								
								int xPoints[] = map[i][j].makeXPoints();
								int yPoints[] = map[i][j].makeYPoints();
								g.fillPolygon(xPoints, yPoints, map[i][j].size);
						
								continue;
							}
						}
						
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x, r.y); 
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y);
						map[i][j].vertices[2] = new DoublePoint(r.x + r.width, r.y + r.height);
						map[i][j].vertices[3] = new DoublePoint(r.x, r.y + r.height);
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						g.fillRect(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight - emptySpace);
						continue;
					}
						
					if (map[i][j].size==3) {
						
						// Trougao koji se rotira
						if (map[i][j].direction!=0) {
							if (state == State.START || state == State.RESTART) {
								Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
								map[i][j].vertices[0] = new DoublePoint(r.x, r.y + r.height/2);
								map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y);
								map[i][j].vertices[2] = new DoublePoint(r.x + r.width, r.y + r.height);
								
								// Centar rotacije je na istom mestu kao i kod pravougaonika
								// U preseku dijagonala potencijalnog pravouganika koji bi se nalazio na poziciji map[i][j] 
								map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
								
								int xPoints[] = map[i][j].makeXPoints();
								int yPoints[] = map[i][j].makeYPoints();
								g.fillPolygon(xPoints, yPoints, map[i][j].size);
								
								continue;
								
							} else {
								map[i][j].rotate(); 
								
								//reset(map, i, j);
									
								int xPoints[] = map[i][j].makeXPoints();
								int yPoints[] = map[i][j].makeYPoints();
								g.fillPolygon(xPoints, yPoints, map[i][j].size);
								
								continue;
							}
						}
						
						Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
						map[i][j].vertices[0] = new DoublePoint(r.x, r.y); 
						map[i][j].vertices[1] = new DoublePoint(r.x + r.width, r.y);
						map[i][j].vertices[2] = new DoublePoint(r.x, r.y + r.height);
						map[i][j].polCenter = new DoublePoint(r.x + r.width/2, r.y + r.height/2);
						
						int xPoints[] = map[i][j].makeXPoints();
						int yPoints[] = map[i][j].makeYPoints();
						g.fillPolygon(xPoints, yPoints, map[i][j].size);
						continue;
					}
					
				} else if (map[i][j].active < 0) {
					
					Rectangle r = new Rectangle(j*brickWidth + gamePan.getSize().width * 1/7, i*brickHeight + gamePan.getSize().height*1/14, brickWidth - emptySpace, brickHeight-emptySpace);
					
					map[i][j].polCenter = new DoublePoint(r.x + brickWidth/2, r.y + brickWidth/2);
					
					TexturePaint tp = new TexturePaint(gamePan.getMetalTexture(), r);
					
					Graphics2D g2d = (Graphics2D) g.create();
					
					// Crtanje loptice
					g2d.setPaint(tp);
	
					g2d.fillOval(r.x, r.y, brickWidth, brickWidth);
					g2d.setColor(Color.RED.darker());
					g2d.setStroke(new BasicStroke(emptySpace/2));
					g2d.drawOval(r.x, r.y, brickWidth, brickWidth);
					
					continue;
				
				}
			}
		}
		
		return activeBricks;
	}
	
	public int getBrickWidth () {
		return brickWidth;
	}
}
