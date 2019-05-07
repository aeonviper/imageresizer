package imageresizer;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileFilter;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

public class ImageResizer {

	public static void main(String[] args) {
		new ImageResizer().run(args);
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
		Integer height, width;

		if (sourceDirectory.exists() && sourceDirectory.isDirectory() && sourceDirectory.canRead()) {
			if (sourceDirectory.listFiles().length == 0) {
				return;
			}
		} else {
			System.err.println("Source directory: " + sourceDirectory.getName() + " invalid");
			return;
		}

		if (destinationDirectory.exists() && destinationDirectory.isDirectory() && destinationDirectory.canWrite()) {
		} else {
			System.err.println("Destination directory: " + destinationDirectory.getName() + " invalid");
			return;
		}

		width = height = null;

		if ("small".equals(size) || "medium".equals(size) || "large".equals(size)) {
			if ("small".equals(size)) {
				width = 480;
				height = 360;
			} else if ("medium".equals(size)) {
				width = 640;
				height = 480;
			} else if ("large".equals(size)) {
				width = 1024;
				height = 768;
			}
		} else if (size.matches("[0-9]+x[0-9]+")) {
			Pattern pattern = Pattern.compile("([0-9]+)x([0-9]+)");
			Matcher matcher = pattern.matcher(size);
			if (matcher.matches()) {
				width = Integer.parseInt(matcher.group(1));
				height = Integer.parseInt(matcher.group(2));
			}
		} else {
			usage();
			return;
		}

		if (width == null || height == null) {
			width = 1024;
			height = 768;
		}

		System.out.println("Resizing to width: " + width + " and height: " + height);

		try {
			File[] fileArray = sourceDirectory.listFiles(new FileFilter() {
				public boolean accept(File f) {
					return f.isFile();
				}
			});
			if (fileArray.length > 0) {
				Thumbnails.of(fileArray).size(width, height).toFiles(destinationDirectory, Rename.NO_CHANGE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
