package tests;

import javax.vecmath.Vector2f;

import org.junit.Test;

import modeler.MainFrame;
import solution.manip.Manip;
import solution.manip.RotateManip;
import solution.manip.ScaleManip;
import solution.manip.TranslateManip;


public class ModelTest3 extends ModelHelpers {

	@Test
	public void testTranslateManipX() throws InterruptedException {
		MainFrame m = setupManip(
				TranslateManip.class,
				Manip.PICK_X, 
				new Vector2f(0.33f, -0.33f), 
				new Vector2f(.1f, -.1f)
		);
		compareImages(m, "translate-manip-x");
	}

	@Test
	public void testTranslateManipY() throws InterruptedException {
		MainFrame m = setupManip(
				TranslateManip.class,
				Manip.PICK_Y, 
				new Vector2f(0.00f, 0.67f), 
				new Vector2f(0f, .2f)
		);
		compareImages(m, "translate-manip-y");
	}
	
	@Test
	public void testTranslateManipZ() throws InterruptedException {
		MainFrame m = setupManip(
				TranslateManip.class,
				Manip.PICK_Z,
				new Vector2f(-.67f, 0.00f), 
				new Vector2f(-.3f, -.1f)
		);
		compareImages(m, "translate-manip-z");
	}
	
	@Test
	public void testTranslateManipO() throws InterruptedException {
		MainFrame m = setupManip(
				TranslateManip.class,
				Manip.PICK_OTHER, 
				new Vector2f(0.00f, 0.00f), 
				new Vector2f(-.3f, .3f)
		);
		compareImages(m, "translate-manip-o");
	}
	
	@Test
	public void testScaleManipX() throws InterruptedException {
		MainFrame m = setupManip(
				ScaleManip.class,
				Manip.PICK_X, 
				new Vector2f(0.33f, -0.33f), 
				new Vector2f(.1f, -.1f)
		);
		compareImages(m, "scale-manip-x");
	}

	@Test
	public void testScaleManipY() throws InterruptedException {
		MainFrame m = setupManip(
				ScaleManip.class,
				Manip.PICK_Y, 
				new Vector2f(0.00f, 0.67f), 
				new Vector2f(0f, .2f)
		);
		compareImages(m, "scale-manip-y");
	}
	
	@Test
	public void testScaleManipZ() throws InterruptedException {
		MainFrame m = setupManip(
				ScaleManip.class,
				Manip.PICK_Z, 
				new Vector2f(-.67f, 0.00f), 
				new Vector2f(-.3f, -.1f)
		);
		compareImages(m, "scale-manip-z");
	}

	@Test
	public void testScaleManipO() throws InterruptedException {
		MainFrame m = setupManip(
				ScaleManip.class,
				Manip.PICK_OTHER, 
				new Vector2f(0.00f, 0.00f), 
				new Vector2f(-.3f, .3f)
		);
		compareImages(m, "scale-manip-o");
	}

	@Test
	public void testRotateManipX() throws InterruptedException {
		MainFrame m = setupManip(
				RotateManip.class,
				Manip.PICK_X,
				new Vector2f(0.36f, 0.5f), 
				new Vector2f(.05f, -.05f)
		);
		compareImages(m, "rotate-manip-x");				
	}

	@Test
	public void testRotateManipY() throws InterruptedException {
		MainFrame m = setupManip(
				RotateManip.class,
				Manip.PICK_Y,
				new Vector2f(0.5f, -.3f), 
				new Vector2f(.05f, .03f)
		);
		compareImages(m, "rotate-manip-y");				
	}

	@Test
	public void testRotateManipZ() throws InterruptedException {
		MainFrame m = setupManip(
				RotateManip.class,
				Manip.PICK_Z,
				new Vector2f(0.33f, -.11f), 
				new Vector2f(-.02f, .05f)
		);
		compareImages(m, "rotate-manip-z");				
	}

}
