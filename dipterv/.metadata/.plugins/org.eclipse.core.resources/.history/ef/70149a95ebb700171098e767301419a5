package tech.artisanhub.ShapeletTrainerMD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;


public class FlightDataProcessor {

	public static void main(String[] args) throws IOException {
		ArrayList<ArrayList<String>> lines = CSVReader.read("BUDflights2007-2012v2.csv", ";");
		
    	// propertyk beolvasása
    	Properties prop = new Properties();
    	InputStream input = null;
    	input = new FileInputStream("config.properties");
    	prop.load(input);
    	
        int dim = Integer.parseInt(prop.get("dimension").toString());
		//lengthOfTS elemú az idõsor, timeWindow db hónapot nézünk
		//és a rákövetkezõ (timeWindow+1.) hónap alapján határozzuk meg, 
		//hogy kritikus helyzet van-e
		//akkor van krit. helyzet, ha a timeWindow darab elemnek az átlagánál nagyobb a tw+1.
		int lengthOfTS = 12;
		int timeWindow = 6;
		
		//le van rendezve repülõtér szerint - ez elvárás a csv-vel szemben
		int sumAirport = 0;
		//amikor reptér váltás van le kell 0-zni az összegzést
		String lastAirport = "";
		int lastMonth = 0;
		ArrayList<String> header = lines.get(0);
		//DESTINATION;
		int destination = 0;
		//COMMERCIAL FLAG;
		int commercialFlag = 1;
		//CITY;
		int city = 2;
		//COUNTRY;
		int country = 3;
		//FLIGH DIRECTION;
		int flightDirection = 4;
		//FLIGHT TYPE;
		int flightType = 5;
		//DATE;
		int date = 6;
		//DATE YEAR;
		int dateYear = 7;
		//DATE HALF YEAR;
		int dateHalfYear = 8;
		//DATE YEAR QUARTER;
		int dateYearQuarter = 9;
		//DATE YEAR MONTH;
		int dateYearMonth = 10;
		//NBR OF PASSENGERS;
		//VAR1
		int passengers = 11;
		//CARGO WEIGHT;
		int cargoWeight = 12;
		//NBR OF FLIGHTS;
		//VAR2
		int flights = 13;
		//VAR3
		//SEAT CAPACITY
		int seatCapacity = 14;

/*		for (int i = 0; i<15;i++){
			System.out.println(header.get(i));
		}
*/	
		//processed lines: vektorok listájának a listáját tároljuk
		//minden reptérhez 1 sor: abban vektorok listája (havonta 1 vektor)
		//egy vektor: a magyarázó változók
		ArrayList<ArrayList<ArrayList<Integer>>> procLines = new ArrayList<ArrayList<ArrayList<Integer>>>();
		//aktuális airport amelyiknél tartunk
		int airportIndex = -1;
		//a reptérnek hányadik hónapeleménél tartunk, hány hónapot adtunk eddig hozzá a reptérhez
		int monthIndex = -1;
		//headert kihagyva végigmegyünk a beolvasott sorokon
		for (int i = 1; i < lines.size(); i++){
			//az év+hónap együtt a sorban ahol tartunk
			int month = Integer.parseInt(lines.get(i).get(dateYearMonth));
			//a negyedév sorszáma ahol tartunk
			int quarter = Integer.parseInt(lines.get(i).get(dateYearQuarter).substring(5));
			//ezt akkor csinálja végig, ha új reptér adataihoz érünk
			if (!lastAirport.equals(lines.get(i).get(destination)) || ( (lastMonth + 1 < month) && !((month % 100 == 1) && ((lastMonth % 100) == 12 )) ) ){
				airportIndex++;
				//új reptér -> új sor (=új elem a procLinesba)
				procLines.add(new ArrayList<ArrayList<Integer>>());
				//új repteret állítjuk be a legutolsó reptérnek
				lastAirport = lines.get(i).get(destination);
				//újrakezdjük, hogy hányadik eleménél vagyunk a reptérnek
				monthIndex = -1;
				//ezért nincs is elõzõ hónap
				lastMonth = 0;
			}
			//ezt akkor csinálja végig, ha új hónaphoz érünk
			if (lastMonth != month){
				monthIndex++;
				//az adott hónaphoz új vektort hozunk létre üresen (0-kat ezért adunk hozzá)
				ArrayList<Integer> monthData = new ArrayList<Integer>();
				monthData.add(0);
				monthData.add(0);
				monthData.add(0);
				//pl. 201702 formátumból a 02-t kivesszük
				monthData.add(month % 100);
				//monthData.add(0); //TODO visszaállítani
				//negyedév sorszámának hozzáadása
				monthData.add(quarter);
				//monthData.add(0); //TODO visszaállítani
				//hozzáadjuk a procLines adott reptérhez tartozó eleméhez az aktuális hónaphoz tartozó vektort
				procLines.get(airportIndex).add(monthData);
				lastMonth = month;
			}
			//aggregáljuk az adott hónaphoz tartozó értékeket változónként
			procLines.get(airportIndex).get(monthIndex).set(0, procLines.get(airportIndex).get(monthIndex).get(0) + Integer.parseInt(lines.get(i).get(passengers)));
			procLines.get(airportIndex).get(monthIndex).set(1, procLines.get(airportIndex).get(monthIndex).get(1) + Integer.parseInt(lines.get(i).get(flights)));
			procLines.get(airportIndex).get(monthIndex).set(2, procLines.get(airportIndex).get(monthIndex).get(2) + Integer.parseInt(lines.get(i).get(seatCapacity)));
		}
/*		for (int i = 0; i < procLines.size(); i++){
			for (int j = 0; j < procLines.get(i).size(); j++){
				System.out.println(procLines.get(i).get(j));
			}
		}
		*/
		
		//flights.csv: ez lesz a logunk, amit az sh keresés felhasznál
		PrintWriter pw = new PrintWriter(new File("flights.csv"));
		StringBuilder outputHeader = new StringBuilder();
		
		//header
		for (int i = 0; i < lengthOfTS * dim; i++){
			outputHeader.append("attr"+ i + ",");
		}
		outputHeader.append("attr"+ lengthOfTS * dim + "\n");
		pw.write(outputHeader.toString());
		
		//egy reptér
		for (int i = 0; i < procLines.size(); i++){
			//az adott reptér hónapjai = itt fog kezdõdni az adott idõsor
			//(ezért vonjuk le a hosszát)
			for (int j = 0; j < procLines.get(i).size() - lengthOfTS; j++) {
				StringBuilder outputLine = new StringBuilder();
				//sum: az átlagszámításhoz az összeg
				int sum = 0;
				//itt hozzuk létre magát az idõsort
				//egyesével készítjük az elemeit (vektorokon megyünk végig)
				//j. vektortól kezdjük
				for (int k = j; k < j + lengthOfTS; k++) {
					//n-nel a vektor elemein megyünk végig
					for (int n = 0; n < procLines.get(i).get(k).size(); n++) {
						//i. reptér k. hónapjának n. vektorelemét írjuk ki
//						if (n >= 3) 
						// 0: utasszám 1: járatdbszám 2:üléskapacitás 3:hónap 4:negyedév
						if (n==0 || n==1 || n==2 || n==3 || n==4)
						outputLine.append(procLines.get(i).get(k).get(n) + ",");
					}
					//azért lesz itt get(0), mert a 0. az utasok száma
					if (k >= j + lengthOfTS - timeWindow)	
						sum += procLines.get(i).get(k).get(0);
				}
				if (procLines.get(i).get(j + lengthOfTS).get(0) * timeWindow > sum) {
					//kritikus
					outputLine.append("1");
				}
				else  {
					//nem kritikus
					outputLine.append("0");
				}
				
				outputLine.append("\n");
				pw.write(outputLine.toString());
					
			}
		}
		
		pw.close();
		
	}

}
