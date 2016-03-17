package quantld;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */

import java.util.HashMap;
import org.apache.commons.cli.*;

public class JOptsParse {  
    private void printHelp(Options opts) {  
        HelpFormatter hf = new HelpFormatter();  
        hf.printHelp(" ", opts);  
    }
    
    /**
     * command line processing by using Apache Commons CLI
     * @param args arguments array from main()
     * @return HashMap of options
     * @throws ParseException can not parse opts
     */
    public HashMap<String, String> getOpt(String[] args) throws ParseException{
        HashMap<String, String> params = new HashMap<>();
        // create Options object
        Options options = new Options();
        // create the boolean options
        Option help = new Option("h", "help",false, "print this message" );
        Option version = new Option("v","version",false, "print the version information and exit");
        // add the boolean options
        options.addOption(help);
        options.addOption(version);
        // add other options
        options.addOption("f", "first-tped", true, "first input genotype file, required");
        options.addOption("s", "second-tped", true, "second input genotype file, required");
        options.addOption("tol", "conv-tolerance", true, "convergence tolerance, default 0.001");
        options.addOption("iter", "max-iteration", true, "maximum number of EM steps, default 1000");
        options.addOption("m", "ld-measure", true, "LD measure, default r2");
        options.addOption("w", "win-size", true, "window size, default 50");
        options.addOption("d", "ld-diff-measure", true, "method to measure LD difference, default evd");
        options.addOption("o", "out", true, "output file name, default output.txt");
        options.addOption("nr", "num-rows", true, "read how many rows each time, default 1000");
        options.addOption("p", "perm", true, "times of permutation, default 0");
        options.addOption("seed", "rand-seed", true, "random seed");
        options.addOption("nt","num-threads",true,"threads used in permutation, default 1");
        
        //Parsing the command line arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if(cmd.hasOption("help")){
            printHelp(options);
            System.exit(0);
        }else if(cmd.hasOption("version")){
            System.out.println("quantLD " + globalSetting.packageVersion + " " + globalSetting.lastReviseDate);
            System.exit(0);
        }else{
            if(cmd.hasOption("f")){
                String tmp = cmd.getOptionValue("f");
                params.put("f", tmp);
            }
            if(cmd.hasOption("s")){
                String tmp = cmd.getOptionValue("s");
                params.put("s", tmp);
            }
            if(cmd.hasOption("m")){
                String tmp = cmd.getOptionValue("m");
                params.put("m", tmp);
            }
            if(cmd.hasOption("d")){
                String tmp = cmd.getOptionValue("d");
                params.put("d", tmp);
            }
            if(cmd.hasOption("iter")){
                String tmp = cmd.getOptionValue("iter");
                params.put("iter", tmp);
            }
            if(cmd.hasOption("w")){
                String tmp = cmd.getOptionValue("w");
                params.put("w", tmp);
            }
            if(cmd.hasOption("o")){
                String tmp = cmd.getOptionValue("o");
                params.put("o", tmp);
            }
            if(cmd.hasOption("tol")){
                String tmp = cmd.getOptionValue("tol");
                params.put("tol", tmp);
            }
            if(cmd.hasOption("nr")){
                String tmp = cmd.getOptionValue("nr");
                params.put("nr", tmp);
            }
            if(cmd.hasOption("p")){
                String tmp = cmd.getOptionValue("p");
                params.put("p", tmp);
            }
            if(cmd.hasOption("seed")){
                String tmp = cmd.getOptionValue("seed");
                params.put("seed", tmp);
            }
            if(cmd.hasOption("nt")){
                String tmp=cmd.getOptionValue("nt");
                params.put("nt", tmp);
            }
        }
     
        // print help if no option was provided
        if(params.isEmpty()){
            printHelp(options);
            System.exit(0);
        }
        
        // check required options
        if(!params.containsKey("f")){
            System.out.println("Option -f is required!");
            System.exit(0);
        }else if(!params.containsKey("s")){
            System.out.println("Option -s is required!");
            System.exit(0);
        }
        
        return params;
    }
}
