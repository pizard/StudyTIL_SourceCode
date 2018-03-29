package FileIO.NIO.transferto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TransferToServer {
	ServerSocketChannel listener = null;
	protected void mySetup(){
		InetSocketAddress listenAddr = new InetSocketAddress(9026);
		try {
			listener = ServerSocketChannel.open();
			ServerSocket ss = listener.socket();
			ss.setReuseAddress(true);
			ss.bind(listenAddr);
			System.out.println("Listening on port : "+ listenAddr.toString());			
		}catch (IOException e) {
			System.out.println("Failed to bind, is port : "+ listenAddr.toString()
			+ " already in use ? Error Msg : "+e.getMessage());
			e.printStackTrace();			
			// TODO: handle exception
		}
	}
	
	public static void main(String[] args){
		TransferToServer dns = new TransferToServer();
		dns.mySetup();
		dns.readData();
	}
	
	private void readData() {
		try{
			while(true){
				SocketChannel socketChannel = listener.accept();
				System.out.println("Accepted: " + socketChannel);
				socketChannel.configureBlocking(true);

				ByteBuffer dst = ByteBuffer.allocate(500000000);
				FileOutputStream file_out = new FileOutputStream("C:\\Users\\coscoi_cjm\\Desktop\\copy\\FILE.mp4");
				FileChannel fileChannel = file_out.getChannel();
				long count = fileChannel.size();
//		        fileChannel.transferFrom(socketChannel, 0, count); 
				
/*				long pos = 0, length = fc.size();
				while( pos < length) {
					pos += fc.transferFrom(socketChannel, pos, length,  sc);			
					System.out.println("진행 크기: " + pos);
				}*/
				
				/*int nread = 0;
				while(nread != -1){
					try{
						nread = socketChannel.read(dst);
						System.out.println("다 읽었다네~: ");
					}catch (Exception e) {
						e.printStackTrace();
						nread = -1;
						// TODO: handle exception
					}
				}
				System.out.println("사이즈: " + dst.position());
				dst.flip();
				fileChannel.write(dst);*/
				
				fileChannel.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
