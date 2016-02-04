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
    private final String packageVersion = "0.1";
    
    private void printHelp(Options opts) {  
        HelpFormatter hf = new HelpFormatter();  
        hf.printHelp(" ", opts);  
    }
    
    /**
     * command line processing by using Apache Commons CLI
     * @param args arguments array from main()
     * @return HashMap<String, String> of options
     * @throws ParseException 
     */
    public HashMap<String, String> getOpt(String[] args) throws ParseException{
        HashMap<String, String> params = new HashMap<>();
        // create Options object
        Options options = new Options();
        // create the boolean options
        Option help = new Option("help", "print this message" );
        Option version = new Option("version", "print the version information and exit");
        // add the boolean options
        options.addOption(help);
        options.addOption(version);
        // add other options
        options.addOption("f", "first-tped", true, "first input genotype file, required");
        options.addOption("s", "second-tped", true, "second input genotype file, required");
        options.addOption("t", "tolerance", true, "convergence tolerance, default 0.001");
        options.addOption("i", "max-iteration", true, "maximum number of EM steps, default 1000");
        options.addOption("m", "ld-measure", true, "LD measure, default r2");
        
        //Parsing the command line arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if(cmd.hasOption("help")){
            printHelp(options);
            System.exit(0);
        }else if(cmd.hasOption("version")){
            System.out.println("quantLD " + packageVersion);
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
            if(cmd.hasOption("t")){
                String tmp = cmd.getOptionValue("t");
                params.put("t", tmp);
            }
            if(cmd.hasOption("i")){
                String tmp = cmd.getOptionValue("i");
                params.put("i", tmp);
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