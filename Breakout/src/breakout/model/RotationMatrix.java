package breakout.model;

public class RotationMatrix {
	
	public double[][] matrix = new double[2][2];
	
	public RotationMatrix(double rotationAngle, int direction) {
		if (direction!=0) {	
			// Pretvaranje u radijane
			double radians = direction*rotationAngle*Math.PI/180;
			
			matrix[0][0] = Math.cos(radians);
			matrix[0][1] = -Math.sin(radians);
			matrix[1][0] = Math.sin(radians);
			matrix[1][1] = Math.cos(radians);
		}
	}
}
