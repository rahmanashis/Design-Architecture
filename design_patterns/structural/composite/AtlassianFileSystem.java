package design_patterns.structural.composite;

abstract class FileSystemNode{
    String name;
    long size;
    Directory parent;
    FileSystemNode(String name, Directory parent){
        this.name = name;
        this.parent = parent;
    }
    abstract int getSize();
    abstract boolean isFile();
}

class File extends FileSystemNode{
    int size;
    File(String name, int size, Directory parent){
        super(name,parent);
        this.size = size;
    }
    int getSize(){
        //System.out.println("        File: Size --> "+ name + " --> "+size);
        return size;
    }
    boolean isFile(){
        return true;
    }
}

class Directory extends FileSystemNode{
    HashMap<String,FileSystemNode> children = new HashMap<>();
    Directory(String name, Directory parent){
        super(name,parent);
    }

    void addNode(FileSystemNode node){
        children.put(node.name, node);
        updateSize(node.size);

    }

    int getSize(){
        int total = 0;
        //System.out.println("Current Directory -->" + this.name);
        for(String key : children.keySet()){
            total += children.get(key).getSize();
        }
        //System.out.println("       Folder: Size --> "+ name + " --> "+total);
        return total;
    }

    void updateSize(long changed){
        //change the size of current directory + all the parent directory
        Directory curr = this;
        while(curr != null){
            curr.size += changed;
            curr = curr.parent;
        }
    }

    boolean isFile(){
        return false;
    }
}

class FileSystem{
    Directory root;
    Directory current;
    FileSystem(){
        root = new Directory("/", null);
        current = root;
    }
    void createFile(String name, int size){
        current.addNode(new File(name, size, current));
    }
    void createFolder(String name){
        current.addNode(new Directory(name, current));
    }
    void displaySize(){
        //it will show size from current directory till root
        Directory curr = current;
        String output = "";
        while(curr != null){
            output = "Folder--> " + curr.name + " Size: " +curr.getSize() +" "+output +"\n";
            curr = curr.parent;
        }
        System.out.println(output);
    }
    void cd(String name){
        if(current.children.containsKey(name)){
            current = (Directory)current.children.get(name);
        }
    }

    void cd_back(){
        if(current.parent != null){
            current = current.parent;
        }
    }
    void showDetails(){
        current.getSize();
        displaySize();
    }

}


public class AtlassianFileSystem {
    public static void main(String[] args) {

        FileSystem fs = new FileSystem();
        fs.createFile("pradeep.txt",12);
        fs.createFolder("LLD");
        fs.cd("LLD");
        fs.createFile("LLD_file1", 10);
        fs.createFolder("HLD");
        fs.cd("HLD");
        fs.createFile("HLD File", 20);
        fs.showDetails();
        fs.createFile("HLD New files",100);
        fs.showDetails();

    }
}