package exam_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHelperManger {
	public static void createNewFile(String fileName) {
		try {
			File myObj = new File(fileName + ".txt");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static ArrayList<String> readFromFile(File myFile) throws FileNotFoundException {
		ArrayList<String> allStrings = new ArrayList<String>();
		Scanner myReader = new Scanner(myFile);
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			allStrings.add(data);
		}
		myReader.close();
		return allStrings;
	}

	public static void write(File fileDB, ArrayList<String> fileContent) throws IOException {
		PrintWriter myWriter = new PrintWriter(fileDB);
		for (int i = 0; i < fileContent.size(); i++) {
			String dataLine = fileContent.get(i);
			myWriter.print(dataLine);
			if (i != fileContent.size() - 1) {
				myWriter.println();
			}			
		}
		myWriter.close();
		System.out.println("Successfully wrote to the file.");
	}

	

}
