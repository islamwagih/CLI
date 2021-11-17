import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Terminal {
    Parser parser = new Parser();
    String currWorkingPath = "";
    Scanner scan = new Scanner(System.in);
    public Terminal(){
        try {
            currWorkingPath = new File("").getCanonicalPath();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String pwd(){
        return currWorkingPath;
    }

    public void cp(String original, String copy) {
        if(!original.contains("\\")){
            original = this.pwd() + "\\" + original;
        }
        if(!copy.contains("\\")){
            copy = this.pwd()+"\\"+copy;
        }
        try {
            Files.copy(new File(original).toPath(), new File(copy).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("FILE NOT FOUND");
        }
    }

    public void cpr(String org, String dest) {
        if(!org.contains("\\")){
            org = this.pwd() + "\\" + org;
        }
        if(!dest.contains("\\")){
            dest = this.pwd()+"\\"+dest;
        }
        File original = new File(org);
        File destination = new File(dest);
        if (original.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }
            String file[] = original.list();
            for (String temp : file) {
                File supOriginal = new File(original, temp);
                File supDestination = new File(destination, temp);
                cpr(supOriginal.getPath(), supDestination.getPath());
            }
        } else {
            cp(original.getPath(), destination.getPath());
        }
    }

    public void cat(String file) throws IOException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.pwd()+"\\"+file));
            String temp;
            boolean hasContent = false;
            while ((temp = br.readLine()) != null) {
                System.out.println(temp);
                hasContent = true;
            }
            if(!hasContent){
                System.out.println("FILE IS EMPTY");
            }
            br.close();
        } catch (IOException e) {
            System.out.println("FILE NOT FOUND");
        }
    }

    public void cat(String file1, String file2) throws IOException {
        file1 = this.pwd() +"\\"+ file1;
        file2 = this.pwd() +"\\"+ file2;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            String temp;
            while ((temp = br.readLine()) != null) {
                System.out.println(temp);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("FIRST FILE NOT FOUND");
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file2));
            String temp;
            while ((temp = br.readLine()) != null) {
                System.out.println(temp);
            }

        } catch (Exception e) {
            System.out.println("SECOND FILE NOT FOUND");
        }
    }

    public void echo (String line) {
        System.out.print(line);
    }

    public void ls () {
        File dir = new File(pwd());
        File[] filesList = dir.listFiles();
        Arrays.sort(filesList);
        for(File f : filesList){
            System.out.println(f.getName());
        }
    }

    public void lsr () {
        File dir = new File(".");
        File[] filesList = dir.listFiles();
        Arrays.sort(filesList,  Collections.reverseOrder());
        for(File f : filesList){
            System.out.println(f.getName());
        }
    }

    public void touch (String x) {
        File f;
        try {
            if(x.contains("\\")) {
                f = new File(x);
            }else{
                f = new File(this.pwd()+"\\"+x);
            }
            f.createNewFile();
        }
        catch (IOException e) {
            System.out.println("CAN'T CREATE FILE");
        }
    }

    public void mkdir(String[] args){
        for(String arg:args){
            String path = arg;
            if(!arg.contains("\\")){
                path = this.pwd()+"\\"+arg;
            }
            File f = new File(path);
            f.mkdir();
        }
    }

    public boolean isEmptyDirectory(String path){
        File dir = new File(path);
        if(!dir.isDirectory()) return false;
        return dir.list().length == 0;
    }

    public void rmdir(String command){
        File f;
        if(command.equals("*")){
            f = new File(this.pwd());
            File[] sub = f.listFiles();
            for(File s:sub){
                if(s.isDirectory() && isEmptyDirectory(s.getPath())){
                    s.delete();
                }
            }
        }else{
            if(!command.contains("\\")){
                command = this.pwd()+"\\"+command;
            }
            f = new File(command);
            if(f.isDirectory() && isEmptyDirectory(f.getPath())){
                f.delete();
            }
        }
    }

    public void rm(String fileName){
        File f = new File(this.pwd()+"\\"+fileName);
        if(f.isFile()) f.delete();
    }

    public void cd(){
        try {
            currWorkingPath = new File("").getCanonicalPath();
        }catch (IOException e){
            System.out.println("CAN'T GO TO HOME DIRECTORY");
        }
    }

    public void cd(String path){
        if(path.equals("..")){
            int idx = -1;
            String newPath = null;
            for(int i=currWorkingPath.length()-2;i>-1;i--){
                if(currWorkingPath.charAt(i) == '\\'){
                    idx = i;
                    break;
                }
            }
            newPath = currWorkingPath.substring(0, idx+1);
            Path p = Paths.get(newPath);
            if(newPath.length() > 0 && Files.exists(p)){
                currWorkingPath = newPath;
            }
        }else{
            if(!path.contains("\\")){
                path = this.pwd()+"\\"+path;
            }
            File f = new File(path);
            if(f.isDirectory()){
                currWorkingPath = f.getPath();
            }
        }
    }

    public void run(){
        boolean running = true;
        while (running) {
            //take input line from the user
            System.out.print(">>");
            String userInput = scan.nextLine();
            if(userInput.toLowerCase().equals("exit")){
                running = false;
                continue;
            }
            //parse the input and check for any error
            boolean res = parser.parse(userInput);
            if(!res){
                System.out.println("INVALID ARGUMENT LIST");
                continue;
            }
            //if all is good execute the command
            String command = parser.getCommandName();
            command = command.toLowerCase();
            String[] args = parser.getArgs();
            if (command.equals("echo")) {
                for (String arg : parser.getArgs()) {
                    this.echo(arg);
                    System.out.print(' ');
                }
                System.out.println();
            } else if (command.equals("pwd")) {
                System.out.println(this.pwd());
            } else if (command.equals("ls")) {
                if(args.length == 0) {
                    this.ls();
                }else{
                    this.lsr();
                }
            } else if(command.equals("mkdir")){
                this.mkdir(args);
            } else if(command.equals("rmdir")){
                this.rmdir(args[0]);
            } else if(command.equals("touch")){
                this.touch(args[0]);
            } else if(command.equals("cd")){
                if(args.length == 0){
                    this.cd();
                }else{
                    this.cd(args[0]);
                }
            } else if(command.equals("rm")){
                this.rm(args[0]);
            }else if(command.equals("cat")){
                if(args.length == 1){
                    try {
                        this.cat(args[0]);
                    }catch (IOException e){
                        System.out.println("CAN't CAT THE FILE");
                    }
                }else {
                    try {
                        this.cat(args[0], args[1]);
                    }catch (IOException e){
                        System.out.println("CAN'T CAT THE TWO FILES");
                    }
                }
            }else if(command.equals("cp")){
                if(args.length == 2){
                    this.cp(args[0], args[1]);
                }else{
                    this.cpr(args[1], args[2]);
                }
            }else{
                System.out.println("COMMAND NOT FOUND");
            }
        }
    }

    public static void main(String[] args){
        Terminal myTerm = new Terminal();
        myTerm.run();
    }
}
