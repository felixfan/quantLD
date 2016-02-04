/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quantld;

import java.io.*;
        
/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */
public class ReadTxtFile {
    /**
     * returns the number of lines of a file
     * @param fileName file in PLINK tped format
     * @return number of lines in tped file
     * @throws java.io.IOException
    */
    private int countLines(String fileName) throws IOException{
        BufferedReader bf = null;
        String lineContent = null;
        int n = 0;
        try{
            bf = new BufferedReader(new FileReader(fileName));
            while((lineContent = bf.readLine())!= null){
                n++;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace(System.out);
        }catch(IOException e){
            e.printStackTrace(System.out);
        }finally{
            if(bf != null){
                bf.close();
            }
        }
        return n;
    }
    
    /**
     * find major allele for each SNP
     * @param gtp a two-dimension array, output of readTpedFile
     * @return array of major allele for each SNP
    */
    private String[] getMajorAllele(String gtp[][]){
        int i = 0;
        int n = gtp.length;
        String ma[] = new String[n];
        for(String snp[] : gtp){
            int a = 0;
            int c = 0;
            int g = 0;
            int t = 0;
            for(int j = 4; j< snp.length; j++){
                switch (snp[j]) {
                    case "A":
                        a++;
                        break;
                    case "C":
                        c++;
                        break;
                    case "G":
                        g++;
                        break;
                    case "T":
                        t++;
                        break;
                }
            }
            
            if((a+c+g+t) > 0){
                if(a>=c && a>=g && a>=t){
                    ma[i] = "A";
                }else if(c>=a && c>=g && c>=t){
                    ma[i] = "C";
                }else if(g>=a && g>=c && g>=t){
                    ma[i] = "G";
                }else if(t>=a && t>=c && t>=g){
                    ma[i] = "T";
                }
            }else{
                ma[i] = "-9";
            }
            i++;
        }
        return ma;
    }
    
    /**
     * read tped file to a two-dimension array
     * @param fileName file in PLINK tped format
     * @return a two-dimension array
     * @throws java.io.IOException 
    */
    private String[][] readTpedFile(String fileName) throws IOException{
        BufferedReader bf = null;
        String lineContent;
        int i = 0;
        int n = countLines(fileName);
        String dat[][] = new String[n][];
        try{
            bf = new BufferedReader(new FileReader(fileName));
            while((lineContent = bf.readLine())!= null){
                String str[] = lineContent.split("\\s+"); // split by white spaces
                dat[i] = new String[str.length];
                dat[i] = str;
                i++;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace(System.out);
        }catch(IOException e){
            e.printStackTrace(System.out);
        }finally{
            if(bf != null){
                bf.close();
            }
        }
        return dat;
    }
    
    /**
     * read lines between "start" and "end" of tped file to a two-dimension array
     * @param fileName file in PLINK tped format
     * @param start line to start read
     * @param end line to end read
     * @return a two-dimension array
     * @throws java.io.IOException
    */
    private String[][] readTpedFile(String fileName, int start, int end) throws IOException{
        BufferedReader bf = null;
        String lineContent;
        int i = 0;
        int l = 0;
        int n = countLines(fileName);
        String dat[][] = new String[n][];
        try{
            bf = new BufferedReader(new FileReader(fileName));
            while((lineContent = bf.readLine())!= null){
                i++;
                if(i >= start && i <= end){
                    String str[] = lineContent.split("\\s+"); // split by white spaces
                    dat[l] = new String[str.length];
                    dat[l] = str;
                    l++;
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace(System.out);
        }catch(IOException e){
            e.printStackTrace(System.out);
        }finally{
            if(bf != null){
                bf.close();
            }
        }
        return dat;
    }
    
    /**
     * code the genotypes for each SNP
     * homozygous of major allele was coded as 2
     * heterozygous was coded as 1
     * homozygous of minor allele was coded as 0
     * missing data was coded as -9
     * @param gtp a two-dimension array, output of readTpedFile
     * @return a two-dimension array
    */
    private int[][] codeGenotype(String gtp[][]){
        String ma[] = getMajorAllele(gtp);
        int genoCode[][];
        genoCode = new int[gtp.length][(gtp[0].length-4)/2];
        int n = 0;
        for(String snp[] : gtp){
            int m = 0;
            for(int i = 4; i < snp.length -1; i += 2){
                int j = i + 1;
                if(snp[i].equals(snp[j])){
                    if(snp[i].equals(ma[n])){
                        genoCode[n][m] = 2;
                    }else if(snp[i].equals("0") || snp[i].equals("-9")){
                        genoCode[n][m] = -9;
                    }else{
                        genoCode[n][m] = 0;
                    }
                }else{
                    genoCode[n][m] = 1;
                }
                m++;
            }
            n++;
        }
        
        return genoCode;
    }
    
    /**
     * code the genotypes for each SNP
     * homozygous of major allele was coded as 2
     * heterozygous was coded as 1
     * homozygous of minor allele was coded as 0
     * missing data was coded as -9
     * @param fileName file in PLINK tped format
     * @return a two-dimension array
     * @throws java.io.IOException
    */
    public int[][] recodeGenotype(String fileName) throws IOException{
        String gtp[][] = readTpedFile(fileName);
        int genoCode[][] = codeGenotype(gtp);
        return genoCode;
        
    }
    
    /**
     * code the genotypes for each SNP
     * homozygous of major allele was coded as 2
     * heterozygous was coded as 1
     * homozygous of minor allele was coded as 0
     * missing data was coded as -9
     * @param fileName file in PLINK tped format
     * @param start line to start read
     * @param end line to end read
     * @return a two-dimension array
     * @throws java.io.IOException
    */
    public int[][] recodeGenotype(String fileName, int start, int end) throws IOException{
        String gtp[][] = readTpedFile(fileName, start, end);
        int genoCode[][] = codeGenotype(gtp);
        return genoCode;
    }
}
