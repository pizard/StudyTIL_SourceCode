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
			 // ������ 10° ����Ʈ�� ���������� �̵�
			channel.read(buf); // channel���� �о� buf�� ����!buf.flip();
			buf.flip();
//			Charset charset = Charset.forName("UTF-8");
			Charset charset = Charset.defaultCharset();


			String data = charset.decode(buf).toString();
			System.out.println(data);
			// �ٽ� ��Ʈ������ ��ȯ 
			/*byte[] rtnBytes = buf.array();
			String rtnStr = new String(rtnBytes);
			System.out.println(rtnStr);*/
			
			raf.seek(40);    // ������ 40° ����Ʈ�� ���������� �̵�
			channel.write(buf); // buf�� ������ channel�� ����!
			channel.close();
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
