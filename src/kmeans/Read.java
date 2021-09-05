package kmeans;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Read {
    public List<Record> readFile() {
        File file = new File("src/glass.csv");
        List<Record> _dataset = new ArrayList<>();

        try {
            BufferedReader _bufferedReader = new BufferedReader(new FileReader(file));

            String _storageString;
            while ((_storageString = _bufferedReader.readLine()) != null) {
                String[] _splitStringArray = _storageString.split(",",0);
                if (_splitStringArray[0].equals("RI")) continue;

                double _ri = Double.parseDouble(_splitStringArray[0]);
                double _na = Double.parseDouble(_splitStringArray[1]);
                double _mg = Double.parseDouble(_splitStringArray[2]);
                double _al = Double.parseDouble(_splitStringArray[3]);
                double _si = Double.parseDouble(_splitStringArray[4]);
                double _k = Double.parseDouble(_splitStringArray[5]);
                double _ca = Double.parseDouble(_splitStringArray[6]);
                double _ba = Double.parseDouble(_splitStringArray[7]);
                double _fe = Double.parseDouble(_splitStringArray[8]);
                int _type = Integer.parseInt(_splitStringArray[9]);

                _dataset.add(new Record(_type, _ri, _na, _mg, _al, _si, _k, _ca, _ba, _fe));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return _dataset;
    }
}
