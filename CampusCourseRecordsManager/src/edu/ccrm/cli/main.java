package edu.ccrm.cli;

//main is the main entry point for the Campus Course & Records Manager application.
public class main {

    /*
     The main method that starts the execution of the program.
   
      @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        System.out.println("----");
        System.out.println(" Welcome to the Campus Course & Records Manager (CCRM) ");
        System.out.println("----");

        // Create an instance of the CLI manager and start the application
        CliManager cliManager = new CliManager();
        cliManager.start();
    }
}