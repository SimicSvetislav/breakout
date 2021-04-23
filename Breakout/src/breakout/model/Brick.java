package breakout.model;

import breakout.view.BreakoutFrame;

public class Brick {
	public int active;
	public DoublePoint[] vertices;
	public int size;
	public DoublePoint polCenter;
	public int direction;	// Smer rotacije 1 - u smeru kazaljke na sat, 0 - nema rotacije, -1 - u suprotnom smeru od smera kazaljke na satu
	public RotationMatrix rotationMatrix;
	
	public Brick(int size, int direction, int active) {
		this.size = size;
		this.active = active;
		if (size>0) {
			vertices = new DoublePoint[size];
		}
		
		this.direction = direction;
		rotationMatrix = new RotationMatrix(BreakoutFrame.getInstance().getGamePan().getRotationAngle(), direction);
	}

	public int[] makeXPoints() {
		int xs[] = new int[size];
		
		for (int i=0; i<size; i++) {
			xs[i] = (int)(vertices[i].getX());
		}
		
		return xs;
	}
	
	public int[] makeYPoints() {
		int ys[] = new int[size];
		
		for (int i=0; i<size; i++) {
			ys[i] = (int)(vertices[i].getY());
		}
		
		return ys;
	}
	
	public void rotate () {
		for (int i = 0; i<size; i++) {
			DoublePoint p = vertices[i];
			DoublePoint temp = new DoublePoint(p.getX() - polCenter.getX(), p.getY() - polCenter.getY());
			
			double temp2;
			temp2 = temp.getX() * rotationMatrix.matrix[0][0] + temp.getY() * rotationMatrix.matrix[0][1];
			temp.setY(temp.getX() * rotationMatrix.matrix[1][0] + temp.getY() * rotationMatrix.matrix[1][1]);
			temp.setX(temp2);
			
			p.setX(temp.getX() + polCenter.getX());
			p.setY(temp.getY() + polCenter.getY());
		}
	}
}
