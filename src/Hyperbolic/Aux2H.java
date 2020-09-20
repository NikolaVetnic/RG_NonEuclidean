package Hyperbolic;

import javafx.scene.paint.Color;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;

public class Aux2H {

	double r_global, z_global, grain;
	
	
	public Aux2H(double r_global, double z_global, double grain) {
		this.r_global = r_global;
		this.z_global = z_global;
		this.grain = grain;
	}
	
	
	void strokeGrid(View view, boolean lines) {
		
		view.setStroke(Color.gray(0.50));
		view.setLineWidth(1.0);
		
		view.strokeLine(Vector.polar(500, 0.00), Vector.polar(500, 0.50));
		view.strokeLine(Vector.polar(500, 0.25), Vector.polar(500, 0.75));
		
		view.setStroke(Color.gray(0.25));
		view.setLineWidth(0.5);
		
		for (int j = 0; j < (int) grain - 1; j++) {
			
			for (int i = 0; i < (int) grain - 1; i++) {
				
				Vector s1 = (new Vector(-1.0, -1.0)).add(((new Vector(i, j)).div(grain)).mul(2.0)).mul(r_global);
				Vector s2 = (new Vector(-1.0, -1.0)).add(((new Vector(i + 1, j + 1)).div(grain)).mul(2.0)).mul(r_global);
				
				Point2H pnt0 = new Point2H(r_global, s1, z_global);
				Point2H pnt1 = new Point2H(r_global, s1.x, s2.y, z_global);
				Point2H pnt2 = new Point2H(r_global, s2.x, s1.y, z_global);
				
				if (lines) {
					view.strokeLine(pnt0.project(), pnt1.project());
					view.strokeLine(pnt0.project(), pnt2.project());
				} else {
					view.strokeCircleCentered(pnt1.project(), 0.5);
				}
			}
		}
	}
}
