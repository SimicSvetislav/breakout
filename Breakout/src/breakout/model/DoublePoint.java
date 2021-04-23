package breakout.model;

public class DoublePoint extends Vector2D{
	
	RotationMatrix rotationMatrix;
	
	public DoublePoint() {
		super();
	}
	
	public DoublePoint(double x, double y) {
		super(x, y);
	}
	
	public DoublePoint(DoublePoint d) {
		super(d.x, d.y);
	}
	
	public DoublePoint addP(Vector2D vector) {
		return new DoublePoint(x + vector.x, y + vector.y);
	}
	
}
