import static org.junit.Assert.*;

import org.junit.Test;

public class UnitTests2 {

	@Test
	public void test() {

		assertEquals( (UtilityMethods.toObject(int.class, "1")).getClass(), Integer.class);
	
	
	}
	
	@Test
	public void test1() {

		assertEquals( (UtilityMethods.toObject(short.class, "1")).getClass(), Short.class);
	
	
	}

	

}
