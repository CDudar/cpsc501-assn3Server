import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Deserializer {
	
	
	
	HashMap<Integer, Object> IDsToObjects;
	Vector<Object> deserializedObjects;
				


	public Object deserialize(Document docToDeserialize){
		
		
		IDsToObjects = new HashMap<Integer, Object>();
		deserializedObjects = new Vector<Object>();
		
		Element root = docToDeserialize.getRootElement();

		List<Element> objectElements = root.getChildren();
		
		initializeBaseObjects(objectElements);
		
		for(int i = 0; i < objectElements.size(); i++){
			
			constructObject(objectElements.get(i));
			
		}
		
		return deserializedObjects.get(0);
		
	}
	
	
	private void initializeBaseObjects(List<Element> objectElements){
		
		for(int i = 0; i < objectElements.size(); i++){
			
			Element currentElement = objectElements.get(i);
			
			Object obj = null; 
			
			
			try {
				Class objClass = Class.forName(currentElement.getAttributeValue("class"));
				
				if(objClass.isArray()){
					obj = Array.newInstance(objClass.getComponentType(), Integer.valueOf(currentElement.getAttributeValue("length")));
				}
				else{
					
					Constructor cons = objClass.getDeclaredConstructor(new Class<?>[0]);
					cons.setAccessible(true);
					obj = cons.newInstance(new Object[] { });
					
				}
				IDsToObjects.put(Integer.valueOf(currentElement.getAttributeValue("id")), obj);
			
			
			}

			catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	
	private void constructObject(Element objectElement){
		
		
		Object obj = IDsToObjects.get(Integer.valueOf(objectElement.getAttributeValue("id")));
		
		Class objClass = obj.getClass();
		List<Element> fields = objectElement.getChildren();
		
		if(objClass.isArray()){
			//array object
			
			int length = Integer.valueOf(objectElement.getAttributeValue("length"));
			constructArray(obj, fields, length);
		}
		else{
			//Non array object
			
			deserializedObjects.add(obj);
			constructFields(obj, fields);
		}
	}
	
	private void constructArray(Object obj, List<Element> fields, int length){
		
		Class objClass = obj.getClass();
		
		if(objClass.getComponentType().isPrimitive()){
			//primitive list
			
			for (int i = 0; i < length; i++){
				
				String value = fields.get(i).getText();
				
				Array.set(obj, i, UtilityMethods.toObject(objClass.getComponentType(), value));
				
			}
			
		}
		else{
			//object reference list
			
			for(int i = 0; i < length; i++){
				
				int id = Integer.valueOf(fields.get(i).getText());
				Array.set(obj, i, IDsToObjects.get(id));
				
			}
		}
		
	}
	
	private void constructFields(Object obj, List<Element> fields){
		
		Class objClass = obj.getClass();
		
		for(int i = 0; i < fields.size(); i++){
			
			Element fieldElement = fields.get(i);
			
			String fieldName = fieldElement.getAttributeValue("name");
			
			Field f = null;
			try {
				f = objClass.getDeclaredField(fieldName);
				f.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			
			if(f.getType().isPrimitive()){
				//primitive value field
				
				try {
					f.set(obj, UtilityMethods.toObject(f.getType(), fieldElement.getChild("value").getText()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					System.out.println("Don't set final fields");

				}
				
				
			}
			else{
				//object reference field
					try {
						f.set(obj, IDsToObjects.get(Integer.valueOf(fieldElement.getChild("reference").getText())));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						System.out.println("Dont set final fields");
					}
			}
			
		}
		
	}
	
	
	
}
