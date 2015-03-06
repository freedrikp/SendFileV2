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
			long totalSizeOfFiles) throws Exception {
		long fileSize = f.length();
		dos.writeLong(fileSize);
		dos.writeUTF(parents + f.getName());

		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);

		byte[] buffer = new byte[2048];
		int bytesRead = 0;

		while ((bytesRead = bis.read(buffer)) > 0) {
			dos.write(buffer, 0, bytesRead);
			si.inform(totalSizeOfFiles, bytesRead);
		}
		bis.close();
	}

	private long buildFileTree(File f, ArrayList<File> files,
			ArrayList<String> parents, String parentPath) throws Exception {
		long sizeOfFiles = 0;
		if (f.isDirectory()) {
			File[] filesInDir = f.listFiles();
			for (File file : filesInDir) {
				if (parentPath.isEmpty()) {
					sizeOfFiles += buildFileTree(file, files, parents,
							f.getName());
				} else {
					sizeOfFiles += buildFileTree(file, files, parents,
							parentPath + "/" + f.getName());
				}
			}
		} else {
			files.add(f);
			sizeOfFiles += f.length();
			if (parentPath.isEmpty()) {
				parents.add(parentPath);
			} else {
				parents.add(parentPath + "/");
			}

		}
		return sizeOfFiles;
	}

	public void run() {
		try {
			Socket conn = new Socket(dest, 63398);
			si.println("Preparing to send!");
			OutputStream os = conn.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);

			ArrayList<File> files = new ArrayList<File>();
			ArrayList<String> parents = new ArrayList<String>();

			long totalSizeOfFiles = buildFileTree(f, files, parents, "");

			dos.writeLong(totalSizeOfFiles);
			dos.writeInt(files.size());

			for (int i = 0; i < files.size(); i++) {
				sendFile(files.get(i), parents.get(i), dos, totalSizeOfFiles);
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
