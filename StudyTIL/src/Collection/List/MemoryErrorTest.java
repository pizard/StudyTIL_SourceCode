package Collection.List;

import java.util.ArrayList;
import java.util.List;

public class MemoryErrorTest {

	public static void main(String[] args) {

		
		System.out.println("--------------------------- 잘못된 예 ---------------------------");

		List<StringBuilder> tempList1 = new ArrayList<StringBuilder>();
		StringBuilder temp = new StringBuilder();
		int count = 5;
		for(int i=0; i< count; i++) {
			temp.delete(0, temp.length());
			temp.append(i);
			tempList1.add(temp);
		}
		
		for(StringBuilder temp2 : tempList1) {
			System.out.println(count + " : " + temp2);
		}
		
		
		System.out.println("---------------------------- 잘된 예 ----------------------------");
		
		
		List<StringBuilder> tempList2 = new ArrayList<StringBuilder>();
		for(int i=0; i< count; i++) {
			StringBuilder temp2 = new StringBuilder();
			temp2.delete(0, temp2.length());
			temp2.append(i);
			tempList2.add(temp2);
		}
		
		for(StringBuilder temp2 : tempList2) {
			System.out.println(count + " : " + temp2);
		}
		
		
		
	}
}
