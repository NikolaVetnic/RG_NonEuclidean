package Auxiliary;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;

public class PolarSquare implements Drawing {
	
	@GadgetDouble(min = 50.0, max = 400.0)
	double side = 150.0;
	
	@GadgetVector
	Vector pos = new Vector(-25, -150);
	
	@GadgetDouble(min = 0.0, max = 1.0)
	double rot = 0.775;
	
	@GadgetInteger(min = 0, max = 3)
	int vert = 1;
	
	
	Vector toPolar(Vector p) {
		return Vector.polar(Math.sqrt(p.x * p.x + p.y * p.y), Math.atan2(p.y, p.x));
	}
	
	
	Vector[] createSquarePolar(double grain, double side, double rot) {

		double rotation = rot + 0.5;
		
		Vector[] shape = new Vector[(int) (1.0 / grain)];
		int i = 0;

		double r = side * 0.5;
		
		for (double x = 3.0 / 8; x <= 5.0 / 8; x += grain)
			shape[i++] = Vector.polar(r / Math.cos((2 * x) 	   * Math.PI), x + rotation);

		for (double x = 5.0 / 8; x <= 7.0 / 8; x += grain)
			shape[i++] = Vector.polar(r / Math.sin((2 * x) 	   * Math.PI), x + rotation);
		
		for (double x = 3.0 / 8; x <= 5.0 / 8; x += grain)
			shape[i++] = Vector.polar(r / Math.cos((2 * x + 1) * Math.PI), x + rotation);
		
		for (double x = 5.0 / 8; x <= 7.0 / 8; x += grain)
			shape[i++] = Vector.polar(r / Math.sin((2 * x + 1) * Math.PI), x + rotation);
		
		return shape;
	}
	
	
	
	Vector[] drawCirclePolar(double grain, double r) {

		// object is westward 
		double rotation = rot + 0.5;
		
		Vector[] shape = new Vector[(int) (1.0 / grain)];
		int i = 0;
		
		for (double x = 0.00; x <= 1.00; x += grain)
			shape[i++] = Vector.polar(r, x + rotation);
		
		return shape;
	}
	
	
	void drawGuides(View view) {
		
		// OO' axis
		view.setStroke(Color.gray(0.375));
		view.setLineWidth(2.0);
		view.setLineDashes(0.0);
		
		view.strokeLine(Vector.polar(0.00, 0.00), Vector.polar(500, 0.00));
		
		// global axes
		view.setStroke(Color.gray(0.25));
		view.setLineWidth(1.0);
		view.setLineDashes(0.0);
		
		view.strokeLine(Vector.polar(0.0, 0.0), Vector.polar(500, 0.25));
		view.strokeLine(Vector.polar(0.0, 0.0), Vector.polar(500, 0.50));
		view.strokeLine(Vector.polar(0.0, 0.0), Vector.polar(500, 0.75));
		
		// OC vector
		view.setStroke(Color.gray(0.5));
		view.setLineWidth(1.0);
		view.setLineDashes(5.0);
		
		view.strokeLine(Vector.ZERO, Vector.polar(500, pos.angle()));
		
		// θ[c] angle
		view.setStroke(Color.gray(0.5));
		view.setLineWidth(2.5);
		view.setLineDashes(0.0);
		
		view.strokeArcCentered(Vector.ZERO, new Vector(25, 25), 0.0, pos.angle());
	}
	
	
	void renderStroke(View view, Color c, Vector[] shape, Vector p) {
		
		// OVC triangle
		view.setStroke(Color.INDIANRED);
		view.setLineWidth(1.5);
		view.setLineDashes(0.0);
		
		view.strokePolygon(Vector.ZERO, p, shape[vert * shape.length / 4].add(p));
		
		// θ[v] angle
		view.setLineWidth(2.5);
			view.strokeArcCentered(Vector.ZERO, new Vector(35, 35), 0.0, shape[vert * shape.length / 4].add(p).angle());
		
		// CC' axis
		double rotation = rot + 0.5;
			
		view.setStroke(c);
		view.setLineWidth(1.5);
		view.setLineDashes(5.0);
		
		view.strokeLine(p, Vector.polar(1000, rotation).add(p));
		
		view.setLineWidth(0.5);
		view.setLineDashes(10.0);
		
		view.strokeLine(p, Vector.polar(1000, rotation + 0.5).add(p));
		
		// θ[local] angle
		view.setStroke(Color.CORNFLOWERBLUE);
		view.setLineWidth(1.5);
		view.setLineDashes(0.0);
			view.strokeArcCentered(pos, new Vector(-35), rot, (2 * vert - 1) * 0.125);
		
		// α angle
		view.setStroke(Color.CORNFLOWERBLUE);
		view.setLineWidth(3.5);
			view.strokeArcCentered(pos, new Vector(-50), p.angle(), rot - p.angle());
		
		// β angle
		view.setStroke(Color.CORNFLOWERBLUE);
		view.setLineWidth(2.5);
			view.strokeArcCentered(pos, new Vector(-65), p.angle(), rot + (2 * vert - 1) * 0.125 - p.angle());
		
		// render radial spokes
		view.setStroke(Color.hsb(c.getHue(), c.getSaturation(), c.getBrightness(), 0.15));
		view.setLineDashes(0.0);
		view.setLineWidth(1.0);
		
		for (int i = 0; i < shape.length - 1; i += 20)
			view.strokeLine(p, shape[i].add(p));
		
		// render shape
		view.setStroke(c);
		
		for (int i = 0; i < shape.length - 1; i++)
			view.strokeLine(shape[i].add(p), shape[i + 1].add(p));
	}
	

	@Override
	public void draw(View view) {
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		drawGuides(view);
		
//		Vector[] circle = drawCirclePolar(0.001, side * 0.5);
//		renderStroke(view, Color.INDIANRED, circle, pos);
		
		Vector[] square = createSquarePolar(0.001, side, rot);
		renderStroke(view, Color.GREENYELLOW, square, pos);
	}

	
	public static void main(String[] args) {
		
		DrawingApplication.launch(600, 600);
	}
}