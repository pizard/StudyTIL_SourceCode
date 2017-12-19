package FileIO.NIO.FileCopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

public class Copy {
	// 출처: http://gyrfalcon.tistory.com/entry/Java-NIO-Channel [Minsub's Blog]
	public static void main(String[] args)throws Exception {
		selectMenu();
	}
	
	static int count = 0;
	static void selectMenu() throws Exception{
		int num = -1;
		Scanner scan = new Scanner(System.in);
		num = scan.nextInt();
//		while(num<1 || num>4){
//			System.out.println("사용법 : java FileTest 메서드번호 파일명 복사 파일명");
//			System.out.println("1. io패키지 사용");
//			System.out.println("2. 매핑을 이용.");
//			System.out.println("3. read()와 write()사용");
//			System.out.println("4. transferTo() 사용");
//			num = scan.nextInt();
//		}
		
		count = count+1;
		FileInputStream file_in;
		try {
			file_in = new FileInputStream("C:\\Users\\coscoi_cjm\\Desktop\\20171122_205004.mp4");
			FileOutputStream file_out = new FileOutputStream("C:\\Users\\coscoi_cjm\\Desktop\\copy\\test" + count + ".mp4");
			FileChannel in = file_in.getChannel();
			FileChannel out = file_out.getChannel();
			long start = System.currentTimeMillis();
	
			switch(num){
			case 1: 
				copyIO(file_in, file_out, start);
				break;
			case 2:
				copyMap(in, out, start);
				break;
			case 3:
				copyNIO(in, out, start);
				break;
			case 4:
				copyTransfer(in, out, start);
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scan.close();
		}
	}
	

	static void copyIO(FileInputStream file_in, FileOutputStream file_out, long start) throws Exception{
		byte[] buf = new byte[1024];
		for (int i; (i=file_in.read(buf))!=-1; ) {
			file_out.write(buf, 0, i);
		} 
		file_in.close();
		file_out.close();

		long end = System.currentTimeMillis();
		System.out.println("1. io패키지 소요시간 " + (end-start)/(double)1000);
//		System.out.println("1. 파일간 복사 --> io패키지 사용 성공!!");
		selectMenu();
	}
	
	static void copyMap(FileChannel in, FileChannel out, long start) throws Exception{
		MappedByteBuffer m_buffer = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
		out.write(m_buffer);
		in.close();
		out.close();
		
		long end = System.currentTimeMillis();
		System.out.println("2. Mapping 소요시간 " + (end-start)/(double)1000);
		selectMenu();
	}
	
	static void copyNIO(FileChannel in, FileChannel out, long start) throws Exception {
		ByteBuffer buf = ByteBuffer.allocate((int)in.size());
		
		in.read(buf);
		buf.flip();
		out.write(buf);
		in.close();
		out.close();

		long end = System.currentTimeMillis();
		System.out.println("3. read()와 write() 소요시간 " + (end-start)/(double)1000);
		selectMenu();

	}
	
	static void copyTransfer(FileChannel in, FileChannel out, long start) throws Exception {
		in.transferTo(0, in.size(), out);
		in.close();
		out.close();
		
		long end = System.currentTimeMillis();
		System.out.println("4. transferTo() 소요시간 " + (end-start)/(double)1000);
		selectMenu();
	}

}
