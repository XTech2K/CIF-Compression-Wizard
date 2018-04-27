package test;

import java.util.ArrayList;

/**
 * Use: Write a class that implements Testable. Implement the method runAll() to call all the testing methods of that
 * class. Add an instance of that class to the ArrayList below.
 */
public class TestDriver
{
	public static void main(String[] args)
	{

		ArrayList<Testable> tests = new ArrayList<>();

		//copy this, changing ExampleTest to your class name
		//tests.add(new ExampleTest());

		tests.add(new ControllerTest());

		for(Testable t : tests)
		{
			t.runAll();
		}
	}
}
