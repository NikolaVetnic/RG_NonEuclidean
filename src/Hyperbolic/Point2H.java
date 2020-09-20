package Hyperbolic;

import mars.geometry.Vector;

public class Point2H {

	double R, x, y, z;
	
	
	public Point2H(double R, double x, double y, double z) {
		this.R = R;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public Point2H(double R, Vector p, double z) {
		this.R = R;
		this.x = p.x;
		this.y = p.y;
		this.z = z;
	}
	
	
	private double[] cartesianToPolar(double x, double y, double z) {
		
		return new double[] { 
				Math.sqrt(x * x + y * y + z * z),           // r
				Math.atan2(y, x),                           // phi
				Math.atan2(Math.sqrt(x * x + y * y), z)     // theta
		};
	}

	
	public Vector project() {
		
		double[] polar = cartesianToPolar(x, y, z);
        
		// 1.5 - random factor, makes it better looking
		return Vector.polar(2 * polar[0] * Math.tan(1.5 * polar[2]), polar[1] / (2 * Math.PI));
	}
	
	
	public String toString() {
		return "( " + x + ", " + y + ", " + z + " )";
	}
}
