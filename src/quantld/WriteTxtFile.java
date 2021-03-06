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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.text.DecimalFormat;

/**
 *
 * @author Felix Yanhui Fan  felixfanyh@gmail.com
 */
public class WriteTxtFile{
    private final ReadTxtFile rtf = new ReadTxtFile();
    private final BatchLD bld = new BatchLD();
    private final Permutation pm;
    
    WriteTxtFile(){
        pm = new Permutation();
    }
    
    WriteTxtFile(long seed){
        pm = new Permutation(seed);
    }
    /**
     * convert milliseconds to hours, minutes and seconds
     * @param miSec milliseconds
     */
    public void miSecToHourMinSec(long miSec){
        long diffSeconds = (miSec / 1000) % 60;  
        long diffMinutes = (miSec / 1000 / 60) % 60; 
        long diffHours = (miSec / 1000 / 60 / 60) % 60;
        if(diffHours > 0){
            System.out.print(diffHours + " hours, ");
        }
        if(diffMinutes > 0){
            System.out.print(diffMinutes + " minutes, ");
        }
        System.out.println(diffSeconds + " seconds");
    }
    
    /**
     * cat multiple files to a file
     * @param files list of file names
     * @param outfile output file name
     * @throws FileNotFoundException
     * @throws IOException can not open file
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
                    fop.write(System.getProperty("line.separator").getBytes());
                    fop.flush();               
                }
            }
            fop.close();
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
     * write output to a file
     * @param outName output file name
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @throws IOException can not open file
     */
    public void outputTxt(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr) throws IOException{   
        File outfile = new File(outName);
        double[] ans = bld.batchQuantLD(fileName1, fileName2, method, winSize, ldMeasure, tol, maxItr);       
        double[] pos = rtf.readPos(fileName1, winSize);
        int n = ans.length;
        
        DecimalFormat df = new DecimalFormat("#.####");
        
        try(FileOutputStream fop = new FileOutputStream(outfile)){
            if(!outfile.exists()){
                outfile.createNewFile();
            }
                        
            for(int i=0; i<n;i++){
                String tmp = fileName1 + "\t" + fileName2 + "\t" + df.format(pos[i]) + "\t" + df.format(ans[i]) + "\n";
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
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @param start row to start read
     * @param end row to end read
     * @throws IOException can not open file
     */
    public void outputTxt(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, int start, int end, double tol, int maxItr) throws IOException{   
        File outfile = new File(outName);
        double[] ans = bld.batchQuantLD(fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr);
        double[] pos = rtf.readPos(fileName1, winSize, start, end);
        int n = ans.length;
        
        DecimalFormat df = new DecimalFormat("#.####");
        
        try(FileOutputStream fop = new FileOutputStream(outfile)){
            if(!outfile.exists()){
                outfile.createNewFile();
            }
            
            for(int i=0; i<n;i++){
                String tmp = fileName1 + "\t" + fileName2 + "\t" + df.format(pos[i]) + "\t" + df.format(ans[i]) + "\n";
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
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @param perm times of permutation
     * @param intThread threads used in permutation
     * @throws IOException can not open file
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    public void outputTxtPerm(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr, int perm, int intThread) throws IOException, InterruptedException, ExecutionException{   
        File outfile = new File(outName);
        
        double[][] ans = pm.permQuantLD(fileName1, fileName2, method, winSize, ldMeasure, tol, maxItr, perm, intThread);
        double[] pos = rtf.readPos(fileName1, winSize);
        int n = ans.length;
        
        DecimalFormat df = new DecimalFormat("#.####");
        
        try(FileOutputStream fop = new FileOutputStream(outfile)){
            if(!outfile.exists()){
                outfile.createNewFile();
            }
                        
            for(int i=0; i<n;i++){
                String tmp = fileName1 + "\t" + fileName2 + "\t" + df.format(pos[i]) + "\t" + df.format(ans[i][0]) + "\t" + df.format(ans[i][1]) + "\t" + df.format(ans[i][2]) + "\n";
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
     * @param start row to start read
     * @param end row to end read
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @param perm times of permutation
     * @param intThread threads used in permutation
     * @throws IOException can not open file
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    public void outputTxtPerm(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, int start, int end, double tol, int maxItr, int perm,int intThread) throws IOException, InterruptedException, ExecutionException{   
        File outfile = new File(outName);
        
        double[][] ans = pm.permQuantLD(fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr, perm, intThread);
        double[] pos = rtf.readPos(fileName1, winSize, start, end);
        int n = ans.length;
        
        DecimalFormat df = new DecimalFormat("#.####");
        
        try(FileOutputStream fop = new FileOutputStream(outfile)){
            if(!outfile.exists()){
                outfile.createNewFile();
            }
                        
            for(int i=0; i<n;i++){
                String tmp = fileName1 + "\t" + fileName2 + "\t" + df.format(pos[i]) + "\t" + df.format(ans[i][0]) + "\t" + df.format(ans[i][1]) + "\t" + df.format(ans[i][2]) + "\n";
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
     * run quantLD block by block
     * @param outName output file name
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @param nrow read how many rows each time
     * @throws IOException can not open file
     */
    public void runQuantLD(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr, int nrow) throws IOException{
        int n = rtf.countLines(fileName1);
        System.out.println("There are total " + n + " variants in " + fileName1);
        if(n <= nrow){
            System.out.println("processing ...");
            outputTxt(outName, fileName1, fileName2, method, winSize, ldMeasure, tol, maxItr);
        }else{
            int totsplit;
            int start = 1;
            int end = nrow;
            String tmpdirstr = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
            File tmpdir = new File(tmpdirstr);
            boolean bdir = tmpdir.mkdir();
            if(!bdir){
                System.out.println("can not create temp dir");
                System.exit(7);
            }
            String tmpname = tmpdirstr + "/tmp0";
            System.out.println("processing markers from " + start + " to " + end + " (total: " + n + ") ...");
            outputTxt(tmpname, fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr);
            for(totsplit=1;;totsplit++){
                tmpname = tmpdirstr + "/tmp" + totsplit;
                start = end - winSize + 2;
                end = start + nrow - 1;
                if(end >= n){
                    end = n;
                }
                System.out.println("processing markers from " + start + " to " + end + " (total: " + n + ") ...");
                outputTxt(tmpname, fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr);
                if(end == n){
                    break;
                }
            }
 
            List<String> fileList = new ArrayList<>();
            List<String> dirList = new ArrayList<>();
            for(int i=0; i<=totsplit;i++){
                fileList.add(tmpdirstr + "/tmp"+i);
            }
            mergeTxtFiles(fileList,outName);
            deleteTxtFiles(fileList);
            dirList.add(tmpdirstr);
            deleteTxtFiles(dirList);
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
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @param nrow read how many rows each time
     * @param perm times of permutation
     * @param intThread threads used in permutation
     * @throws IOException can not open file
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    public void runQuantLDPerm(String outName, String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr, int nrow, int perm,int intThread) throws IOException, InterruptedException, ExecutionException{
        int n = rtf.countLines(fileName1);
        System.out.println("There are total " + n + " variants in " + fileName1);
        if(n <= nrow){
            System.out.println("processing ...");
            outputTxtPerm(outName, fileName1, fileName2, method, winSize, ldMeasure, tol, maxItr, perm, intThread);
        }else{
            int totsplit;
            int start = 1;
            int end = nrow;
            String tmpdirstr = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
            File tmpdir = new File(tmpdirstr);
            boolean bdir = tmpdir.mkdir();
            if(!bdir){
                System.out.println("can not create temp dir");
                System.exit(7);
            }
            String tmpname = tmpdirstr + "/tmp0";
            System.out.println("processing markers from " + start + " to " + end + " (total: " + n + ") ...");
            outputTxtPerm(tmpname, fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr, perm,intThread);
            for(totsplit=1;;totsplit++){
                tmpname = tmpdirstr + "/tmp" + totsplit;
                start = end - winSize + 2;
                end = start + nrow - 1;
                if(end >= n){
                    end = n;
                }
                System.out.println("processing markers from " + start + " to " + end + " (total: " + n + ") ...");
                outputTxtPerm(tmpname, fileName1, fileName2, method, winSize, ldMeasure, start, end, tol, maxItr, perm,intThread);
                if(end == n){
                    break;
                }
            }
 
            List<String> fileList = new ArrayList<>();
            List<String> dirList = new ArrayList<>();
            for(int i=0; i<=totsplit;i++){
                fileList.add(tmpdirstr + "/tmp"+i);
            }
            mergeTxtFiles(fileList,outName);
            deleteTxtFiles(fileList);  
            dirList.add(tmpdirstr);
            deleteTxtFiles(dirList);
        }
    }
       
}
