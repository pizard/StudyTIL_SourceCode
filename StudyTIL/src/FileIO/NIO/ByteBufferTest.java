package FileIO.NIO;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class ByteBufferTest {
	public static void main(String[] args){
		
		try {
			RandomAccessFile raf = new RandomAccessFile("F:\\TestFile.txt", "rw");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = ByteBuffer.allocateDirect(12);
			buf.clear();
			raf.seek(10);
			 // 파일의 10째 바이트로 파일포인터 이동
			channel.read(buf); // channel에서 읽어 buf에 저장!buf.flip();
			buf.flip();
//			Charset charset = Charset.forName("UTF-8");
			Charset charset = Charset.defaultCharset();


			String data = charset.decode(buf).toString();
			System.out.println(data);
			// 다시 스트링으로 변환 
			/*byte[] rtnBytes = buf.array();
			String rtnStr = new String(rtnBytes);
			System.out.println(rtnStr);*/
			
			raf.seek(40);    // 파일의 40째 바이트로 파일포인터 이동
			channel.write(buf); // buf의 내용을 channel에 저장!
			channel.close();
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
