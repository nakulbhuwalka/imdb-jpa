package com.example.imdbjpa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataReader {

    private static Path path = Paths.get("/home/nakul/Downloads/imdb");

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File[] files = path.toFile().listFiles();
        for (File file : files) {
            System.out.println(file);

            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(file));

            BufferedReader bis = new BufferedReader(new InputStreamReader(gis));
            List<Integer> max = new ArrayList<>();
            StringBuilder builder = new StringBuilder();

            bis.lines().forEach(line -> {
                boolean newMax=false;
                String[] fields = line.split("\t");
                if(builder.isEmpty())
                builder.append(line);

                if (max.isEmpty()) {
                    for (int i = 0; i < fields.length; i++) {
                        max.add(fields[i].length());
                    }
                } else {
                    for (int i = 0; i < fields.length; i++) {
                        if(fields[i].length() > max.get(i))
                        {
                            max.set(i, fields[i].length());
                            newMax =true;
                        }
                    
                    }
                }
                if(newMax)
                {
                    builder.setLength(0);
                    builder.append(line);
                //     System.out.println(line);
                //     System.out.println(max);
                }

            });

            System.out.println(builder);
            System.out.println(max);
            

        }

    }

}
