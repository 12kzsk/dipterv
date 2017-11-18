package com.sample.svm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import tech.artisanhub.ShapeletTrainerMD.CSVReader;
import tech.artisanhub.ShapeletTrainerMD.DoubleVectorMD;
import tech.artisanhub.ShapeletTrainerMD.LearnShapeletsMD;
import tech.artisanhub.ShapeletTrainerMD.ShapeletFilterMD;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTestSVM {

	public static String cls = "";
	public static Integer dimension;
	public static svm_model model;
	
	public static final void main(String[] args) throws IOException {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

        	//get props
        	Properties prop = new Properties();
    		InputStream input = null;
    		try {
    			input = new FileInputStream("C:/Users/Zsuzsi/git/dipterv/SH/config.properties");
    			prop.load(input);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
        	String modelSource = "C:/Users/Zsuzsi/git/dipterv/SH/" + prop.getProperty("svm_model_file_name");
        	dimension = Integer.parseInt(prop.getProperty("dimension"));

    		model = svm.svm_load_model(modelSource);
    				
        	//get input (SVMtest.txt, prop.getProperty("SVMtestinput"))
    		//BufferedReader br = new BufferedReader(new FileReader(prop.getProperty("SVMtestinput")));
    		BufferedReader br = new BufferedReader(new FileReader("SVMtest.txt"));
    		
    		String line;
    		while (true){
    			line = br.readLine();
    			
    			if (line == null || line == "") break;
    			
    			StringTokenizer st = new StringTokenizer(line, "\t\n\r\f: ");
//    			System.out.println(st.nextToken());
    			double target = atof(st.nextToken());
    			int m = st.countTokens()/2;
    			svm_node[] x = new svm_node[m];
    			for(int j=0;j<m;j++)
    			{
    				x[j] = new svm_node();
    				x[j].index = atoi(st.nextToken());
    				x[j].value = atof(st.nextToken());
    			}
    		
    			LogItemSVM logItemSVM = new LogItemSVM(dimension);
        		DoubleVectorMD inVec = new DoubleVectorMD(dimension);
        		for (int j = 0; j < dimension; j++){
        			inVec.setElement(j, x[j].value);
        		}

            	logItemSVM.setLogItemSVM(inVec);
                kSession.insert(logItemSVM);
            	
            	//szab�lyok ki�rt�kel�se
                kSession.fireAllRules();
    		}
    }

    public static class LogItemSVM {

    	public LogItemSVM(Integer dimension){
    		this.logItemSVM = new DoubleVectorMD(dimension);
    	}
    	
        private DoubleVectorMD logItemSVM;

        public DoubleVectorMD getLogItemSVM() {
            return this.logItemSVM;
        }

        public void setLogItemSVM(DoubleVectorMD logItemSVM) {
            this.logItemSVM = logItemSVM;
        }
    }

	private static double atof(String s) {
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			System.err.print("NaN or Infinity in input\n");
			System.exit(1);
		}
		return (d);
	}

	private static int atoi(String s) {
		return Integer.parseInt(s);
	}

}