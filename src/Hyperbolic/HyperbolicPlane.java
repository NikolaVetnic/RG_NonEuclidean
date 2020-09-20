package Hyperbolic;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;

public class HyperbolicPlane implements Drawing {
	
	@GadgetDouble(min = 100.0, max = 500.0)
	double r_global = 100.0;						// global radius
	
//	@GadgetDouble(min = 0.80, max = 1.0)
	double z = 0.80;
	
	@GadgetVector
	Vector C = Vector.ZERO;							// polygon center
	
//	@GadgetDouble(min = 10.0, max = 1000.0)
	double r = 15.0;								// polygon radius
	
	@GadgetInteger(min = 2, max = 100)
	int n = 3;										// polygon number of verts
	
	@GadgetVector
	Vector P = Vector.ZERO;							// rotation pivot		
	
	@GadgetDouble(min = 0.0, max = 1.0)
	double phi = 0.0;								// rotation angle
	
//	@GadgetDouble(min = 10.0, max = 1000.0)
	double g = 100.0;								// grain (when using points for strokes)
	
//	@GadgetBoolean
	boolean gridLines = true;						// grid display (lines or points)
	

	@Override
	public void draw(View view) {
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		Aux2H grid = new Aux2H(r_global, z * r_global, g);
		grid.strokeGrid(view, gridLines);
		
		Poly2H polyH = new Poly2H(r_global, z * r_global, C.div(4), r, n, P.div(4));
		polyH.rotate(phi);
		polyH.stroke(view, Color.INDIANRED, g);
	}
	
	
	public static void main(String[] args) {
		
		DrawingApplication.launch(600, 600);
	}
}
