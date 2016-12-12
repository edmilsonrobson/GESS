package gess;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

public class CSVExporter {
	
	public void Export(String filename, List<Entry> entries){
		
		PrintWriter pw = null;
		try {
			File file = new File(filename + ".csv");
			pw = new PrintWriter(file);
			System.out.println(file.getAbsolutePath());
			
			
			StringBuilder sb = new StringBuilder();
			for (Entry entry : entries){
				sb.append(entry.entryCSV);
				sb.append("\n");
			}
			pw.write(sb.toString());
		    pw.close();
		    System.out.println("done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
