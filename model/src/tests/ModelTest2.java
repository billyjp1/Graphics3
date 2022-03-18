package tests;

import org.junit.Test;


public class ModelTest2 extends ModelHelpers {
	@Test
	public void testCylinder() throws InterruptedException {
		runTest("cylinder-gray");
		runTest("cylinder-red");
		runTest("cylinder-reverse");
	}

	@Test
	public void testCylinderWire() throws InterruptedException {
		runTest("cylinder-wire");
	}

	@Test
	public void testCylinderTolerance() throws InterruptedException {
		for (int numPolygons : new int[] {20, 28, 36, 124, 196, 284}) {			
			runTest("cylinder-" + numPolygons);
		}
	}

	@Test
	public void testSphere() throws InterruptedException {
		runTest("sphere-gray");
		runTest("sphere-red");
		runTest("sphere-reverse");
	}

	@Test
	public void testSphereWire() throws InterruptedException {
		runTest("sphere-wire");
	}

	@Test
	public void testSphereTolerance() throws InterruptedException {
		for (int numPolygons : new int[] {36, 64, 100, 400, 1024, 2500}) {			
			runTest("sphere-" + numPolygons);
		}
	}
	
}
