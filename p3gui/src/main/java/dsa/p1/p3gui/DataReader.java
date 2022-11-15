package dsa.p1.p3gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DataReader {
	public ArrayList<String[]> tsvr() {
		File test2 = new File("C:\\Users\\Bansel\\Downloads\\Sample-dataset-projects (1).xlsx - Sheet1.tsv");
		ArrayList<String[]> Data = new ArrayList<>(); // initializing a new ArrayList out of String[]'s
		try (BufferedReader TSVReader = new BufferedReader(new FileReader(test2))) {
			String line = null;
			while ((line = TSVReader.readLine()) != null) {
				String[] lineItems = line.split("\t"); // splitting the line and adding its items in String[]
				Data.add(lineItems); // adding the split line array to the ArrayList
			}
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		return Data;
	}
}
