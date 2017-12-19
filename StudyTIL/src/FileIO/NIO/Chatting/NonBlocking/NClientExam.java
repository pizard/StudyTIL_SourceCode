package FileIO.NIO.Chatting.NonBlocking;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NClientExam extends Application{

	SocketChannel socketChannel;
	 
	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(true);
					socketChannel.connect(new InetSocketAddress("localhost", 5001));
					Platform.runLater(() -> {
						try {
							displayText("[연결 완료: " + socketChannel.getRemoteAddress() + "]");
							btnConn.setText("stop");
							btnSend.setDisable(false);
						} catch (Exception e) {
						}
					});
				} catch (Exception e) {
					Platform.runLater(() -> displayText("[서버 통신 안됨]"));
	 
					if (socketChannel.isOpen()) {
						stopClient();
					}
					return;
				}
				receive();
			}
		};
	 
		thread.start();
	}
 
	void stopClient() {
		try {
			Platform.runLater(() -> {
				displayText("[연결 끊음]");
				btnConn.setText("start");
				btnSend.setDisable(true);
			});
	 
			if (socketChannel != null && socketChannel.isOpen()) {
				socketChannel.close();
			}
		} catch (IOException e) {
	 
		}
	}
 
	void receive() {
		while (true) {
			try {
				ByteBuffer byteBuffer = ByteBuffer.allocate(100);
	 
				int byteCount = socketChannel.read(byteBuffer);
				Charset charset = Charset.forName("UTF-8");
	 
				if (byteCount == -1) {
					throw new IOException();
				}
				
//				byteBuffer.put(charset.encode("_receiveClient_"));		// 추가된 부분
				byteBuffer.flip();
				String data = charset.decode(byteBuffer).toString();
				long receiveTime = System.currentTimeMillis();
				Platform.runLater(() -> displayText("[받기 완료: " + receiveTime +"] \n" + data));
			} catch (Exception e) {
				Platform.runLater(() -> displayText("[서버 통신 안됨]"));
				stopClient();
				break;
			}
		}
	}
 
	void send(String data) {
		Thread thread = new Thread() {
			 
			@Override
			public void run() {
				try {
					System.out.println("send 시작");
					Charset charset = Charset.forName("UTF-8");
					ByteBuffer byteBuffer = charset.encode(data);
					socketChannel.write(byteBuffer);
					
					Platform.runLater(() -> displayText("[보내기 완료]"));
				} catch (Exception e) {
					Platform.runLater(() -> displayText("[서버 통신 안됨]"));
					stopClient();
				}
			}
	 
		};
		thread.start();
		
	}
		
	void uploadFile(File file){
		if(file != null){
			Thread thread = new Thread() {
				@Override
				public void run() {
					try{
						System.out.println("File 전송 시작");
						FileChannel fc = new FileInputStream(file).getChannel();
						long size = fc.transferTo(0, file.length(), socketChannel);
						
						System.out.println("보내진 파일의 크기: " + size);
						Platform.runLater(() -> displayText("[파일 보내기 완료]"));
					}catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			};
			thread.start();
		}else{
			
		}
	}
	///////////////////////
	// UI 생성 코드
	TextArea txtDisplay;
	TextField txtInput;
	Button btnConn, btnSend, btnFile;
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = new BorderPane();
		root.setPrefSize(500, 300);
	 
		txtDisplay = new TextArea();
		txtDisplay.setEditable(false);
		BorderPane.setMargin(txtDisplay, new Insets(0, 0, 2, 0));
		root.setCenter(txtDisplay);
	 
		btnFile = new Button("file");
		btnFile.setPrefSize(60, 30);
		btnFile.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(primaryStage);
			uploadFile(file);
		});
		
		BorderPane bottom = new BorderPane();
		txtInput = new TextField();
		txtInput.setPrefSize(60, 30);
		BorderPane.setMargin(txtInput, new Insets(0, 1, 1, 1));
	 
		btnConn = new Button("start");
		btnConn.setPrefSize(60, 30);
		btnConn.setOnAction(e -> {
			if (btnConn.getText().equals("start")) {
				startClient();
			} else if (btnConn.getText().equals("stop")) {
				stopClient();
			}
		});
	 
		btnSend = new Button("send");
		btnSend.setPrefSize(60, 30);
		btnSend.setDisable(true);
		btnSend.setOnAction(e -> send(txtInput.getText()));
		
		bottom.setCenter(txtInput);
		bottom.setLeft(btnConn);
		bottom.setRight(btnSend);
		bottom.setTop(btnFile);
	 
		root.setBottom(bottom);
	 
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("app.css").toString());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Client");
		primaryStage.setOnCloseRequest(evnt -> stopClient());
		primaryStage.show();
		
	}
	 
	void displayText(String text) {
		txtDisplay.appendText(text + "\n");
	}
	 
	public static void main(String[] args) {
		launch(args);
	}


}