package solution.scene;

import java.io.PrintWriter;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import org.w3c.dom.Element;

import jgl.GL;
import jgl.GLU;
import modeler.MainFrame;
import modeler.scene.SceneNode;
import modeler.ui.TransformationAttributePanel;

/**
 * @author andru Transformation provides an interface to a 3D transformation
 *         matrix. It allows independent access to the translational,
 *         rotational, and scaling properties of the matrix. By convention,
 *         rotations are performed around the axes in X,Y,Z order.
 */

public class Transformation extends SceneNode {

	private static final long serialVersionUID = 3256437006337718072L;

	// Individual Translation, Rotation, and Scaling components of this transformation
	public final Vector3f R = new Vector3f(0, 0, 0); // Stored in degrees
	public final Vector3f S = new Vector3f(1, 1, 1);
	public final Vector3f T = new Vector3f(0, 0, 0);

	//Constants defining the coordinate axes
	public static final Vector3f xAxis = new Vector3f(1, 0, 0);
	public static final Vector3f yAxis = new Vector3f(0, 1, 0);
	public static final Vector3f zAxis = new Vector3f(0, 0, 1);

	/**
	 * Required for IO
	 */
	public Transformation() {}

	/**
	 * Constructor
	 * @param name
	 */
	public Transformation(String name) {

		this.name = name;
	}

	/**
	 * Copy constructor
	 * @param copy
	 */
	public Transformation(Transformation copy) {

		this.R.set(copy.R);
		this.S.set(copy.S);
		this.T.set(copy.T);
	}

	/**
	 * Converts the given point v from local space to world space 
	 */
	public void toWorld(Vector3f v, Vector3f outv) {
		Vector3f result = new Vector3f();
		transform(v, result);
		if (parent == null) {
			outv.set(result);
		} else {
			((Transformation)parent).toWorld(result, outv);
		}
	}

	/**
	 * Transforms the given point by this transformation
	 */
	Matrix3f tempMatrix = new Matrix3f();
	public void transform(Vector3f v, Vector3f outv) {
		outv.set(v);

		// Scale
		outv.set(outv.x * S.x, outv.y * S.y, outv.z * S.z);

		// Rotate
		tempMatrix.rotX((float)Math.toRadians(R.x));
		tempMatrix.transform(outv);
		tempMatrix.rotY((float)Math.toRadians(R.y));
		tempMatrix.transform(outv);
		tempMatrix.rotZ((float)Math.toRadians(R.z));
		tempMatrix.transform(outv);

		// Translate
		outv.add(T);
	}

	/**
	 * Sets the translation vector
	 * @param translation
	 */
	public void setTranslate(Vector3f translation) {

		T.set(translation);
	}

	/**
	 * Sets the scale vector
	 * @param scaling
	 */
	public void setScale(Vector3f scaling) {

		S.set(scaling);
	}

	/**
	 * Sets the rotation angle in degrees
	 * @param rotation
	 */
	public void setRotate(Vector3f rotation) {

		R.set(rotation);
	}

	/**
	 * Gets the translation vector
	 * @return
	 */
	public Vector3f getTranslate() {

		return new Vector3f(T);
	}

	/**
	 * Gets the scale vector
	 * @return
	 */
	public Vector3f getScale() {

		return new Vector3f(S);
	}

	/**
	 * Gets the rotation vector in degrees
	 * @return
	 */
	public Vector3f getRotate() {

		return new Vector3f(R);
	}

	/**
	 * @see modeler.scene.SceneNode#render(javax.media.opengl.GL)
	 */
	@Override
	public int render(GL gl, GLU glu) {
		int outCount = 0;
		gl.glPushMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		Vector3f tV = getTranslate();
		Vector3f rV = getRotate();
		Vector3f sV = getScale();
		
		gl.glTranslatef(tV.x, tV.y, tV.z);
	
		gl.glRotatef(rV.x, 1, 0, 0);
		gl.glRotatef(rV.z, 0, 0, 1);
		gl.glRotatef(rV.y, 0, 1, 0);
		
		gl.glScalef(sV.x, sV.y, sV.z);
		
		// TODO - fill in code before and after super.render to render children correctly
		outCount = super.render(gl, glu);
		gl.glPopMatrix();
		
		return outCount;
	}

	/**
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {

		return true;

	}

	/**
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	public boolean isLeaf() {

		return false;

	}

	public Vector3f getR() {
		return R;
	}

	public Vector3f getS() {
		return S;
	}

	public Vector3f getT() {
		return T;
	}

	protected void exportMainData(PrintWriter pw, String indent) {
		//Write our Transformation out
		String attrs = "name=\"" + name + "\"" +
				" T=\"" + T.x + " " + T.y + " " + T.z + "\"" + 
				" R=\"" + R.x + " " + R.y + " " + R.z + "\"" + 
				" S=\"" + S.x + " " + S.y + " " + S.z + "\"";
		String c = getClass().getCanonicalName();

		if (this.getChildCount()==0) {
			pw.println(indent + "<" + c + " " + attrs + "/>");
		} else {
			pw.println(indent + "<" + c + " " + attrs + ">");
			super.exportMainData(pw, indent + "  ");
			pw.println(indent + "</" + c + ">");
		}
	}

	@Override
	protected void readXMLData(Element node) {
		String[] t = node.getAttribute("T").split(" ");
		T.set(Float.parseFloat(t[0]), Float.parseFloat(t[1]), Float.parseFloat(t[2]));
		t = node.getAttribute("R").split(" ");
		R.set(Float.parseFloat(t[0]), Float.parseFloat(t[1]), Float.parseFloat(t[2]));
		t = node.getAttribute("S").split(" ");
		S.set(Float.parseFloat(t[0]), Float.parseFloat(t[1]), Float.parseFloat(t[2]));

	}

	public void setupAttributePanels(MainFrame mf) {
		this.setUserObject(new TransformationAttributePanel(mf, this));
		// do for children
		super.setupAttributePanels(mf);
	}
}
