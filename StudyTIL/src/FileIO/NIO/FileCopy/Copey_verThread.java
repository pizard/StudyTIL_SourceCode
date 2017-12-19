package FileIO.NIO.FileCopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.util.Scanner;

public class Copey_verThread {

	Selector selector;

	static int count = 0, num = -1, menu = -1;
	public static void main(String argsp[]){
		System.out.println("menu 번호 시행횟수를 차례로 입력해 주세요.");
		Scanner scan = new Scanner(System.in);
		menu = Integer.parseInt(scan.next());
		num = Integer.parseInt(scan.next());
		System.out.println("menu: " + menu + " num: " + num);
		double[] Time = new double [num];
		double averageTime = 0;
		
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
	
	void fileCopy() {
		try{
			selector = Selector.open();
		}catch (Exception e) {
			// TODO: handle exception
		}
		Thread thread = new Thread() {
			@Override
			public void run() {
				while(true){
					try{
						
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		};
		
		thread.start();
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
