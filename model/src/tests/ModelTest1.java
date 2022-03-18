package tests;

import org.junit.Test;

public class ModelTest1 extends ModelHelpers {

	@Test
	public void testCube() throws InterruptedException {
		runTest("cube");
	}

	@Test
	public void testCubeTranslate() throws InterruptedException {
		runTest("cube-tx");
		runTest("cube-ty");
		runTest("cube-tz");
	}

	@Test
	public void testCubeRotate() throws InterruptedException {
		runTest("cube-rx");
		runTest("cube-ry");
		runTest("cube-rz");
	}

	@Test
	public void testCubeScale() throws InterruptedException {
		runTest("cube-sx");
		runTest("cube-sy");
		runTest("cube-sz");
	}

	@Test
	public void testCubeRST() throws InterruptedException {
		runTest("cube-rst");
	}

	@Test
	public void testDiffuseMaterial() throws InterruptedException {
		runTest("cyan-cube");
		runTest("magenta-cube");
		runTest("yellow-cube");
	}

	@Test
	public void testShinyMaterial() throws InterruptedException {
		runTest("shiny-red-cube");
		runTest("shiny-green-cube");
		runTest("shiny-blue-cube");
		runTest("super-shiny-cube");
	}

	@Test
	public void testMultiple() throws InterruptedException {
		runTest("hi");
	}	

	@Test
	public void testGroup() throws InterruptedException {
		runTest("hi2");
	}	
}
