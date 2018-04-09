package DataType.Clone;

public class ClassType implements Cloneable {
	public static void main(String[] args) {
		CarVo car1 = new CarVo();
		car1.setBrand("Hyundai");
		CarVo car2 = new CarVo();
		car2.setBrand("Benz");
		
		CarVo car3 = car1;			// Shallow Copy
		CarVo car4 = new CarVo();	// Deep Copy
		try{
			car4 = (CarVo) car2.clone();
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		System.out.println(car1.hashCode());		// 2018699554
		System.out.println(car2.hashCode());		// 1311053135
		System.out.println(car3.hashCode());		// 2018699554
		System.out.println(car4.hashCode());		// 118352462
		
		
		System.out.println(car3.getBrand());		// Hyundai
		car1.setBrand("Kia");				
		System.out.println(car3.getBrand());		// Kia
		car1.setBrand("GM");
		System.out.println(car3.getBrand());		// GM
	}
}