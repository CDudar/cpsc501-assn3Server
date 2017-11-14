/*==========================================================================
File: Asst2TestDriver.java
Purpose: Driver program that loads the objects inspector and runs the
         tests. Verification of tests is done through the inspection
         of the out from the object inspector loaded at run time.

Location: University of Calgary, Alberta, Canada
Created By: Jordan Kidney
Created on:  Oct 23, 2005
Last Updated: Oct 23, 2005
========================================================================*/



import java.lang.reflect.*;
import java.util.Vector;

public class Asst2TestDriver

{
    
    //-------------------------------------------------------------------
    public Asst2TestDriver(String ObjInspectorName, boolean recursive)
	throws Exception
    {
	this.recursive=recursive;
	setObjectInspectorInfo(ObjInspectorName);
    }
    //--------------------------------------------------------------------
    public void setObjectInspectorInfo(String ObjectInspectorName)
	throws Exception
    {
	Class objInspectClass=null;
	try
	    {
		System.out.println("good");
		objInspectClass = Class.forName(ObjectInspectorName);

		ObjInspector = objInspectClass.newInstance();
	    }
	catch(Exception e) 
	    {
		throw new Exception("Unable create instance of your object inspector");
	    }

	// get reference to inspect method
	try
	    {
		Class[] param = { Object.class, boolean.class };
		inspectionMethod = objInspectClass.getDeclaredMethod("inspect",param);
	    }
	catch(Exception e) 
	    {
		throw new Exception("Unable to find required method: public void inspect(Object obj,boolean recursive)");
	    }
    }
    //--------------------------------------------------------------------
    public void runTest(Object testObj) throws Exception
    {
	try
	    {
		System.out.println("======================================================");
		System.out.println("Running Test: " + testObj);
		Object[] param = { testObj, new Boolean(recursive) };
		inspectionMethod.invoke(ObjInspector, param); 
		System.out.println("======================================================");
	    }
	catch(Exception e)
	    {
		
		e.printStackTrace();
		throw new Exception("unable to completely run test");
	    
	    }
    }
    //------- Fields -----------------------------------------------------
    private Object ObjInspector = null;
    private Method inspectionMethod =null;
    private boolean recursive=false;
    
    
				


}
