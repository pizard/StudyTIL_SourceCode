package FileIO.NIO.Chatting.NonBlocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NServerExam extends Application{
	Selector selector;
	ServerSocketChannel serverSocketChannel;
	List<Client> connections = new Vector<Client>();
	
	void startServer() {
		try{
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(5001));
			 
			// selector에 등록
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		}catch ( Exception e){
			if(serverSocketChannel.isOpen()) {
				stopServer();
			}
			return;
		}
		
		// 작업 처리 준비가 된 SeletionKey를 선택하고, 작업 유형별로 스레드가 처리
		Thread thread = new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
/*						Thread.sleep(2000);*/
						int keyCount = selector.selectNow();
						
						// keyCount가 0인 경우는 while문 밖으로 이동
						// 즉 준비된 SelectionKey가 있는 경우 해당 채널을 구동시키는 작업
						if(keyCount == 0){
							continue;
						}
						
						// selector에서 준비가 된 key들 가져옴
						Set<SelectionKey> selectedKeys = selector.selectedKeys();
						System.out.println("select: " + keyCount);
						System.out.println("selected Key: " + selectedKeys.size());
						Iterator<SelectionKey> iterator = selectedKeys.iterator();
						// selectedKey들의 각각의 유형별로 작업 실행
						while(iterator.hasNext()){
							SelectionKey selectionKey = iterator.next();
							
							// selectionKey들의 각각의 작업 진행
							if(selectionKey.isAcceptable()) {
								accept(selectionKey);
							} else if(selectionKey.isReadable()) {
								Client client = (Client) selectionKey.attachment();
								client.receive(selectionKey);
							} else if(selectionKey.isWritable()){
								Client client = (Client) selectionKey.attachment();
								client.send(selectionKey);
							}
							
							iterator.remove();
						}
						
					} catch (Exception e) {
						// 오류 발생시 서버 종료
						if(serverSocketChannel.isOpen()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		
		thread.start();
		
		Platform.runLater(() ->{
			displayText("[서버 시작]");
			btnStartStop.setText("Stop");
		});
		
	}
 
	void stopServer() {
		try{
			// 연결되어있는 모든 클라이언트를 가져옴
			Iterator<Client> iterator = connections.iterator();
			
			// connections가 존재하는 경우 닫기
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socketChannel.close();
				iterator.remove();
			}
			
			// serverSocketChannel 닫기
			if(serverSocketChannel != null && serverSocketChannel.isOpen()){
				serverSocketChannel.close();
			}
			
			// selector 닫기
			if (selector != null && selector.isOpen()){
				selector.close();
			}
			
			
			// ㅇ.ㅇ..? 아마 javafx와 관련된 거일듯... UI
			Platform.runLater(() -> {
				displayText("[서버 멈춤]");
				btnStartStop.setText("start");
			});
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
 
	// 연결 수락 확인
	void accept(SelectionKey selectionKey) {
		try{
			// selectionKey를 통해 Channel 불러옴
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			
			String msg = "[연결 수락: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]";
			Platform.runLater(() -> displayText(msg));
			
			Client client = new Client(socketChannel);
			connections.add(client);
			
			Platform.runLater(() -> displayText("[연결 개수: " + connections.size() + "]"));

		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
 
	class Client {
		SocketChannel socketChannel;
		String sendData;
		long sendTime;
		long receiveTime;
		
		/*ByteBuffer byteBuffer = null;*/
		// socketChannel을 받아서 Client에 등록
		Client(SocketChannel socketChannel) throws IOException {
			this.socketChannel = socketChannel;
			
			// 이건 왜 해주는 거지? non-blocking이 같이 안들어가나?
			socketChannel.configureBlocking(false);
			// selector에 Channel을 등록하고 selectionKey 반환
			SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
			
			// ????? selectionKey에 어떤 정보를 첨부로 넣은거지?, socketChannel넣은건가..?
			selectionKey.attach(this);
		}
		
		void receive(SelectionKey selectionKey) {
			try {
				System.out.println("받긴 받았나?");
				this.receiveTime = System.currentTimeMillis();
				ByteBuffer byteBuffer = ByteBuffer.allocate(100);
				// socketChannel의 내용을 읽어 byteBuffer로 넣는데 
				// 이거 왜 selectionKey를 이용하는게 아니라 그냥 Client에 있던걸 넣는거냐? 되기야 하겠다마는 이래도 되는건가?
				// 이럴거면 selectionKey는 왜 받아온거야?ㅋㅋㅋㅋㅋㅋ
				int byteCount = socketChannel.read(byteBuffer);
				if(byteCount == -1){
					throw new IOException();
				}
				Charset charset = Charset.forName("UTF-8");

//				byteBuffer.put(charset.encode("_receiveServer_"));		// 추가된 부분
				String msg = "[요청 처리: " + socketChannel.getRemoteAddress() +  ": " + Thread.currentThread().getName() + "]";
				Platform.runLater(() -> displayText(msg));
				byteBuffer.flip();
				String data = charset.decode(byteBuffer).toString();
				
				// connections에 연결되어있는 모든 client에 data 전달
				for (Client client : connections) {
//					client.sendData = data + "_sendServer_";
					// 각 Client의 Channel이 Server의 selector에 등록되어 있는지 확인
					SelectionKey key = client.socketChannel.keyFor(selector);
					// SelectionKey의 작업 유형을 쓰기 유형으로 변경
					key.interestOps(SelectionKey.OP_WRITE);
				}
				// blocking되어 있던 selector 실행, 즉시 리턴
				selector.wakeup();
				
			} catch(Exception e ) {
				 try {
						connections.remove(this);
						String msg = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
								+ Thread.currentThread().getName() + "]";
		 
						Platform.runLater(() -> displayText(msg));
		 
						socketChannel.close();
					} catch (IOException e2) {
		 
					}
			}
		}
		
		void send(SelectionKey selectionKey) {
			try {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode(sendData);
	 
				socketChannel.write(byteBuffer);
				selectionKey.interestOps(SelectionKey.OP_READ);
				selector.wakeup();
				this.sendTime = System.currentTimeMillis();
/*				String msg = "보낸 시간: " + this.sendTime + " 받은 시간: " + receiveTime + " \n 소요 시간: " + (this.sendTime - this.receiveTime);*/
				String msg = "[보내기!]";
				Platform.runLater(() -> displayText(msg));
			} catch (Exception e) {
				try {
					String msg = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
							+ Thread.currentThread().getName() + "]";
	 
					Platform.runLater(() -> displayText(msg));
					connections.remove(this);
					socketChannel.close();
				} catch (IOException e2) {
	 
				}
			}
		}

	}
	
///////  UI 생성	///////
	TextArea txtDisplay;
	Button btnStartStop;
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(500, 300);
	 
		txtDisplay = new TextArea();
		txtDisplay.setEditable(false);
		BorderPane.setMargin(txtDisplay, new Insets(0, 0, 2, 0));
		root.setCenter(txtDisplay);
	 
		btnStartStop = new Button("start");
		btnStartStop.setPrefHeight(30);
		btnStartStop.setMaxWidth(Double.MAX_VALUE);
	 
		btnStartStop.setOnAction(e -> {
			if (btnStartStop.getText().equals("start")) {
				startServer();
			} else if (btnStartStop.getText().equals("stop")) {
				stopServer();
			}
		});
	 
		root.setBottom(btnStartStop);
	 
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("app.css").toString());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Server");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.show();
	 
	}
	 
	void displayText(String text) {
		txtDisplay.appendText(text + "\n");
	}
	 
	public static void main(String[] args) {
		launch(args);
	}
}
