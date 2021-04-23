package breakout.model;

public class Vector2D {
	
	public double x;
	public double y;
	
	public Vector2D () {
		x = 0;
		y = 0;
	}
	
	public Vector2D (double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D (Vector2D v) {
		x = v.x;
		y = v.y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Vector2D add(Vector2D vector) {
		return new Vector2D(x + vector.x, y + vector.y);
	}
	
	public Vector2D diff(Vector2D vector) {
		return new Vector2D(x-vector.x, y-vector.y);
	}
	
	public Vector2D scalarMul(double scalar) {
		return new Vector2D(x * scalar, y * scalar);
	}
	
	public double dotProduct(Vector2D vector) {
		return x*vector.x+y*vector.y;
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y);
	}
	
	public Vector2D normalize() {
		return new Vector2D(x / magnitude(), y / magnitude());
	}
	
	public Vector2D getNormal() {
		// Normala ka desno, ako posmatramo vektor u smeru strelice
		return new Vector2D(-y, x);
	}
	
	public Vector2D setLength(double length) {
		Vector2D norm = normalize();
		norm = norm.scalarMul(length);
		return new Vector2D(norm);
	}
	
	@Override
	public String toString() {
		String retVal = String.format("x = %f\ny = %f", x, y);
		return retVal;
	}
}
