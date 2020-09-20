package Auxiliary;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;

public class PivotalRotation implements Drawing {

	@GadgetVector
	Vector p = Vector.ZERO;
	
	@GadgetVector
	Vector q = Vector.ZERO;
	
	@GadgetDouble(min = 0.0, max = 1.0)
	double phi = 0.0;
	
	
	@Override
	public void draw(View view) {
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		view.setStroke(Color.gray(0.25));
		view.strokeLine(Vector.polar(500, 0.50), Vector.polar(500, 0.00));
		view.strokeLine(Vector.polar(500, 0.25), Vector.polar(500, 0.75));
		
		int n = 3;
		
		double s = Math.sin(phi * 2 * Math.PI);						// rotation angle sine
		double c = Math.cos(phi * 2 * Math.PI);						// rotation angle cosine
		
		for (int i = 0; i < n; i++) {
			
			double phi1 = 1.0 * i / n;
			double phi2 = ((i + 1.0) % n) / n;
			
			Vector a = p.add(Vector.polar(50, phi1));				// polygon vertes
			Vector b = p.add(Vector.polar(50, phi2));				// next polygon vertex
			Vector d = p;											// polygon center
			
			a = a.sub(q);											// subtract pivot	
			b = b.sub(q);
			d = d.sub(q); 
			
			a = new Vector(a.x * c - a.y * s, a.x * s + a.y * c);	// calculate position after rotation
			b = new Vector(b.x * c - b.y * s, b.x * s + b.y * c);
			d = new Vector(d.x * c - d.y * s, d.x * s + d.y * c);
			
			a = a.add(q);											// add pivot
			b = b.add(q);
			d = d.add(q);
			
			view.setStroke(Color.INDIANRED);
			view.strokeLine(a, b);
			view.strokeCircleCentered(d, 2.5);
		}
		
		view.setStroke(Color.CORNFLOWERBLUE);
		view.strokeCircleCentered(q, 2.5);
		
	}
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
}
