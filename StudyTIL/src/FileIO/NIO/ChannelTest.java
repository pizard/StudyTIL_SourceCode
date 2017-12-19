package FileIO.NIO;

import java.io.FileOutputStream;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ChannelTest {

	static Selector selector;
	static ServerSocketChannel serverSocketChannel;
	public static void main(String[] args){
		try{
			FileOutputStream fo = new FileOutputStream("F:\\TestFile.txt");
	        GatheringByteChannel channel = fo.getChannel();
	        System.out.println("isOpen() : " + channel.isOpen());
	        System.out.println("isOpen() : " + channel.isOpen());
	        SelectionKey selectionKey = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
	        System.out.println("@@@@ : " + serverSocketChannel.isRegistered());
	        
	        
	        
	        
		}catch (Exception e) {
			System.out.println(e);
		}

	}
}
