package tech.artisanhub.ShapeletTrainer1D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class CSVtoCSVs {
	public static void main(String[] args) throws Exception {

		int ts1 = 0;
		int ts2 = 0;
		int count = 1;

		BufferedReader br = new BufferedReader(new FileReader("baseline_SLA.csv"));
		PrintWriter pw = new PrintWriter(new File("dataset_outputs/out" + count + ".csv"));
		String line = br.readLine();
		String header = line;
		
		pw.write(header + "\n");
		
		line = br.readLine();
		
		do {
			String[] sh = line.split(",");
			ts2 = Integer.parseInt(sh[0]);
			if (ts2 < ts1) {
				pw.close();
				count++;
				pw = new PrintWriter(new File("dataset_outputs/out" + count + ".csv"));
				pw.write(header + "\n");
				pw.write(line + "\n");
			} else {
				pw.write(line + "\n");
			}
			ts1 = ts2;
			line = br.readLine();
		} while (line != null);

		pw.close();
		br.close();
	}
}
