package solution.manip;


import javax.vecmath.*;

import jgl.GL;
import modeler.GLJPanel;
import solution.scene.Transformation;

public class RotateManip extends Manip {

	public RotateManip() {} 
	
	public RotateManip(Transformation t) {

		this.t = t;
	}

	public void dragged(Vector2f mousePosition, Vector2f mouseDelta) {
		//TODO: Implement this method
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Renders the rotate manipulator
	////////////////////////////////////////////////////////////////////////////////////////////////////

	public void toggleCircleDepth() {

		if(axisMode == PICK_OTHER)
			circleDepth = !circleDepth;

	}

	Vector3f skewX = new Vector3f();
	Vector3f skewY = new Vector3f();
	Vector3f skewZ = new Vector3f();
	protected boolean circleDepth = true;
	public void glRender(GLJPanel canvas, double scale) {

		GL gl = canvas.getGL();
		gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT);

		gl.glPushAttrib(GL.GL_LIGHTING_BIT);
		gl.glDisable(GL.GL_LIGHTING);

		gl.glPushAttrib(GL.GL_DEPTH_BUFFER_BIT);
		gl.glDisable(GL.GL_DEPTH_TEST);

		gl.glLineWidth(3);

		gl.glPushMatrix();
		glDoLocation(gl, t);

		gl.glScaled(scale, scale, scale);

		gl.glLoadName(PICK_OTHER);
		gl.glColor4d(0.8, 0.8, 0, 1);
		glRenderBox(gl);

		if(circleDepth )
			gl.glEnable(GL.GL_DEPTH_TEST);

		normalizedTransformedAxes(skewX, skewY, skewZ);

		gl.glPushMatrix();
		gl.glRotatef(t.R.z, skewZ.x, skewZ.y, skewZ.z);
		gl.glRotatef(t.R.y, skewY.x, skewY.y, skewY.z);
		glRotateTo(gl,skewX);
		gl.glColor4d(0.8, 0, 0, 1);
		gl.glLoadName(PICK_X);
		glRenderCircle(Y_AXIS, gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glRotatef(t.R.z, skewZ.x, skewZ.y, skewZ.z);
		glRotateTo(gl,skewY);
		gl.glColor4d(0, 0.8, 0, 1);
		gl.glLoadName(PICK_Y);
		glRenderCircle(Y_AXIS, gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		glRotateTo(gl,skewZ);
		gl.glColor4d(0, 0, 0.8, 1);
		gl.glLoadName(PICK_Z);
		glRenderCircle(Y_AXIS, gl);
		gl.glPopMatrix();    

		gl.glEnable(GL.GL_DEPTH_TEST);

		gl.glPopMatrix();

		gl.glLineWidth(1);

		gl.glPopAttrib();
		gl.glPopAttrib();
		gl.glPopAttrib();

	}

}