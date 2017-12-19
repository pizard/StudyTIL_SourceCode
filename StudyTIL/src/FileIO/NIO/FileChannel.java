package FileIO.NIO;

import java.io.FileInputStream;

public class FileChannel {
	//
	static FileInputStream in = null;
	public static void main(String[] args) throws Exception {
		in = new FileInputStream("F:\\TestFile.txt");
        TestThread t = new TestThread(in);
        t.start();

        Thread.sleep(2000);
        System.out.println("a: " + in.available());

        t.interrupt();

        Thread.sleep(2000);
        System.out.println("b: " + in.available());
				
	}
	
	static class TestThread extends Thread {
        FileInputStream in = null;
        public TestThread(FileInputStream o) {
            in = o;
        }
        
        public void run() {
            try {
                int v = 0;
                while ( ( v = in.read() ) != -1 ) {
                    System.out.println("Thread start...");
                    System.out.println(v);
                    Thread.sleep(500);
                }
                in.close();
            }catch (Exception e) {
                System.out.println("Thread end...");
            }
        }
    }
}
