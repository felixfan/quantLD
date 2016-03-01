/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quantld;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Felix Yanhui Fan  felixfanyh@gmail.com
 */
public class WriteTxtFile extends BatchLD {
    private final ReadTxtFile rtf = new ReadTxtFile();
    
    public void outputTxt(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr) throws IOException{   
        File outfile = new File(outName);

        double[] ans = batchQuantLD(fileName1, fileName2, method, winSize, ldMeasure, tol, maxItr);
        double[] pos = rtf.readPos(fileName1, winSize);
        int n = ans.length;
        try(FileOutputStream fop = new FileOutputStream(outfile)){
            if(!outfile.exists()){
                outfile.createNewFile();
            }
            
            for(int i=0; i<n;i++){
                String tmp = pos[i] + "\t" + ans[i] + "\n";
                byte[] contentInBytes = tmp.getBytes();
                fop.write(contentInBytes);
                fop.flush();               
            }
            
            fop.close();
        }catch(IOException e){
            e.printStackTrace(System.out);
        }
        
        
        
        
    }
}
