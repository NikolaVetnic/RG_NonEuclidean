package Spherical;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetBoolean;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;

public class HyperbolicPlane implements Drawing {
	
	@GadgetDouble(min = 100.0, max = 500.0)
	double R = 100.0;
	
//	@GadgetDouble(min = 0.80, max = 1.0)
	double zz = 0.80;
	
	@GadgetInteger(min = 2, max = 100)
	int n = 3;
	
//	@GadgetDouble(min = 10.0, max = 1000.0)
	double g = 100.0;
	
	@GadgetDouble(min = 0.0, max = 1.0)
	double phi = 0.0;
	
	@GadgetBoolean
	boolean gridLines = false;	
	
	@GadgetVector
	Vector p = Vector.ZERO;
	
	@GadgetVector
	Vector q = Vector.ZERO;
	
	
	class Point2S {
		

		double R, x, y, z;
		
		
		public Point2S(double R, double x, double y, double z) {
			this.R = R;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		
		public Point2S(double R, Vector p, double z) {
			this.R = R;
			this.x = p.x;
			this.y = p.y;
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
	
	
	class RegPoly {
		
		static final int MAX_VERTS = 100;
		
		
		Vector C;
		Vector P;
		Vector[] verts;
		int n;
		
		
		public RegPoly(Vector C, Vector P, double r, int n) {
			
			this.C = C;
			this.P = P;
			
			this.verts = new Vector[MAX_VERTS];
			for (int i = 0; i < n; i++)
				verts[this.n++] = Vector.polar(r, 1.0 * i / n);
		}
		
		
		private void rotate(double ang) {
			
			double s = Math.sin(ang * 2 * Math.PI);
			double c = Math.cos(ang * 2 * Math.PI);
			
			for (int i = 0; i < n; i++) {
				
				verts[i] = verts[i].sub(P);
				verts[i] = new Vector(verts[i].x * c - verts[i].y * s, verts[i].x * s + verts[i].y * c);
				verts[i] = verts[i].add(P);
			}
		}
		
		
		private void strokeLine(View view, Color c, double grain, Vector p0, Vector p1) {
			
			view.setStroke(c);
			view.setLineWidth(2.0);
			
			for (int i = 0; i < (int) grain; i++) {
				
				double t0 = i / grain;
				double t1 = (i + 1) / grain;
				
				Vector q0 = Vector.lerp(p0, p1, t0).add(C);
				Vector q1 = Vector.lerp(p0, p1, t1).add(C);
				
				view.strokeLine(
						(new Point2S(R, q0, zz * R)).project(),
						(new Point2S(R, q1, zz * R)).project());
			}
		}
		
		
		public void stroke(View view, Color c, double grain) {
			
			view.setStroke(Color.CORNFLOWERBLUE);
			view.strokeCircleCentered((new Point2S(R, P.add(C), zz * R)).project(), 2.5);
			
			for (int i = 0; i < n; i++)
				strokeLine(view, c, grain, verts[i], verts[(i+1) % n]);
		}
	}
	
	
	void strokeGrid(View view, Color c, double grain) {
		
		view.setStroke(c);
		view.setLineWidth(1.0);
		
		view.strokeLine(Vector.polar(500, 0.00), Vector.polar(500, 0.50));
		view.strokeLine(Vector.polar(500, 0.25), Vector.polar(500, 0.75));
		
		view.setLineWidth(0.5);
		
		for (int j = 0; j < (int) g - 1; j++) {
			
			for (int i = 0; i < (int) g - 1; i++) {
				
				Vector s1 = (new Vector(-1.0, -1.0)).add(((new Vector(i, j)).div(grain)).mul(2.0)).mul(R);
				Vector s2 = (new Vector(-1.0, -1.0)).add(((new Vector(i + 1, j + 1)).div(grain)).mul(2.0)).mul(R);
				
				Point2S pnt0 = new Point2S(R, s1, zz * R);
				Point2S pnt1 = new Point2S(R, s1.x, s2.y, zz * R);
				Point2S pnt2 = new Point2S(R, s2.x, s1.y, zz * R);
				
				if (gridLines) {
					view.strokeLine(pnt0.project(), pnt1.project());
					view.strokeLine(pnt0.project(), pnt2.project());
				} else {
					view.strokeCircleCentered(pnt1.project(), 0.5);
				}
			}
		}
	}

//	void strokeLine(View view, Color c, double grain, Vector p, Vector q) {
//		
//		for (int j = 0; j < (int) grain; j++)
//			view.strokeLine(
//					(new Point2S(R, Vector.lerp(p, q, ( j      / grain)), zz * R)).project(), 
//					(new Point2S(R, Vector.lerp(p, q, ((j + 1) / grain)), zz * R)).project());
//	}
//		
//	void strokePoly(View view, Color c, double grain, Vector[] verts) {
//		
//		view.setStroke(c);
//		
//		for (int i = 0; i < verts.length; i++)
//			strokeLine(view, c, grain, verts[i], verts[(i + 1) % verts.length]);
//	}
//	
//	void strokeRegPoly(View view, Color c, double grain, Vector C, double r, Vector R, double phi, int n) {
//		
//		view.setStroke(c);
//		
//		for (int i = 0; i < n; i++)
//			strokeLine(
//					view, c, grain, 
//					C.add(Vector.polar(r, 1.0 * i / n + phi)), 
//					C.add(Vector.polar(r, 1.0 * ((i+1)%n) / n + phi)));
//	}
	

	@Override
	public void draw(View view) {
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		strokeGrid(view, Color.gray(0.5), g);
		
		RegPoly poly = new RegPoly(p.div(4), q.div(4), 15, n);
		poly.rotate(phi);
		poly.stroke(view, Color.INDIANRED, g);
	}
	
	
	public static void main(String[] args) {
		
		DrawingApplication.launch(600, 600);
	}
}
