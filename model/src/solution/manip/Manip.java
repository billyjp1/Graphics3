package solution.manip;

import jgl.GL;
import modeler.GLJPanel;
import modeler.scene.Camera;
import modeler.ui.TransformationAttributePanel;
import solution.scene.Transformation;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public abstract class Manip {

	//Constant picking names for the manipulator handle
	public static final int PICK_X = 10;
	public static final int PICK_Y = 11;
	public static final int PICK_Z = 12;
	public static final int PICK_OTHER = 13;
	public static final double BOX_RADIUS = 0.1;

	public static final byte X_AXIS = 0;
	public static final byte Y_AXIS = 1;
	public static final byte Z_AXIS = 2;

	Vector3f e0 = new Vector3f(0,0,0);
	Vector3f eX = new Vector3f(1,0,0);
	Vector3f eY = new Vector3f(0,1,0);
	Vector3f eZ = new Vector3f(0,0,1);

	protected Vector2f pickedMousePoint = new Vector2f(); // the mouseEvent that led to this manip being picked
	protected Transformation t; // transformations this manipulator modifies
	protected Camera c; // camera associated with the active viewport
	public int axisMode; // which axis is currently being dragged

	public Manip() {}
	
	/**
	 * Determines the action of the manipulator when it it dragged.  Responsible 
	 * for computing the change the manipulators transformation from the 
	 * supplied mousePosition and the current mouseDelta.
	 * @param mousePosition
	 * @param mouseDelta
	 */
	public abstract void dragged(Vector2f mousePosition, Vector2f mouseDelta);

	public void nonParallelVector(Vector3f v, Vector3f nonParallel) {
		if (v.x <= v.y && v.x <= v.z) nonParallel.set(1,0,0);
		else if (v.y <= v.x && v.y <= v.z) nonParallel.set(0,1,0);
		else if (v.z <= v.x && v.z <= v.y) nonParallel.set(0,0,1);
	}

	// Computes normalized transformed versions of the standard basis vectors eX, eY, eZ
	// up to and including this transform's parent, but not this transform.  It ignores
	// translation.
	public void normalizedTransformedAxes(Vector3f outX, Vector3f outY, Vector3f outZ) {
		//TODO: Implement this method
		outX.set(1,0,0);
		outY.set(0,1,0);
		outZ.set(0,0,1);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Various methods for controlling manipulators
	////////////////////////////////////////////////////////////////////////////////////////////////////

	public void released() {
		System.out.println("Manip.released");
		axisMode = 0;
		refreshTransformationAttributePanel();
	}

	public void setPickedInfo(int newAxis, Camera camera, Vector2f lastMousePoint) {
		System.out.println("Manip.setPickedInfo");
		this.pickedMousePoint.set(lastMousePoint); 
		c = camera;
		axisMode = newAxis;
	}

	public void setTransformation(Transformation t) {
		this.t = t;
	}

	public void refreshTransformationAttributePanel() {
		if( t.getUserObject() != null ) {
			((TransformationAttributePanel)t.getUserObject()).refresh();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methods for rendering the manipulators
	////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean drawEnabled = true;
	public abstract void glRender(GLJPanel canvas, double scale);

	// gl-rotates the Y axis to the given vector using
	// an arbitrary frame
	private Vector3f ortho1 = new Vector3f();
	private Vector3f ortho2 = new Vector3f();
	public void glRotateTo(GL gl, Vector3f v) {
		nonParallelVector(v, ortho1);
		ortho2.cross(v, ortho1);
		ortho1.cross(ortho2, v);
		ortho1.normalize();
		ortho2.normalize();
		gl.glMultMatrixf(new float[] {
				ortho1.x, ortho1.y, ortho1.z, 0,
				v.x, v.y, v.z, 0,
				ortho2.x, ortho2.y, ortho2.z, 0,
				0,0,0, 1 });
	}

	public void glRenderRotation(GL gl, Transformation t) {
		gl.glRotated(t.R.z, 0, 0, 1);
		gl.glRotated(t.R.y, 0, 1, 0);
		gl.glRotated(t.R.x, 1, 0, 0);
	}

	// openGL-ifies all the rotations that come before the current transformation
	public void glDoOrientation(GL gl, Transformation t) {
		glDoOrientationHelper(gl, t, t);
	}

	public void glDoOrientationHelper(GL gl, Transformation t, Transformation originalT) {
		if (t.getParent() == null) {
			glRenderRotation(gl, t);
		} else {
			glDoOrientationHelper(gl, (Transformation) t.getParent(), originalT);
			if (t == originalT) return;
			glRenderRotation(gl, t);
		}
	}

	Vector3f location = new Vector3f(0,0,0);
	public void glDoLocation(GL gl, Transformation t) {
		t.toWorld(e0, location);
		gl.glTranslatef(location.x, location.y, location.z);
	}

	private double constantSize = 0.3;

	public void renderConstantSize(GLJPanel canvas, Camera camera) {
		if (!drawEnabled) return;
		double scale = camera.getHeight() * constantSize;
		glRender(canvas, scale);
	}

	private static double arrowDivs = 32;
	private static double arrowTailRadius = 0.05;
	private static double arrowHeadRadius = 0.11;

	public static void glRenderArrow(GL gl) {
		glRenderArrow(Y_AXIS, gl);
	}

	public static void glRenderBox(GL gl) {

		double r = BOX_RADIUS;
		gl.glBegin(GL.GL_QUADS);
		{
			gl.glNormal3d(1, 0, 0);
			gl.glVertex3d(r, r, r);
			gl.glVertex3d(r, -r, r);
			gl.glVertex3d(r, -r, -r);
			gl.glVertex3d(r, r, -r);

			gl.glNormal3d(-1, 0, 0);
			gl.glVertex3d(-r, r, -r);
			gl.glVertex3d(-r, -r, -r);
			gl.glVertex3d(-r, -r, r);
			gl.glVertex3d(-r, r, r);

			gl.glNormal3d(0, 1, 0);
			gl.glVertex3d(r, r, r);
			gl.glVertex3d(r, r, -r);
			gl.glVertex3d(-r, r, -r);
			gl.glVertex3d(-r, r, r);

			gl.glNormal3d(0, -1, 0);
			gl.glVertex3d(-r, -r, r);
			gl.glVertex3d(-r, -r, -r);
			gl.glVertex3d(r, -r, -r);
			gl.glVertex3d(r, -r, r);

			gl.glNormal3d(0, 0, 1);
			gl.glVertex3d(r, r, r);
			gl.glVertex3d(-r, r, r);
			gl.glVertex3d(-r, -r, r);
			gl.glVertex3d(r, -r, r);

			gl.glNormal3d(0, 0, -1);
			gl.glVertex3d(r, -r, -r);
			gl.glVertex3d(-r, -r, -r);
			gl.glVertex3d(-r, r, -r);
			gl.glVertex3d(r, r, -r);

		}
		gl.glEnd();
	}

	public static void glRenderArrow(byte axis, GL gl) {

		gl.glPushMatrix();
		gl.glFrontFace(GL.GL_CCW);
		switch (axis) {
		case X_AXIS:
			gl.glRotatef(90f, 0, 0, -1);
			break;
		case Z_AXIS:
			gl.glRotatef(90f, 1, 0, 0);
		}

		// RMP: fix z-buffer issues by breaking up arrow into smaller segments.
		// tail coney
		int numSegments = 20;
		// first segment
		double theta = 0;
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glVertex3d(0d, 0d, 0d);
		for (double i = 0; i <= arrowDivs; ++i) {
			theta = (i / arrowDivs) * Math.PI * 2;
			gl.glVertex3d(Math.cos(theta) * arrowTailRadius/numSegments, 1.8/numSegments, Math.sin(theta) * arrowTailRadius/numSegments);
		}
		gl.glEnd();

		//rest of segments
		for (int segment = 1; segment < numSegments; segment++)
		{
			gl.glBegin(GL.GL_QUAD_STRIP);
			float tailMultiplier = ((float) segment) / numSegments;
			float headMultiplier = ((float) segment + 1) / numSegments;
			for (double i = 0; i <= arrowDivs; ++i) {
				theta = (i / arrowDivs) * Math.PI * 2;
				gl.glVertex3d(Math.cos(theta) * arrowTailRadius*tailMultiplier, 1.8*tailMultiplier, Math.sin(theta) * arrowTailRadius*tailMultiplier);
				gl.glVertex3d(Math.cos(theta) * arrowTailRadius*headMultiplier, 1.8*headMultiplier, Math.sin(theta) * arrowTailRadius*headMultiplier);
			}			
			gl.glEnd();
		}

		// neck ring
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (double i = 0; i <= arrowDivs; ++i) {
			theta = (i / arrowDivs) * Math.PI * 2;
			gl.glVertex3d(Math.cos(theta) * arrowTailRadius, 1.8, Math.sin(theta) * arrowTailRadius);
			gl.glVertex3d(Math.cos(theta) * arrowHeadRadius, 1.83, Math.sin(theta) * arrowHeadRadius);
		}
		gl.glEnd();

		// head coney
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		gl.glVertex3d(0, 2, 0);
		for (double i = 0; i <= arrowDivs; ++i) {
			theta = (i / arrowDivs) * Math.PI * 2;
			gl.glVertex3d(Math.cos(theta) * arrowHeadRadius, 1.83, Math.sin(theta) * arrowHeadRadius);
		}
		gl.glEnd();

		gl.glPopMatrix();
	}

	public static void glRenderCircle(byte axis, GL gl) {
		gl.glPushMatrix();
		switch (axis) {
		case X_AXIS:
			gl.glRotatef(90f, 0, 0, -1);
			break;
		case Z_AXIS:
			gl.glRotatef(90f, 1, 0, 0);
		}

		// neck ring
		gl.glBegin(GL.GL_LINE_LOOP);
		for (double i = 0; i <= arrowDivs; ++i) {
			double theta = (i / arrowDivs) * Math.PI * 2;
			gl.glVertex3d(Math.cos(theta) * 2, 0, Math.sin(theta) * 2);
		}
		gl.glEnd();

		gl.glPopMatrix();
	}

	public void glRenderBoxOnAStick(GL gl) {
		glRenderBoxOnAStick(Y_AXIS, gl);
	}

	public void glRenderBoxOnAStick(byte axis, GL gl) {
		gl.glPushMatrix();
		switch (axis) {
		case X_AXIS:
			gl.glRotatef(90f, 0, 0, -1);
			break;
		case Z_AXIS:
			gl.glRotatef(90f, 1, 0, 0);
		}

		gl.glPushAttrib(GL.GL_CURRENT_BIT);
		gl.glColor4f(1,1,1,1);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0,0,0);
		gl.glVertex3f(0,2,0);
		gl.glEnd();
		gl.glPopAttrib();

		gl.glTranslatef(0,2,0);
		glRenderBox(gl);

		gl.glPopMatrix();
	}

}
