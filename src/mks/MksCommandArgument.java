/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mks;

/**
 *
 * @author uidj5418
 */
public class MksCommandArgument {
    
    public static final int ARG_TYPE_NORMAL = 0;
    public static final int ARG_TYPE_SECRET = 1;
    
    private int             type = 0;
    private String          stringArgument = null;
    
    public MksCommandArgument(String stringArgument) {
        this.stringArgument = stringArgument;
        this.type = ARG_TYPE_NORMAL;
    }
    
    public MksCommandArgument(String stringArgument, int argType) {
        this.stringArgument = stringArgument;
        switch(argType){
            case 0:
                this.type = ARG_TYPE_NORMAL; break;
            case 1:
                this.type = ARG_TYPE_SECRET; break;
            default:
                this.type = ARG_TYPE_NORMAL; break;
        }
    }

    @Override
    public String toString() {
        if (this.type == ARG_TYPE_SECRET) {
            return "****";
        }
        return this.stringArgument;
    }
}
