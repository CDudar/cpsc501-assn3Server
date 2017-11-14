

/** CPSC 501 ASSIGNMENT 2
 * 	Christian Dudar
 */



/** BELOW DISCLAIMER WAS INCLUDED IN ZIP FILE WE WERE GIVEN


/*==========================================================================
File: ObjectInspector.java
Purpose:Demo Object inspector for the Asst2TestDriver

Location: University of Calgary, Alberta, Canada
Created By: Jordan Kidney
Created on:  Oct 23, 2005
Last Updated: Oct 23, 2005

***********************************************************************
If you are going to reproduce this code in any way for your assignment 
remember to include my name at the top of the file to indicate where you
got the original code from
***********************************************************************


========================================================================*/




import java.util.*;
import java.lang.reflect.*;


public class ObjectInspector
{
	
	//hashmap to keep track of objects that have already been inspected
	HashMap<Object, Integer> ObjectsInspected = new HashMap<Object, Integer>();
	
    public ObjectInspector() { }

    //-----------------------------------------------------------
    public void inspect(Object obj, boolean recursive)
    {
	Vector objectsToInspect = new Vector();
	
	Class ObjClass;
	
	if(obj == null) {
		System.out.println("Object to be inspected is null");
		return;
	}
	
	
	ObjClass = obj.getClass();


	//Update hashmap to hold this object so it does not get inspected twice
	
	ObjectsInspected.put(obj, 1);

	

	System.out.println("inside inspector: " + obj + " (recursive = "+recursive+")");
	
	System.out.println("------------");
	
	
	
	if(ObjClass.getSuperclass() != null){
		System.out.println("Immediate SuperClass: " + ObjClass.getSuperclass());	
	}
	
	else{
		System.out.println("This object has no superclass");
	}
	

	
	
	
	//inspect the current classes various components

	inspectConstructors(obj, ObjClass, objectsToInspect);
	inspectFields(obj, ObjClass,objectsToInspect);
	inspectMethods(obj, ObjClass, objectsToInspect);	
	inspectInterfaces(obj, ObjClass, objectsToInspect);	
	
	System.out.println(objectsToInspect.size() + " objects found");

	System.out.println(objectsToInspect);

		
	System.out.println("\n\n\n");
	
	
	//If recursive is true, inspect the objects found in fields
	if(recursive)
	    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
	   
    }
    
    //-----------------------------------------------------------
    
    
    
    
    
    
    //-----------------------------------------------------------
    private void inspectFieldClasses(Object obj,Class ObjClass,
				     Vector objectsToInspect,boolean recursive)
    {
	
	if(objectsToInspect.size() > 0 )
	    System.out.println("---- Inspecting Field Classes ----");
	
	
	//Cycle through the objects to inspect
	
	Enumeration e = objectsToInspect.elements();
	while(e.hasMoreElements())
	    {
		
		
		Field f = null;
		Object fieldObject = null;
		try{
			f = (Field) e.nextElement();
			fieldObject = f.get(obj);
		}
		catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Inspecting Field: " + f.getName() );
		
		try
		    {
			
				//If object has already been inspected ignore it.
			
				if(!ObjectsInspected.containsKey(fieldObject)) {
				

					if(fieldObject == null) {
						System.out.println("Object to be inspected is null");
						return;
					}
					
					
					//If object is array, must traverse through array
					
					if(fieldObject.getClass().isArray()) {
						
						Class<?> componentType = (fieldObject.getClass().getComponentType());
						
						
							//If array is primitive, print out contents normally
							if(componentType.isPrimitive()) {
								
								
								System.out.println("=====Inspecting Primitve Array:=====");
								
								System.out.println("Component Type: " + fieldObject.getClass().getComponentType());
								
								System.out.println("Length: " + Array.getLength(fieldObject));
								
								for(int i = 0; i < Array.getLength(fieldObject); i++) {
									
									System.out.println("Index: " + i + " Value = " + (Array.get(fieldObject, i)));
								}
								
								
								//If array holds objects, recursively inspect the inner objects
							}
							else {
								System.out.println("=====Recursing on array of objects=====");
								
								System.out.println("Component Type: " + fieldObject.getClass().getComponentType());
								
								System.out.println("Length: " + Array.getLength(fieldObject));
								
								for(int i = 0; i < Array.getLength(fieldObject); i++) {
									System.out.println("====Index: " + i + "=====");
									System.out.println("Contents (full inspection):");
									Object cObj = Array.get(fieldObject, i);
									inspect(cObj, recursive);

									
								}
								
								
							}
							
							System.out.println("\n\n\n\n");
						
						
						
					}
					//Simply inspect the object if it is not an array
						else {
						
					System.out.println("******************");
					inspect( fieldObject , recursive);
					System.out.println("DONE THIS BRANCH");
					
					System.out.println("******************");
							}
				}
				else {
					System.out.println("OBJECT ALREADY INSPECTED, SKIPPING" + fieldObject);
				}
				
			}
		catch(Exception exp) { exp.printStackTrace(); }
	    }
    }
    
    
    //-----------------------------------------------------------
    private void inspectInterfaces(Object obj, Class ObjClass, Vector objectsToInspect)
    {
    	
    	System.out.println("=====Interfaces for: " + ObjClass.getName() + "=====");
    	
    	
    	//Get interfaces
    	if(ObjClass.getInterfaces() != null )
    	{
    	
    		Class[] interfaces = ObjClass.getInterfaces();
    		

    		
    			for(int i = 0; i < interfaces.length; i++)
    			{
    				
    				Class inter = interfaces[i];
    				System.out.printf("Interfaces: " + inter.getName());
    				
    			}
    		System.out.printf("\n");	
    			
    			
    	}
    	else{
    		System.out.println("No interfaces");
    		
    	}
    	
    	System.out.println("------Done Interfaces------");
    	
    	/**
    	//Ascend and print hierarchy of interfaces
    	if(ObjClass.getSuperclass() != null) {
    		inspectInterfaces(obj, ObjClass.getSuperclass(), objectsToInspect);
    		
    	}
    	*/
    
    
    }
    	
    	
    	
    
    
    
    static String testString = "";
    
