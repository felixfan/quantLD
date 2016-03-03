/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quantld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felix Yanhui Fan  felixfanyh@gmail.com
 */
public class WriteTxtFile extends BatchLD {
    private final ReadTxtFile rtf = new ReadTxtFile();
    
    /**
     * write output to a file
     * @param outName output file name
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is <= tol.
     * @param maxItr maximum iterate
     * @throws IOException 
     */
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
    
    /**
     * write output to a file
     * @param outName output file name
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is <= tol.
     * @param maxItr maximum iterate
     * @param start row to start read
     * @param end row to end read
     * @throws IOException 
     */
    public void outputTxt(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, int start, int end, double tol, int maxItr) throws IOException{   
        File outfile = new File(outName);

        double[] ans = batchQuantLD(fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr);
        double[] pos = rtf.readPos(fileName1, winSize, start, end);
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
    
    /**
     * cat multiple files to a file
     * @param files list of file names
     * @param outfile output file name
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void mergeTxtFiles(List<String> files, String outfile) throws FileNotFoundException, IOException{
        try (FileOutputStream fop = new FileOutputStream(outfile)) {
            BufferedReader bf;
            String line;
            for(String f : files){
                bf = new BufferedReader(new FileReader(f));
                while((line = bf.readLine())!= null){
                    byte[] contentInBytes = line.getBytes();
                    fop.write(contentInBytes);
                    fop.flush();               
                }
            }
        }
    }
    
    /**
     * delete list of files
     * @param files list of file names
     */
    private void deleteTxtFiles(List<String> files){
        for(String f : files){
            File t = new File(f);
            t.delete(); 
        }
    }
    
    /**
     * run quantLD block by block
     * @param outName output file name
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is <= tol.
     * @param maxItr maximum iterate
     * @param nrow rows to read for each time
     * @throws IOException 
     */
    public void runQuantLD(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr, int nrow) throws IOException{
        int n = rtf.countLines(fileName1);
        if(n < nrow){
            outputTxt(outName, fileName1, fileName2, method, winSize, ldMeasure, tol, maxItr);
        }else{
            int totlen = n + winSize -1;
            int totsplit;
            if(totlen % nrow == 0){
                totsplit = (n + winSize - 1) / nrow;
            }else{
                totsplit = (n + winSize - 1) / nrow + 1;
            }
            int start = 1;
            int end = nrow;
            String tmpname = "tmp0";
            outputTxt(tmpname, fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr);
            for(int i=1;i<totsplit;i++){
                tmpname = "tmp" + i;
                start = end - winSize + 2;
                end = start + nrow - 1;
                if(end > n){
                    end = n;
                }
                outputTxt(tmpname, fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr);
            }
 
            List<String> fileList = new ArrayList<>();
            for(int i=0; i<totsplit;i++){
                fileList.add("tmp"+i);
            }
            mergeTxtFiles(fileList,outName);
            deleteTxtFiles(fileList);
        }
    }
}
