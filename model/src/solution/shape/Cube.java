package solution.shape;

import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3f;

/**
 * Models a cube.  The mesh for a cube is derived from static data.  The vertices
 * for each face must be replicated to avoid smoothing the corners for the cube.
 * @author ags
 */
public class Cube extends Shape {

	private static final long serialVersionUID = 3256437006337718072L;

	/* The eight vertices of the cube. n = negative, p = positive */
	private static final Point3f nnn = new Point3f(-1.0f, -1.0f, -1.0f);
	private static final Point3f nnp = new Point3f(-1.0f, -1.0f, +1.0f);
	private static final Point3f npn = new Point3f(-1.0f, +1.0f, -1.0f);
	private static final Point3f npp = new Point3f(-1.0f, +1.0f, +1.0f);
	private static final Point3f pnn = new Point3f(+1.0f, -1.0f, -1.0f);
	private static final Point3f pnp = new Point3f(+1.0f, -1.0f, +1.0f);
	private static final Point3f ppn = new Point3f(+1.0f, +1.0f, -1.0f);
	private static final Point3f ppp = new Point3f(+1.0f, +1.0f, +1.0f);

	/* Normals for the different faces of the cube */
	private static final Vector3f lNormal = new Vector3f(-1, 0, 0);
	private static final Vector3f rNormal = new Vector3f(+1, 0, 0);
	private static final Vector3f dNormal = new Vector3f(0, -1, 0);
	private static final Vector3f uNormal = new Vector3f(0, +1, 0);
	private static final Vector3f bNormal = new Vector3f(0, 0, -1);
	private static final Vector3f fNormal = new Vector3f(0, 0, +1);

	// Vertex matrix
	private static final Point3f[] vertices = new Point3f[] { 
			ppp, npp, nnp, pnp, // Front face
			ppn, pnn, nnn, npn, // Back face
			ppn, ppp, pnp, pnn, // Right face
			npp, npn, nnn, nnp, // Left face
			ppp, ppn, npn, npp, // Up face
			pnp, nnp, nnn, pnn  // Down face
	};

	// Normal matrix
	private static final Vector3f[] normals = new Vector3f[] { 
			fNormal, fNormal, fNormal, fNormal,
			bNormal, bNormal, bNormal, bNormal, 
			rNormal, rNormal, rNormal, rNormal, 
			lNormal, lNormal, lNormal, lNormal, 
			uNormal, uNormal, uNormal, uNormal, 
			dNormal, dNormal, dNormal, dNormal
	};

	private static final Point3i[] triangles = new Point3i[] { 
			new Point3i(0, 1, 2),
			new Point3i(2, 3, 0), 
			new Point3i(4, 5, 6), 
			new Point3i(6, 7, 4), 
			new Point3i(8, 9, 10), 
			new Point3i(10, 11, 8),
			new Point3i(12, 13, 14), 
			new Point3i(14, 15, 12), 
			new Point3i(16, 17, 18),
			new Point3i(18, 19, 16),
			new Point3i(20, 21, 22), 
			new Point3i(22, 23, 20) 
	};

	//The static mesh for the cube
	private transient static Mesh cubeMesh;

	/**
	 * Required for IO
	 */
	public Cube() {}

	/**
	 * @see solution.shape.Shape#buildMesh()
	 */
	public void buildMesh() {
		//Do nothing if the mesh has already been built
		if(cubeMesh != null) {
			mesh = cubeMesh;
			return;
		}

		//Build the mesh data arrays from the static mesh data
		int vertLength = vertices.length;
		float[] vertData = new float[3*vertLength];
		float[] normData = new float[3*vertLength];

		//Create the triangle list
		int[] triData = new int[3*triangles.length];

		//Copy the vertex data
		for(int i = 0; i < vertLength; i++) {

			vertData[3*i] = vertices[i].x;
			vertData[3*i+1] = vertices[i].y;
			vertData[3*i+2] = vertices[i].z;
			normData[3*i] = normals[i].x;
			normData[3*i+1] = normals[i].y;
			normData[3*i+2] = normals[i].z;

		}

		//Copy the triangle data
		for(int i = 0; i < triangles.length; i++) {

			triData[3*i] = triangles[i].x;
			triData[3*i+1] = triangles[i].y;
			triData[3*i+2] = triangles[i].z;

		}

		//Create the mesh
		cubeMesh = new Mesh(vertData, triData, normData);
		mesh = cubeMesh;

	}

}
