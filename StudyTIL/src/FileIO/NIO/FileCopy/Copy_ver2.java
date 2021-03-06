package FileIO.NIO.FileCopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

// 출처: http://gyrfalcon.tistory.com/entry/Java-NIO-Channel [Minsub's Blog]
public class Copy_ver2 {
	public static void main(String[] args)throws Exception {
		selectMenu();
	}
	
	static int count = 0, num = -1, menu = -1;
	static void selectMenu() throws Exception{
		System.out.println("menu 번호 시행횟수를 차례로 입력해 주세요.");
		Scanner scan = new Scanner(System.in);
		menu = Integer.parseInt(scan.next());
		num = Integer.parseInt(scan.next());
		System.out.println("menu: " + menu + " num: " + num);
		double[] Time = new double [num];
		double averageTime = 0;
//		while(num<1 || num>4){
//			System.out.println("사용법 : java FileTest 메서드번호 파일명 복사 파일명");
//			System.out.println("1. io패키지 사용");
//			System.out.println("2. 매핑을 이용.");
//			System.out.println("3. read()와 write()사용");
//			System.out.println("4. transferTo() 사용");
//			num = scan.nextInt();
//		}
		
		try {
			FileInputStream file_in = new FileInputStream("C:\\Users\\coscoi_cjm\\Desktop\\20171122_205004.mp4");
			FileOutputStream file_out = new FileOutputStream("C:\\Users\\coscoi_cjm\\Desktop\\copy\\test" + count + ".mp4");
			FileChannel in = file_in.getChannel();
			FileChannel out = file_out.getChannel();
	
			switch(menu){
				case 1: 
					System.out.println("1. copyIO()");
					for(int i=0; i<num; i++){
						Time[i] = copyIO(file_in, file_out);
						System.out.println(i + "번째 소요시간: " + Time[i]);
					}
						
					break;
				case 2:
					System.out.println("2. copyMap()");
					for(int i=0; i<num; i++){
						Time[i] = copyMap(in, out);
						System.out.println(i + "번째 소요시간: " + Time[i]);
					}
					break;
				case 3:
					System.out.println("3. copyNIO()");
					for(int i=0; i<num; i++){
						Time[i] = copyNIO(in, out);
						System.out.println(i + "번째 소요시간: " + Time[i]);
					}
					break;
				case 4:
					System.out.println("4. copyTransfer()");
					for(int i=0; i<num; i++){
						Time[i] = copyTransfer(in, out);
						System.out.println(i + "번째 소요시간: " + Time[i]);
					}
					break;
			}
			averageTime = calculateAverage(Time);
			System.out.println("평균소요시간: " + averageTime);
			
			
			file_in.close();
			file_out.close();
			in.close();
			out.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scan.close();
		}
	}
	

	static double copyIO(FileInputStream file_in, FileOutputStream file_out) throws Exception{
		long start = System.currentTimeMillis();

		byte[] buf = new byte[1024];
		for (int i; (i=file_in.read(buf))!=-1; ) {
			file_out.write(buf, 0, i);
		} 

		long end = System.currentTimeMillis();
		return (end-start)/(double)1000;
	}
	
	static double copyMap(FileChannel in, FileChannel out) throws Exception{
		long start = System.currentTimeMillis();
		
		MappedByteBuffer m_buffer = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
		out.write(m_buffer);
		
		long end = System.currentTimeMillis();
		return (end-start)/(double)1000;
	}
	
	static double copyNIO(FileChannel in, FileChannel out) throws Exception {
		long start = System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocateDirect((int)in.size());
		
		in.read(buf);
		buf.flip();
		out.write(buf);

		long end = System.currentTimeMillis();
		return (end-start)/(double)1000;

	}
	
	static double copyTransfer(FileChannel in, FileChannel out) throws Exception {
		long start = System.currentTimeMillis();

		in.transferTo(0, in.size(), out);
		
		long end = System.currentTimeMillis();
		return (end-start)/(double)1000;
	}

	static double calculateAverage(double[] time){
		
		double sum = 0;
		for(int i=0; i<time.length; i++){
			sum += time[i];
		}
		double averageTime = sum/time.length;
	
		return averageTime;
	}
}
