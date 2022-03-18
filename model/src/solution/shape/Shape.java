package solution.shape;

import java.io.PrintWriter;
import java.util.Vector;

import org.w3c.dom.Element;

import jgl.GL;
import jgl.GLU;
import modeler.MainFrame;
import modeler.scene.SceneNode;
import modeler.ui.ShapeAttributePanel;

/**
 * @author ags
 */
public abstract class Shape extends SceneNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4910141234477501011L;

	// Global error tolerance determines the subdivision level of all objects
	public transient static float TOLERANCE = 0.01f;

	// Auto-incrementing id for picking purposes.
	private static int nextId = 1000;
	private int pickId = getNextPickId(this);

	// A map from pickIds to Shape objects
	private static Vector<Shape> pickableShapes = new Vector<Shape>();

	// The default material parameters
	protected final float[] diffuseColor = new float[] {0.8f, 0.2f, 0.2f, 1.0f};
	protected final float[] specularColor = new float[] {0.0f, 0, 0, 1.0f};
	protected float expSpecular = 40.0f;

	//The mesh for this shape
	public transient Mesh mesh;

	/**
	 * Basic constructor uses default material and builds the mesh.
	 *
	 */
	public Shape() {

		String classname = this.getClass().getName();
		setName(classname.substring(classname.lastIndexOf('.') + 1, classname.length()));

	}

	/**
	 * Builds a mesh for this shape using the current subdivision levels.
	 */
	public abstract void buildMesh();

	/**
	 * Returns the next available pick ID
	 * @param s
	 * @return
	 */
	private static int getNextPickId(Shape s) {

		// the index of s in pickableShapes is (s.pickId - 1000)
		pickableShapes.add(s);
		return nextId++;

	}

	/**
	 * Gets the shape with the given ID
	 * @param id
	 * @return
	 */
	public static Shape get(int id) {

		int idx = id - 1000;
		if (idx < 0 || idx >= pickableShapes.size())
			return null;

		return (Shape) pickableShapes.get(idx);

	}

	/**
	 * Returns the pick id for this object
	 * @return
	 */
	public int getPickId() {

		return pickId;
	}

	/**
	 * @see modeler.scene.SceneNode#render(javax.media.opengl.GL)
	 */
	public int render(GL gl, GLU glu) {
		// Rebuild the mesh if the refinement changed
		if(mesh == null) {
			buildMesh();
		}

		if(mesh != null) {
			// Set the pick id
			gl.glLoadName(pickId);

			// TODO - fill in code to set up the material parameters
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, diffuseColor);
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specularColor);
			gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, expSpecular);

			mesh.render(gl);
			return mesh.numTriangles;
		}
		else {
			return 0;
		}
	}

	/**
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {

		return false;

	}

	/**
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	public boolean isLeaf() {

		return true;

	}

	/**
	 * Quick casting version of cosine
	 * @param d
	 * @return
	 */
	protected float cos(double d) {

		return (float) Math.cos(d);
	}

	/**
	 * Quick casting version of sine
	 * @param d
	 * @return
	 */
	protected float sin(double d) {

		return (float) Math.sin(d);
	}

	public float[] getDiffuseColor() {
		return diffuseColor;
	}

	public float getExpSpecular() {
		return expSpecular;
	}

	public void setExpSpecular(float expSpecular) {
		this.expSpecular = expSpecular;
	}

	public float[] getSpecularColor() {
		return specularColor;
	}

	@Override
	protected void readXMLData(Element node) {
		// TODO Auto-generated method stub
		String[] t = node.getAttribute("diffuseColor").split(" ");
		for (int i = 0; i < t.length; i++) {
			diffuseColor[i] = Float.parseFloat(t[i]);
		}
		t = node.getAttribute("specularColor").split(" ");
		for (int i = 0; i < t.length; i++) {
			specularColor[i] = Float.parseFloat(t[i]);
		}
		expSpecular = Float.parseFloat(node.getAttribute("exponent"));
	}

	@Override
	protected void exportMainData(PrintWriter pw, String indent) {
		String attrs = "name=\"" + name + "\"" +
				" diffuseColor=\"" + diffuseColor[0] + " " + diffuseColor[1] + " " + diffuseColor[2] + "\"" + 
				" specularColor=\"" + specularColor[0] + " " + specularColor[1] + " " + specularColor[2] + "\"" + 
				" exponent=\"" + expSpecular + "\""; 

		String c = getClass().getCanonicalName();
		if (this.getChildCount()==0) {
			pw.println(indent + "<" + c + " " + attrs + "/>");
		} else {
			pw.println(indent + "<" + c + " " + attrs + ">");
			super.exportMainData(pw, indent + "  ");
			pw.println(indent + "</" + c + ">");
		}
	}

	public void setupAttributePanels(MainFrame mf) {
		this.setUserObject(new ShapeAttributePanel(mf, this));
		// do for children
		super.setupAttributePanels(mf);

	}

}