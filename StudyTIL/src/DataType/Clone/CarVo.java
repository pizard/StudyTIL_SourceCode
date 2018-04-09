package DataType.Clone;

public class CarVo implements Cloneable{
	private String brand;
	private int price;
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
