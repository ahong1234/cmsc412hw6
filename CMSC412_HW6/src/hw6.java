import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

public class hw6 {
	static File directory;
	static boolean running = true;
	static File[] d;
	static boolean dirLoaded = false;
	
	public static void menu() {
		System.out.println("0 - Exit");
		System.out.println("1 - Select directory");
		System.out.println("2 - List directory content");
		System.out.println("3 - Display file (hexadecimal view)");
		System.out.println("4 - Delete file");
		System.out.println("5 - Mirror reflect file (byte level)");
		System.out.println("Select option:");
	}
	
	public static void processChoice(int choice) {
		switch(choice) {
		case 0:
			System.out.println("Goodbye!");
			System.exit(0);
			break;
		case 1:
			selectDir();
			break;
		case 2:
			listContent(directory);			
			break;
		case 3:		
			getFileName(directory, 1);		
			break;
		case 4:
			getFileName(directory, 2);
			break;
		case 5:
			getFileName(directory, 3);
			break;
		default:
			break;	
		}
	}
	
	public static void convertToHex(File f) {
			try {
			String filePath = f.getAbsolutePath();
			Path path = Paths.get(filePath);
			StringBuilder result = new StringBuilder();
	        StringBuilder hex = new StringBuilder();
	        StringBuilder input = new StringBuilder();
	
	        int count = 0;
	        int value;
	
	        try (InputStream inputStream = Files.newInputStream(path)) {
	
	            while ((value = inputStream.read()) != -1) {
	
	                hex.append(String.format("%02X ", value));
	
	                //If the character is unable to convert, just prints a dot "."
	                if (!Character.isISOControl(value)) {
	                    input.append((char) value);
	                } else {
	                    input.append(".");
	                }
	
	                
	                if (count == 15) {
	                    result.append(String.format("%-60s\n", hex));
	                    hex.setLength(0);
	                    input.setLength(0);
	                    count = 0;
	                } else {
	                    count++;
	                }
	
	            }
	
	            // if the count>0, meaning there is remaining content
	            if (count > 0) {
	                result.append(String.format("%-60s", hex));
	            }
	            System.out.println(result.toString());
	        } catch (IOException e) {
				System.out.println("IOException detected");
		}
			}
			catch(NullPointerException e) {
				System.out.println("file not found");
			}    
	}
	
	public static void deleteFile(File f) {
		try {
		if(f.delete()) {
			System.out.println(f.getName() + " deleted");
		}
		else {
            System.out.println("Failed to delete the file");
        }
		}
		catch (NullPointerException e) {
			System.out.println("file not found");
		}
	}
	
	public static void getFileName(File f, int choice) {
		File temp = null;
		boolean fileFound = false;
		if (dirLoaded) {
			Scanner stdin3 = new Scanner(System.in);
			System.out.println("enter file name: ");
			String fileName = stdin3.nextLine();
			for (File e : d) {
				if (fileName.equals(e.getName())) {
					System.out.println("File found: " + e.getName());
					temp = e;
					fileFound = true;
					break;
				}	
			}
			switch (choice) {
			case 1:
				convertToHex(temp);
				break;
			case 2:
				deleteFile(temp);
				break;
			case 3:
				break;
			}	
		}
		else {
			System.out.println("No directory selected.");
		}
		
	
	}
	
	public static void selectDir() {
		Scanner stdin2 = new Scanner(System.in);
		
		
		System.out.println("Enter path of directory: ");
		String path = stdin2.nextLine();
		
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory()) {
			System.out.println("directory found at: " + directory.getAbsolutePath());
			d = directory.listFiles();
			dirLoaded = true;
		}
		else {
			System.out.println("directory not found");
		}	
		if (d == null) {
			System.out.println("directory is null");
		}	
	}
	
	public static Path getPath(File e) {
		Path path = null;
		try {
			path = Paths.get(e.getCanonicalPath());
		} 
		catch (IOException e1) {
			System.out.println("IOException detected");
		}
		return path;
	}
	
	public static void listContent(File f) { 
		try {
			long fileSize = 0;	
			for(File e : d) {
				if (e.isDirectory()) {
					System.out.println("DIRECTORY: \"" + e.getName());						
				}
			}
			for(File e : d) {
				if (e.isFile()) {
					try {
						fileSize = Files.size(getPath(e));
					} catch (IOException e1) {
						System.out.println("IOException detected");
					}
					System.out.println("FILE: \"" + e.getName() + "\" Size: " + fileSize + " bytes");
				} 
			}
		}
		catch (NullPointerException e) {
			System.out.println("no directory selected");
		}	
	}
	
	public static void main(String[] args) {	
		while(running) {
			Scanner stdin = new Scanner(System.in);	
			menu();
			try {
			int input = stdin.nextInt();
			processChoice(input);
			}
			catch (InputMismatchException e) {
				System.out.println("please enter an integer 0-5");
			}
		}		
	}
}
