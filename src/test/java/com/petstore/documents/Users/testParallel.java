package com.petstore.documents.Users;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Data
public class User {
    @test
    {
           void testParallel(){

               result result =Runner.path(-path"classpath:users")
                       outputCucumberJson(value true)
        tags("@ignore").parallel(theadCount 4);
               generaReport(results.getReportDir());

    }
    public static void  generaReport (string karateOutPutPath){

               Collection<File> jsonFiles=FileUtils.ListFiles(new File (karateOutPutPath),new String []{"json"},true);
               List<String> jsonPath =new ArrayList <> (jsonFiles.size());
               jsonFiles.forEath(file -> jsonPath.add(file.getAbsolutePath()));
               configuration comfig =new configuration (new File (pathname"buld"),proyectName "petstoreKarate")
               ReportBuilder reportBuilder=new reportBuilder (jsonPath,comfig);
               ReportBuilder.generateReports();
    }
    }



}
