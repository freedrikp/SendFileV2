

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Receiver extends Thread {
	private StatusInformer si;

	public Receiver(StatusInformer si) {
		super();
		this.si = si;
	}

	private void receiveFile(DataInputStream dis, long totSize) throws Exception {
		long size = dis.readLong();
		String name = dis.readUTF();
		File f = new File(name);
		if (f.getParent() != null){
		f.getParentFile().mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(f);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		byte[] buffer = new byte[2048];
		int bytesRead = 0;

		int toRead = size >= buffer.length ? buffer.length : (int)size;
		long totalRead = 0;

		while ((bytesRead = dis.read(buffer, 0, toRead)) > 0
				&& !isInterrupted()) {
			bos.write(buffer, 0, bytesRead);
			si.inform(totSize, bytesRead);
			totalRead += bytesRead;
			toRead = size - totalRead >= buffer.length ? buffer.length : (int)(size
					- totalRead);
		}
		
		bos.flush();
		bos.close();
	}

	public void run() {
		si.println("Receiver running!");
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(63398);
		} catch (IOException e) {
			e.printStackTrace();
			this.interrupt();
		}
		while (!isInterrupted()) {
			try {
				Socket conn = socket.accept();
				si.reset();
				si.println("Preparing to receive!");
				InputStream is = conn.getInputStream();
				DataInputStream dis = new DataInputStream(is);

				long totSize = dis.readLong();
				int size = dis.readInt();
//			System.out.println("Receving: " + totSize);
				for (int i = 0; i < size; i++) {
					receiveFile(dis,totSize);
				}
				si.reset();

				if (!isInterrupted()) {
					si.println("Receiving finished!");
				}
				socket.close();

			} catch (SocketException se) {
				// System.out.println("Socket timeout, reconnecting.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		si.println("Receiver stopped!");
	}
}
