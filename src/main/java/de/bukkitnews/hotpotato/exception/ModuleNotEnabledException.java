package de.bukkitnews.hotpotato.exception;

import de.bukkitnews.hotpotato.module.CustomModule;

public class ModuleNotEnabledException extends Exception {

    public ModuleNotEnabledException(CustomModule customModule){
        super("Module "+customModule.getModuleName()+" is not enabled.");
    }
}
