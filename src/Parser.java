public class Parser {
    String commandName = "";
    String[] args = null;

    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input){
        String[] allArgs = input.split(" ");
        commandName = allArgs[0];
        commandName = commandName.toLowerCase();
        args = new String[allArgs.length-1];
        for(int i=1;i<allArgs.length;i++) args[i-1] = allArgs[i];
        if (commandName.equals("echo")) {
            for(int i=0;i< args.length;i++){
                args[i] = args[i].replaceAll("\"", "");
                args[i] = args[i].replaceAll("\'", "");
            }
        } else if (commandName.equals("pwd")) {
            if(args.length > 0) return false;
        } else if (commandName.equals("ls")) {
            if(args.length > 1) return false;
            else if(args.length == 1 && !args[0].equals("-r")) return false;
        } else if(commandName.equals("rmdir")){
            if(args.length != 1) return false;
        } else if(commandName.equals("touch")){
            if(args.length != 1) return false;
        } else if(commandName.equals("cd")){
            if(args.length > 1) return false;
        } else if(commandName.equals("rm")){
            if(args.length != 1) return false;
        }else if(commandName.equals("cat")){
            if(args.length != 1 && args.length != 2) return false;
        } else if(commandName.equals("cp")){
            //cp or cpr
            if(args.length > 3) return false;
        }
        return true;
    }

    public String getCommandName(){
        return commandName;
    }

    public String[] getArgs(){
        return args;
    }
}