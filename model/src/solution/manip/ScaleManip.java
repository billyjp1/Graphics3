package solution.manip;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import jgl.GL;
import modeler.GLJPanel;
import solution.scene.Transformation;

public class ScaleManip extends Manip {

	public ScaleManip() {}
	
	public ScaleManip(Transformation t) {

		this.t = t;
	}

	public void dragged(Vector2f mousePosition, Vector2f mouseDelta) {
		//TODO: Implement this method
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Render the manipulator
	////////////////////////////////////////////////////////////////////////////////////////////////////

	Vector3f skewX = new Vector3f();
	Vector3f skewY = new Vector3f();
	Vector3f skewZ = new Vector3f();
	public void glRender(GLJPanel canvas, double scale) {

		GL gl = canvas.getGL();
		//		GL2 gl = d.getGL().getGL2();
		gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT);

		gl.glPushAttrib(GL.GL_LIGHTING_BIT);
		gl.glDisable(GL.GL_LIGHTING);

		gl.glPushAttrib(GL.GL_DEPTH_BUFFER_BIT);
		gl.glDisable(GL.GL_DEPTH_TEST);

		gl.glPushMatrix();
		glDoLocation(gl, t);

		gl.glScaled(scale, scale, scale);
		glRenderRotation(gl, t);

		normalizedTransformedAxes(skewX, skewY, skewZ);

		gl.glPushMatrix();
		glRotateTo(gl,skewX);
		gl.glColor4d(0.8, 0, 0, 1);
		gl.glLoadName(PICK_X);
		glRenderBoxOnAStick(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		glRotateTo(gl,skewY);
		gl.glColor4d(0, 0.8, 0, 1);
		gl.glLoadName(PICK_Y);
		glRenderBoxOnAStick(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		glRotateTo(gl,skewZ);
		gl.glColor4d(0, 0, 0.8, 1);
		gl.glLoadName(PICK_Z);
		glRenderBoxOnAStick(gl);
		gl.glPopMatrix();    

		gl.glLoadName(PICK_OTHER);
		gl.glColor4d(0.8, 0.8, 0, 1);
		glRenderBox(gl);

		gl.glPopMatrix();

		gl.glPopAttrib();
		gl.glPopAttrib();
		gl.glPopAttrib();
	}

}