    //-----------------------------------------------------------
    private void inspectConstructors(Object obj, Class ObjClass, Vector objectsToInspect)
    {
    	
    	
    	System.out.println("=====Constructors for: " + ObjClass.getName() + "=====");
    	testString += "=====Constructors for: " + ObjClass.getName() + "=====\n";
    	
    	
    	//Get constructors
    	if(ObjClass.getConstructors().length >= 1)
    	{
    		
    		Constructor[] constructors  = ObjClass.getDeclaredConstructors();
    		
    		for(int i = 0; i < constructors.length; i++)
    		{
    			Constructor constructor = constructors[i];
    			Class[] parameterTypes = constructor.getParameterTypes();
    			
    			String modifiers = Modifier.toString(constructor.getModifiers());
    			
    			System.out.printf("------Constructor------"
    					+ "\nModifier(s): " + modifiers + "\n");
    			
    			testString += "------Constructor------" + "\nModifier(s): " + modifiers + "\n";
    			
    			System.out.printf("Parameters: ");
    			for(Class parameter:parameterTypes)
    			{
    				System.out.printf(parameter.getName() + ", ");
    				testString += parameter.getName() + ", ";
    			}
    			System.out.println();
    			testString += "\n";
    			
    			
    		}
    		
    		
    		
    	}

    	System.out.println("------Done Constructors------");
    	testString += "------Done Constructors------\n";
    	
    	
    	//Ascend and print hierarchy of constructors
    	if(ObjClass.getSuperclass() != null) {
    		inspectConstructors(obj, ObjClass.getSuperclass(), objectsToInspect);
    		
    	}
    	
    	

    	
    }
    
    private void inspectMethods(Object obj, Class ObjClass, Vector objectsToInspect)
    {
    	
    	System.out.println("=====Methods for: " + ObjClass.getName() + "=====");
    	
    	//Get methods info
    	if(ObjClass.getDeclaredMethods().length >= 1)
    	{
    		Method[] methods = ObjClass.getDeclaredMethods();
    		
    			for(int i = 0; i < methods.length; i++)
    			{
    				
    				System.out.println("------Method------");
    				
    				Method method = methods[i];
    				System.out.println("Name " + method.getName());
    				
    				Class[] exceptions = method.getExceptionTypes();
    				Class[] parameterTypes = method.getParameterTypes();
    				String modifiers = Modifier.toString(method.getModifiers());
    				Class returnType = method.getReturnType();
    				
    				
    				System.out.println("Modifiers: " + modifiers);
    				
    				System.out.printf("Parameters: ");
        			for(Class parameter:parameterTypes)
        			{
        				System.out.printf(parameter.getName() + ", ");
        				
        			}
        			System.out.println();
        			
        			System.out.printf("Exceptions: ");
        			for(Class exception: exceptions)
        			{
        				System.out.printf(exception.getName() + ", ");
        			}
        			System.out.println();
        			
        			System.out.println("Return Type: " + returnType.getName());
    				
    				
    			}
    		
    		
    	}
    	
    	System.out.println("------Done Methods------");
    	
    	//Ascend and print hierarchy of methods
    	if(ObjClass.getSuperclass() != null) {
    		inspectMethods(obj, ObjClass.getSuperclass(), objectsToInspect);
    		
    	}

    	
    	
    }
    
    
    //-----------------------------------------------------------
    private void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect)
  
    {
    	
    	System.out.println("=====Fields for: " + ObjClass.getName() + "=====");
    	
    	//Get field info
		if(ObjClass.getDeclaredFields().length >= 1)
	    {
		
			Field[] fields = ObjClass.getDeclaredFields();
			
	
			for(int i = 0; i < fields.length; i ++){
				
				Field field = fields[i];
				field.setAccessible(true);
				
				Object fieldObject = null;
				
				try {
					fieldObject = field.get(obj);
				}
				catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				if(! field.getType().isPrimitive() ) 
				    objectsToInspect.addElement( field );
				
				
				System.out.println("------Field------");
				
				try{
				System.out.println("Field: " + field.getName() + " = " + fieldObject);
				System.out.println("Field Type: " + field.getType() );
				//System.out.println("Field Type: " + field.getGenericType() );
				System.out.println("Modifiers: " + Modifier.toString(field.getModifiers()));
				
				}
				catch(IllegalArgumentException e){
					System.out.println("This object " + ObjClass.getName()
					+ "does not specify the field" + field.getName());
				}
				
			}
	

	    }
		
    	System.out.println("------Done Fields------");
    	
    	//Ascend and print hierarchy of fields
		if(ObjClass.getSuperclass() != null)
		    inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
    }
    
    //Used for test code
    public static String getTestString() {
    	String returnString = testString;
    	testString = "";
    	
    	return returnString;
    }
}
