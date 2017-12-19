package FileIO.NIO;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

public class URLFileCopyTest {

	static String Scraping_url = "";					// XML���� Scraping�� �� file url
	public static void main(String[] args) throws Exception{
				OutputStream outStream = null;
				InputStream is = null;
				FileOutputStream fos = null;
				URLConnection uCon = null;
				URL url = null;

				
				String savePath = "";
				File saveFolderPath = new File(savePath);
				
				if(!saveFolderPath.exists()){
					System.out.println("�ش� ����Ʈ ���丮�� �����ϴ�. ");
					saveFolderPath.mkdirs();
				}
				
				try{
				    url = new URL(Scraping_url);
				}catch(MalformedURLException me){
					me.printStackTrace();
				}

				try{    
					uCon = url.openConnection(); 
				}catch(IOException io){
					io.printStackTrace();
				}
				
//				uCon.setDoOutput(true);
//				uCon.setDoInput(true);
				try {
					// �ش� ��¥�� string ����(���ϸ� ���)
					java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
					Calendar cDateCal = Calendar.getInstance();
					cDateCal.add(Calendar.DATE, -1);
					Date currentDate = cDateCal.getTime();
					String dateString = format.format(currentDate);
					dateString += "/" + dateString;
					
					// ���� ����
					fos = new FileOutputStream(dateString);
					outStream = new BufferedOutputStream(fos);
					is = uCon.getInputStream();
					byte[] buf;
					buf = new byte[is.available()];
					
					is.read(buf);
					// 1�� ���
					outStream.write(buf);
					
					// 2�� ���
/*					int byteRead = 0;
					while ((byteRead = is.read(buf)) != -1) {
						System.out.println("Byteread: " + byteRead);
						outStream.write(buf, 0, byteRead);
					}*/
					
					// Performance �� ���̰� �ֳ�..? ��.��
					
					outStream.flush();
				}catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
	}
}
