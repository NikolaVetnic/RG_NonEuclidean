package Hyperbolic;

import javafx.scene.paint.Color;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;

public class Poly2H {

	static final int MAX_VERTS = 100;
	
	double r_global;
	double z_global;
	
	
	Vector C;
	Vector P;
	Vector[] verts;
	int n;
	
	
	public Poly2H(double r_global, double z_global, Vector C, double r, int n, Vector P) {
		
		this.r_global = r_global;
		this.z_global = z_global;
		
		this.C = C;
		
		this.verts = new Vector[MAX_VERTS];
		for (int i = 0; i < n; i++)
			verts[this.n++] = Vector.polar(r, 1.0 * i / n);

		this.P = P;
	}
	
	
	public Poly2H(double r_global, double z_global, Vector C, Vector P, Vector[] verts) {

		this.r_global = r_global;
		this.z_global = z_global;
		
		this.C = C;
		
		this.verts = verts;
		this.n = verts.length;
		
		this.P = P;
	}
	
	
	public void rotate(double ang) {
		
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
					(new Point2H(r_global, q0, z_global)).project(),
					(new Point2H(r_global, q1, z_global)).project());
		}
	}
	
	
	public void stroke(View view, Color c, double grain) {
		
		view.setStroke(Color.CORNFLOWERBLUE);
		view.strokeCircleCentered((new Point2H(r_global, P.add(C), z_global)).project(), 2.5);
		
		for (int i = 0; i < n; i++)
			strokeLine(view, c, grain, verts[i], verts[(i+1) % n]);
	}
}
