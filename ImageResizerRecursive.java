package imageresizer;

import java.io.File;

public class ImageResizerRecursive {

	ImageResizer imageResizer = new ImageResizer();

	public static void main(String[] args) {
		new ImageResizerRecursive().run(args);
	}

	private void usage() {
		System.out.println("Usage: source-directory destination-directory small|medium|large|width x height");
		System.out.println("e.g.   original resized medium");
		System.out.println("e.g.   original resized 1024x768");
	}

	public void run(String[] args) {

		// source-directory destination-directory small|medium|large
		if (args.length != 3) {
			usage();
			return;
		}

		File sourceDirectory = new File(args[0]);
		File destinationDirectory = new File(args[1]);
		String size = args[2];

		run(sourceDirectory, destinationDirectory, size);
	}

	public void run(File sourceFolder, File destinationFolder, String size) {
		System.out.println("Examining " + sourceFolder.getAbsolutePath() + " -> " + destinationFolder.getAbsolutePath() + " ...");
		if (destinationFolder.exists()) {
			System.out.println("Destination folder: " + destinationFolder.getAbsolutePath() + " exists, please remove it");
			System.exit(1);
		}

		if (sourceFolder.isDirectory()) {
			destinationFolder.mkdirs();
			imageResizer.run(new String[] { sourceFolder.getAbsolutePath(), destinationFolder.getAbsolutePath(), size });
			for (File f : sourceFolder.listFiles()) {
				if (f.isDirectory()) {
					File destination = new File(destinationFolder, f.getName());
					run(f, destination, size);
				}
			}
		}

	}

}
