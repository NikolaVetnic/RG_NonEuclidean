package PoincareDiskModel;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;

public class PoincareDisk implements Drawing {
	
	static final double DISC_R = 300.0;
	
	
	@GadgetVector
	Vector p = Vector.ZERO;

	@GadgetVector
	Vector q = Vector.ZERO;
	
	@GadgetDouble(min = 0.0, max = DISC_R - 0.001)
	double r = 100.0;
	
	@GadgetDouble(min = 0.0, max = 1.0)
	double phi = 0.0;

	@GadgetInteger(min = 2, max = 100)
	int n = 3;
	
//	@GadgetBoolean
	boolean drawAux = true;
	
//	@GadgetInteger(min = 0, max = 4)
	int drwIndex = 0;
	
	
	Vector intersection(Vector[] P, Vector[] Q) {
		
		double xx = ((P[3].x * P[4].y - P[3].y * P[4].x) * (Q[3].x - Q[4].x) - 
				     (P[3].x - P[4].x) * (Q[3].x * Q[4].y - Q[3].y * Q[4].x)) 
				  / ((P[3].x - P[4].x) * (Q[3].y - Q[4].y) -
				     (P[3].y - P[4].y) * (Q[3].x - Q[4].x));
		
		double yy = ((P[3].x * P[4].y - P[3].y * P[4].x) * (Q[3].y - Q[4].y) -
				     (P[3].y - P[4].y) * (Q[3].x * Q[4].y - Q[3].y * Q[4].x))
				  / ((P[3].x - P[4].x) * (Q[3].y - Q[4].y) - 
					 (P[3].y - P[4].y) * (Q[3].x - Q[4].x));
		
		return new Vector(xx, yy);
	}
	
	
	Vector[] getInvPoints(double r, double phi) {
		
		Vector[] verts = new Vector[5];
		
		verts[0] = Vector.polar(r, phi);										// point
		verts[1] = Vector.polar(DISC_R * DISC_R / r, phi);						// inversion
		verts[2] = Vector.polar(DISC_R * DISC_R / r * 0.5 + r * 0.5, phi);		// midpoint
		
		verts[3] = new Vector(verts[2].add(Vector.polar(300, phi + 0.25)).x, 
							  verts[2].add(Vector.polar(300, phi + 0.25)).y);
		
		verts[4] = new Vector(verts[2].add(Vector.polar(300, phi + 0.75)).x, 
				  			  verts[2].add(Vector.polar(300, phi + 0.75)).y);
		
		return verts;
	}
	
	
	double[] getArcAngles(Vector C, Vector[] P, Vector[] Q) {
		
		double[] ang = new double[2];
		
		ang[0] = 1.0 - Q[0].sub(C).angle() + P[0].sub(C).angle();
		ang[1] = Q[0].sub(C).angle() - P[0].sub(C).angle();
		
		return ang;
	}
	
	
	int getQuad(double phi) {
		
		int quad = phi <= 0.25 ? 0 :
			   	   phi <= 0.50 ? 1 :
			   	   phi <= 0.75 ? 2 : 3;
		
		return quad;
	}
	
	
	double[] cartToPolar(Vector p) {
		return new double[] { Vector.ZERO.distanceTo(p) > DISC_R ? DISC_R : Vector.ZERO.distanceTo(p), p.angle() };
	}
	
	
	void drawDisk(View view) {
		
		view.setFill(Color.gray(0.5));
		view.fillCircleCentered(Vector.ZERO, 2.5);

		view.setStroke(Color.gray(0.5));
		view.setLineWidth(2.0);
		view.strokeCircleCentered(Vector.ZERO, 4.5);
		
		view.strokeCircleCentered(Vector.ZERO, DISC_R);
	}
	
	
	void drawDiskMask(View view) {
		
		view.setFill(Color.hsb(0, 0, 0, 0.5));
		view.fillCircleCenteredInverted(Vector.ZERO, DISC_R);
		view.strokeCircleCentered(Vector.ZERO, DISC_R);
	}
	
	
	void strokeHLineAux(View view, Color c, Vector[] verts) {
		
		view.setFill(Color.gray(0.875));
		view.setStroke(Color.gray(0.5));
		view.setLineWidth(1.0);
		
		view.strokeLine(Vector.ZERO, verts[1]);									// O-P0 axis
		view.fillCircleCentered(verts[1], 2.5);
		view.fillCircleCentered(verts[2], 2.5);
		
		view.setLineWidth(0.5);
		view.strokeLine(verts[3], verts[4]);									// perpendicular to P0-P1 in PM
		
		view.setFill(c);
		view.fillCircleCentered(verts[0], 2.5);
	}
	
	
	void strokeHLine(View view, Color c, double r1, double phi1, double r2, double phi2) {
	
		Vector[] P = getInvPoints(r1, phi1);
		if (drawAux) strokeHLineAux(view, c, P);
		
		Vector[] Q = getInvPoints(r2, phi2);
		if (drawAux) strokeHLineAux(view, c, Q);
		
		Vector C = intersection(P, Q);			
		if (drawAux) view.fillCircleCentered(C, 5.0);
		
		double[] arcAngles = getArcAngles(C, P, Q);
		int quad = getQuad(phi1);
		
		view.setStroke(c);
		view.setLineWidth(1.5);

		switch (quad) {
		
			case 0:
				view.strokeArcCentered(C, new Vector(C.distanceTo(P[0])), P[0].sub(C).angle(), arcAngles[1]);
				break;
				
			case 1:
				if (phi2 <= phi1 || phi1 + 0.5 <= phi2) {
					if (arcAngles[0] > 0 && arcAngles[1] > 0)
						view.strokeArcCentered(C, new Vector(C.distanceTo(P[0])), P[0].sub(C).angle(), arcAngles[1]);
					else
						view.strokeArcCentered(C, new Vector(C.distanceTo(Q[0])), P[0].sub(C).angle(), arcAngles[1] + 1.0);
				} else {
					if (arcAngles[0] < 0 || arcAngles[1] < 0)
						view.strokeArcCentered(C, new Vector(C.distanceTo(P[0])), P[0].sub(C).angle(), arcAngles[1]);
					else
						view.strokeArcCentered(C, new Vector(C.distanceTo(Q[0])), Q[0].sub(C).angle(), arcAngles[0]);
				}
				break;
				
			case 2:
				if (phi2 >= phi1 || phi2 <= phi1 - 0.5) {
					if (arcAngles[1] < 0)
						view.strokeArcCentered(C, new Vector(C.distanceTo(P[0])), P[0].sub(C).angle(), arcAngles[1]);
					else
						view.strokeArcCentered(C, new Vector(C.distanceTo(Q[0])), Q[0].sub(C).angle(), arcAngles[0]);
				} else {
					if (arcAngles[1] > 0)
						view.strokeArcCentered(C, new Vector(C.distanceTo(P[0])), P[0].sub(C).angle(), arcAngles[1]);
					else
						view.strokeArcCentered(C, new Vector(C.distanceTo(Q[0])), P[0].sub(C).angle(), arcAngles[1] + 1.0);
				}
				break;
				
			case 3:
				view.strokeArcCentered(C, new Vector(C.distanceTo(P[0])), P[0].sub(C).angle(), arcAngles[1]);
				break;
				
			default:
				break;
		}
		
		view.strokeCircleCentered(P[0], 2.5);
		view.strokeCircleCentered(Q[0], 2.5);
	}
	
	
	void strokeHLine(View view, Color c, Vector p, Vector q) {
		
		double[] pp = cartToPolar(p);
		double[] qq = cartToPolar(q);
		
		strokeHLine(view, c, pp[0], pp[1], qq[0], qq[1]);
	}
	
	
	void strokeHPolyCentered(View view, Color c, Vector C, Vector R, double rPoly, double phiRotation, int sides) {

		view.setStroke(Color.CORNFLOWERBLUE);							// rotation axis
		view.strokeCircleCentered(R, 4.5);
		
		view.setStroke(c);
		
		Vector objCenter = Vector.polar(0.0, 0.0).add(C);				// object center
		double objAngle  = objCenter.sub(R).angle() - phiRotation;
		double objRadius = Vector.ZERO.distanceTo(objCenter.sub(R));
		
		objCenter = Vector.polar(objRadius, objAngle).add(R);
		objAngle = objCenter.angle();
		objRadius = Vector.ZERO.distanceTo(objCenter) > DISC_R ? DISC_R : Vector.ZERO.distanceTo(objCenter);
		
		view.strokeCircleCentered(Vector.polar(objRadius, objAngle), 4.5);
		
		Vector[] verts = new Vector[sides];								// creating polygon
		
		double[] a = new double[sides];
		double[] r = new double[sides];
		
		
		for (int i = 0; i < sides; i++) {
			
			verts[i] = Vector.polar(rPoly, (double) i / sides).add(C);
			
			a[i] = verts[i].sub(R).angle() - phiRotation;
			r[i] = Vector.ZERO.distanceTo(verts[i].sub(R));
			
			verts[i] = Vector.polar(r[i], a[i]).add(R);
		}
		
		for (int i = 0; i < sides; i++)
			strokeHLine(view, c, verts[i], verts[(i + 1) % sides]);		// rendering polygon
	}
	

	@Override
	public void draw(View view) {
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		drawDisk(view);
		
		
		String drwCaption;
		
		switch (drwIndex) {
			case 0:
				strokeHPolyCentered(view, Color.hsb(0, 0.85, 0.85), p, q, r, phi, n);
				drwCaption = "A polygon centered at Cartesian vector.";
				break;
			
			default:
				drwCaption = "Empty slot.";
				break;
		}
		
		DrawingUtils.drawInfoText(view, drwCaption);
	}

	
	public static void main(String[] args) {
		
		DrawingApplication.launch(600, 600);
	}
}
