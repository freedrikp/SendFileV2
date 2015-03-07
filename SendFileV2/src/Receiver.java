import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver extends Thread {
	private StatusInformer si;

	public Receiver(StatusInformer si) {
		super();
		this.si = si;
	}

	private void receiveFile(DataInputStream dis, long totalSizeOfFiles)
			throws Exception {
		long fileSize = dis.readLong();
		String fileName = dis.readUTF();
		File f = new File(fileName);
		if (f.getParent() != null) {
			f.getParentFile().mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(f);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		byte[] buffer = new byte[2048];
		int bytesRead = 0;

		int toRead = fileSize >= buffer.length ? buffer.length : (int) fileSize;
		long totalRead = 0;

		while ((bytesRead = dis.read(buffer, 0, toRead)) > 0
				&& !isInterrupted()) {
			bos.write(buffer, 0, bytesRead);
			si.inform(totalSizeOfFiles, bytesRead);
			totalRead += bytesRead;
			toRead = fileSize - totalRead >= buffer.length ? buffer.length
					: (int) (fileSize - totalRead);
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

				long totalSizeOfFiles = dis.readLong();
				int nbrOfFiles = dis.readInt();
				for (int i = 0; i < nbrOfFiles && !isInterrupted(); i++) {
					receiveFile(dis, totalSizeOfFiles);
				}
				si.reset();

				if (!isInterrupted()) {
					si.println("Receiving finished!");
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		si.println("Receiver stopped!");
	}
}
