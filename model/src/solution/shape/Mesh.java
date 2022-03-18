package solution.shape;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jgl.GL;

/**
 * Basic packed triangle mesh. The triangle mesh is primarily data storage, all
 * geometric interaction is handled through MeshTriangle objects.
 *
 * @author arbree Aug 19, 2005 TriangleMesh.java Copyright 2005 Program of
 *         Computer Graphics, Cornell University
 */
public class Mesh {

	/** The number of vertices in the mesh * */
	protected int numVertices;

	/** The number of triangles in the mesh * */
	protected int numTriangles;

	/** The vertex array -- always present in each mesh * */
	protected FloatBuffer verts;

	/** The normal coordinate array -- may be null * */
	protected FloatBuffer normals;

	/** Mesh triangle objects for each triangle. */
	protected IntBuffer triangles = null;

	/**
	 * Default constructor creates an empty mesh
	 */
	public Mesh() { }

	/**
	 * Basic constructor. Sets mesh data array into the mesh structure. IMPORTANT:
	 * The data array are not copies so changes to the input data array will
	 * affect the mesh structure. The number of vertices and the number of
	 * triangles are inferred from the lengths of the verts and tris array. If
	 * either is not a multiple of three, an error is thrown.
	 *
	 * @param verts the vertex data
	 * @param tris the triangle data
	 * @param normals the normal data
	 */
	public Mesh(float[] verts, int[] tris, float[] normals) {

		// check the inputs
		if (verts.length % 3 != 0)
			throw new Error("Vertex array for a triangle mesh is not a multiple of 3.");
		if (tris.length % 3 != 0)
			throw new Error("Triangle array for a triangle mesh is not a multiple of 3.");

		// Set data
		setMeshData(verts, tris, normals);

	}

	/**
	 * @return Returns the normals.
	 */
	public FloatBuffer getNormals() {

		return normals;

	}

	/**
	 * @return Returns the verts.
	 */
	public FloatBuffer getVerts() {

		return verts;
	}

	/**
	 * Sets the mesh data and builds the triangle array.
	 * @param verts the vertices
	 * @param tris the triangles
	 * @param normals the normals
	 */
	private void setMeshData(float[] verts, int[] tris, float[] normals) {

		this.verts = FloatBuffer.allocate(verts.length);
		this.triangles = IntBuffer.allocate(tris.length);
		this.normals = FloatBuffer.allocate(normals.length);

		this.numVertices = verts.length / 3;
		this.numTriangles = tris.length / 3;
		this.verts.put(verts);
		this.triangles.put(tris);
		this.normals.put(normals);

	}

	/**
	 * Returns the number of triangles
	 *
	 * @return the number of triangles
	 */
	public int getNumTriangles() {

		return numTriangles;

	}

	/**
	 * Returns the number of vertices
	 *
	 * @return the number of vertices
	 */
	public int getNumVertices() {

		return numVertices;

	}

	/**
	 * Draws this mesh
	 * @param glD
	 */
	public void render(GL gl) {
		verts.rewind();
		normals.rewind();
		triangles.rewind();

		// TODO: draw the mesh using OpenGL calls
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glBegin(GL.GL_TRIANGLES);
			for (int i=0;i<getNumTriangles();i++) {
				int p1 = triangles.get(i*3);
				int p2 = triangles.get(i*3+1);
				int p3 = triangles.get(i*3+2);
				gl.glNormal3f(normals.get(p1*3), normals.get(p1*3+1), normals.get(p1*3+2));
				gl.glVertex3f(verts.get(p1*3), verts.get(p1*3+1), verts.get(p1*3+2));
				gl.glNormal3f(normals.get(p2*3), normals.get(p2*3+1), normals.get(p2*3+2));
				gl.glVertex3f(verts.get(p2*3), verts.get(p2*3+1), verts.get(p2*3+2));
				gl.glNormal3f(normals.get(p3*3), normals.get(p3*3+1), normals.get(p3*3+2));
				gl.glVertex3f(verts.get(p3*3), verts.get(p3*3+1), verts.get(p3*3+2));
			}
		gl.glEnd();
		gl.glDisable(GL.GL_CULL_FACE);
		gl.glFlush();
	}


}
