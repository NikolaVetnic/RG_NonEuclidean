package Spherical;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetBoolean;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.geometry.Vector;

public class SphericalPlane implements Drawing {
	
	@GadgetDouble(min = 100.0, max = 500.0)
	double R = 100.0;
	
	@GadgetDouble(min = 0.80, max = 1.0)
	double zz = 0.0;
	
	@GadgetDouble(min = 10.0, max = 1000.0)
	double g = 100.0;
	
	@GadgetBoolean
	boolean gridLines = false;
	
	
	class Point2S {
		

		double R, x, y, z;
		
		
		public Point2S(double R, double x, double y, double z) {
			this.R = R;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		
		private double[] cartesianToPolar(double x, double y, double z) {
			
			return new double[] { 
					Math.sqrt(x * x + y * y + z * z),			// r
					Math.atan2(y, x),							// phi
					Math.atan2(Math.sqrt(x * x + y * y), z) 	// theta
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
	
	
	void strokeGrid(View view, Color c, double grain) {
		
		view.setStroke(c);
		
		view.strokeLine(Vector.polar(500, 0.00), Vector.polar(500, 0.50));
		view.strokeLine(Vector.polar(500, 0.25), Vector.polar(500, 0.75));
		
		for (int j = 0; j < (int) g - 1; j++) {
			
			for (int i = 0; i < (int) g - 1; i++) {
				
				double x1 = (-1.0 + 2.0 * i / g) * R;
				double y1 = (-1.0 + 2.0 * j / g) * R;				
				
				double x2 = (-1.0 + 2.0 * (i + 1) / g) * R;
				double y2 = (-1.0 + 2.0 * (j + 1) / g) * R;
				
				Point2S pnt0 = new Point2S(R, x1, y1, zz * R);
				Point2S pnt1 = new Point2S(R, x1, y2, zz * R);
				Point2S pnt2 = new Point2S(R, x2, y1, zz * R);
				
				if (gridLines) {
					view.strokeLine(pnt0.project(), pnt1.project());
					view.strokeLine(pnt0.project(), pnt2.project());
				} else {
					view.strokeCircleCentered(pnt1.project(), 0.5);
				}
			}
		}
	}
	

	@Override
	public void draw(View view) {
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		view.setStroke(Color.gray(0.50));
		
		strokeGrid(view, Color.gray(0.5), g);
	}
	
	
	public static void main(String[] args) {
		
		DrawingApplication.launch(600, 600);
	}
}
