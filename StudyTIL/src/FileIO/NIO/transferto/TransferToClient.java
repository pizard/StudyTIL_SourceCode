package FileIO.NIO.transferto;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class TransferToClient {
	// 출처: https://free-strings.blogspot.kr/2016/04/zero-copy.html [FN(RICE) -> CODE]
	public static void main(String[] args) throws IOException{
		TransferToClient sfc = new TransferToClient();
		sfc.testSendFile();
	}
	
	public void testSendFile() throws IOException{
		String host = "localhost";
		int port = 9026;
		SocketAddress sad = new InetSocketAddress(host, port);
		SocketChannel sc = SocketChannel.open();
		sc.connect(sad);
		sc.configureBlocking(true);
		
		String fname = "C:\\Users\\coscoi_cjm\\Desktop\\20171122_205004.mp4";
		FileChannel fc = new FileInputStream(fname).getChannel();
		long start = System.currentTimeMillis();
		long nsent = 0, curnset = 0;
		curnset = fc.transferTo(0,  fc.size(),  sc);
		System.out.println("total bytes transferred--"+curnset+" and time taken in MS--"+(System.currentTimeMillis() - start));
		    //fc.close();
		  
	}

}
