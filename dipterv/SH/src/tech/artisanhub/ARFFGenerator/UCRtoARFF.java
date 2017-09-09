package tech.artisanhub.ARFFGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tech.artisanhub.ShapeletTrainer1D.UCRUtils;

public class UCRtoARFF {

    private static final String TRAINING_DATA = "/home/rnavagamuwa/Documents/CSE/FYP/Datasets/DEBS-2013-SoccerField/result_1st_goal.ucr";

    private static final String OUTPUT_DATA = "/home/rnavagamuwa/Documents/CSE/FYP/Datasets/DEBS-2013-SoccerField/ARFF_result_1st_goal.arff";

    private static final String CR = "\n";

    private static final Object COMMA = ",";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Map<String, List<double[]>> trainData = UCRUtils.readUCRData(TRAINING_DATA);
        int seriesLength = trainData.entrySet().iterator().next().getValue().get(0).length;

        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(OUTPUT_DATA)));

        bw.write("@relation  TSDM" + CR);
        for (int i = 0; i < seriesLength; i++) {
            bw.write("@attribute\tAtt" + i + "\treal" + CR);

        }

        StringBuffer sb = new StringBuffer("@attribute target  {");
        for (String key : trainData.keySet()) {
            sb.append(String.valueOf(key)).append(COMMA);
        }
        sb.delete(sb.length() - 1, sb.length()).append("}").append(CR);
        sb.append(CR).append("@data").append(CR);
        bw.write(sb.toString());

        for (Entry<String, List<double[]>> e : trainData.entrySet()) {
            for (double[] series : e.getValue()) {
                bw.write(Arrays.toString(series).replace("[", "").replace("]", "").replace(", ", ","));
                bw.write(COMMA + e.getKey() + CR);
            }
        }

        bw.close();
    }
}
