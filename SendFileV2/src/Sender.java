

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Sender extends Thread {
	private String dest;
	private File f;
	private StatusInformer si;

	public Sender(String dest, File f, StatusInformer si) {
		super();
		this.dest = dest;
		this.f = f;
		this.si = si;
	}

	private void sendFile(File f, String parents, DataOutputStream dos,
			long totSize) throws Exception {
		long size = f.length();
		// si.println(Long.toString(size));
		dos.writeLong(size);
		dos.writeUTF(parents + f.getName());

		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);

		byte[] buffer = new byte[2048];
		int bytesRead = 0;

		while ((bytesRead = bis.read(buffer)) > 0) {
			dos.write(buffer, 0, bytesRead);
			si.inform(totSize, bytesRead);
		}
		bis.close();
	}

	private long buildFileTree(File f, ArrayList<File> files,
			ArrayList<String> parents, String dir) throws Exception {
		long size = 0;
		if (f.isDirectory()) {
			File[] indir = f.listFiles();
			for (File file : indir) {
				if (dir.isEmpty()) {
					size += buildFileTree(file, files, parents, f.getName());
				} else {
					size += buildFileTree(file, files, parents,
							dir + "/" + f.getName());
				}
			}
		} else {
			files.add(f);
			size += f.length();
			if (dir.isEmpty()) {
				parents.add(dir);
			} else {
				parents.add(dir + "/");
			}

		}
		return size;
	}

	public void run() {
		try {
			Socket conn = new Socket(dest, 63398);
			si.println("Preparing to send!");
			OutputStream os = conn.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);

			ArrayList<File> files = new ArrayList<File>();
			ArrayList<String> parents = new ArrayList<String>();

			long totSize = buildFileTree(f, files, parents, "");
//			System.out.println("Sending: " + totSize);

			dos.writeLong(totSize);
			dos.writeInt(files.size());

			for (int i = 0; i < files.size(); i++) {
				sendFile(files.get(i), parents.get(i), dos, totSize);
			}
			si.reset();
			si.println("Sending finished!");
			os.flush();

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
