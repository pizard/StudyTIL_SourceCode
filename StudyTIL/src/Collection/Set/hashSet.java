package Collection.Set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class hashSet {

	public static void main(String[] args) {
		
		Set<String> aa = new HashSet <String>();
		
		System.out.println("add Test: " + aa.add("1"));
		System.out.println("add Test: " + aa.add("1"));
		aa.add("2");
		aa.add("3");
		aa.add("4");
		aa.add("5");
		System.out.println("remove Test: "+ aa.remove("5"));
		System.out.println("remove Test: "+ aa.remove("3"));
		
		
		Iterator pp = aa.iterator();
		pp.next();
		pp.remove();
		
		int count = 1;
		for(String p : aa) {
			System.out.println(count + " : " + p);
			count++;
		}
		
	}
}
