package com.shuaib.oopcw;

import com.shuaib.oopcw.backend_api.OopcwApplication;
import com.shuaib.oopcw.cli.Cli;

public class Main {
    public static void main(String[] args) {
        Cli.main(args);
        System.out.println("Starting backend server...");
        OopcwApplication.main(args);        
    }
}
