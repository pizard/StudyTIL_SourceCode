package FileIO.NIO.Chatting.Blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// 출처: http://palpit.tistory.com/644, palpit's log-b
public class ServerExam extends Application{
	ExecutorService executorService;
	ServerSocketChannel serverSocketChannel;
	List<Client> connections = new Vector<Client>();
	
	// 서버 시작
	void startServer() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		// 소켓채널서버 연결
		try{
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true);
			serverSocketChannel.bind(new InetSocketAddress(5001));
		}catch (Exception e) {
			if (serverSocketChannel.isOpen()) {
				stopServer();
			}
			e.printStackTrace();
			// TODO: handle exception
		}
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				Platform.runLater(() -> {
					displayText("[서버 시작]");
					btnStartStop.setText("stop");		// 버튼 이름 변경
				});
				
			 while (true) {
					try {
						// Client로부터 연결요청이 올때까지 Blocking
						SocketChannel socketChannel = serverSocketChannel.accept();
	 
						String msg = "[연결 수락: " + socketChannel.getRemoteAddress() + ": "
								+ Thread.currentThread().getName() + "]";
						
						// 연결요청이 온 Client를 socketChannel에 연결하고 List(connection)에 추가
						Client client = new Client(socketChannel);
						connections.add(client);
	 
						Platform.runLater(()->displayText("[연결 개수: " + connections.size() + "]"));
					} catch (Exception e) {
						if (serverSocketChannel.isOpen()) {
							stopServer();
						}
						
						break;
					}
				}
			}
		};
		executorService.submit(runnable);
	}
	
	// 서버 종료
	void stopServer() {
		try {
			Iterator<Client> iterator = connections.iterator();
			
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socketChannel.close();
				iterator.remove();
			}
			
			if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
				serverSocketChannel.close();
			}
			
			if (executorService != null && executorService.isShutdown()) {
				executorService.shutdown();
			}
			
			Platform.runLater(()->{
				displayText("[서버 종료]");
				btnStartStop.setText("start");
			});
		} catch (Exception e) {
			
		}
	}
	
	// 데이터 통신 코드
	class Client {
		SocketChannel socketChannel;
		 
		public Client(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
			receive();
		}

		// 데이터 받기
		void receive() {
			Runnable runnable = new Runnable() {
				 
				@Override
				public void run() {
					while (true) {
						try {
							ByteBuffer byteBuffer = ByteBuffer.allocate(100);
							
							// 클라이언트가 비정상 종료를 했을 경우 IOException 발생
							// read 즉 데이터가 넘어올때까지 Blocking
							int byteCount = socketChannel.read(byteBuffer);
							
							// 클라이언트가 정상적으로 SocketChannel의 close()를 호출헀을 경우
							if (byteCount == -1) {
								throw new IOException();
							}
							
							String msg = "[요청 처리: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]";
							
							Platform.runLater(()->displayText(msg));
							
							byteBuffer.flip();
							Charset charset = Charset.forName("UTF-8");
							String data = charset.decode(byteBuffer).toString();
							
							for (Client client : connections) {
								client.send(data);
							}
							
						} catch (Exception e) {
							try {
								connections.remove(Client.this);
								
								String msg = "[클라이언트 통신 안됨: " +
										socketChannel.getRemoteAddress() + ": " +
										Thread.currentThread().getName() + "]";
								
								Platform.runLater(()->displayText(msg));
								socketChannel.close();
							} catch (IOException e2) {
								
							}
							
							break;
						}
					}
				}
				
			};
			
			executorService.submit(runnable);
		}

		// 데이터 전송
		void send(String data) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						Charset charset = Charset.forName("UTF-8");
						ByteBuffer byteBuffer = charset.encode(data);
						socketChannel.write(byteBuffer);
					} catch (Exception e) {
						try {
							String msg = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": "
									+ Thread.currentThread().getName() + "]";
		 
							Platform.runLater(() -> displayText(msg));
							connections.remove(Client.this);
							socketChannel.close();
		 
						} catch (IOException e2) {
		 
						}
					}
				}
			};
			executorService.submit(runnable);
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
