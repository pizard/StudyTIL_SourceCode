package FileIO.NIO.Chatting;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ServerTest {
	public static void main(String[] args){
		
		ServerSocketChannel serverSocketChannel = null; // Selectable Channel
		try{
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true);	 // 블로킹 채널, DEFAULT도 BLOCKING일텐뎅
			serverSocketChannel.bind(new InetSocketAddress(5001));	// 5001포트에 연결
			
			while(true){
				System.out.println("[연결 기다림]");
				SocketChannel socketChannel = serverSocketChannel.accept();
				InetSocketAddress isa = (InetSocketAddress) socketChannel.getRemoteAddress();
				System.out.println("[연결 수락] " + isa.getHostName());
				
				ByteBuffer byteBuffer = null;
				Charset charset = Charset.forName("UTF-8"); // 캐릭터 셋 설정
				
				byteBuffer = ByteBuffer.allocate(100);	//메모리 할당 근데 왜 allocte이냐고!!
				int byteCount = socketChannel.read(byteBuffer);
				byteBuffer.flip();
				
				String data = charset.decode(byteBuffer).toString();
				System.out.println("[데이터 받기 성공] " + data);
				
				byteBuffer = charset.encode("Hello Client");
				socketChannel.write(byteBuffer);
				System.out.println("[데이터 보내기 성공]");
				
				
			}
			
		}catch (Exception e) {
			System.out.println("에러 발생");
			e.printStackTrace();
			// TODO: handle exception
		}
		
		if(serverSocketChannel.isOpen()){
			try{
				serverSocketChannel.close();
			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
}
