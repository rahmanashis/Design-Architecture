package design_patterns.structural.composite;

import java.util.*;

// ---------- Base Class ----------
abstract class FileSystemNode {

    protected String name;
    protected Directory parent;

    public FileSystemNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    // Default: Not supported
    public void write(String data) {
        throw new UnsupportedOperationException("Write not supported");
    }

    public String read() {
        throw new UnsupportedOperationException("Read not supported");
    }

    public Directory asDirectory() {
        throw new UnsupportedOperationException("Not a directory");
    }

    public abstract void ls();
}

// ---------- File ----------
class FileNode extends FileSystemNode {

    private StringBuilder content = new StringBuilder();

    public FileNode(String name) {
        super(name);
    }

    @Override
    public void write(String data) {
        content.append(data);
    }

    @Override
    public String read() {
        return content.toString();
    }

    @Override
    public void ls() {
        System.out.println("FILE : " + name);
    }
}

// ---------- Directory ----------
class Directory extends FileSystemNode {

    private Map<String, FileSystemNode> children = new HashMap<>();

    public Directory(String name) {
        super(name);
    }

    @Override
    public Directory asDirectory() {
        return this;
    }

    public void add(FileSystemNode node) {
        children.put(node.getName(), node);
        node.setParent(this);
    }

    public FileSystemNode get(String name) {
        return children.get(name);
    }

    public void remove(String name) {
        children.remove(name);
    }

    @Override
    public void ls() {
        System.out.println("DIR  : " + name);

        for (FileSystemNode node : children.values()) {
            node.ls();
        }
    }
}

// ---------- File System ----------
class FileSystem {

    private Directory root;
    private Directory current;

    public FileSystem() {
        root = new Directory("/");
        current = root;
    }

    // mkdir
    public void mkdir(String name) {

        if (current.get(name) != null) {
            System.out.println("Already exists");
            return;
        }

        current.add(new Directory(name));
    }

    // touch
    public void createFile(String name) {

        if (current.get(name) != null) {
            System.out.println("Already exists");
            return;
        }

        current.add(new FileNode(name));
    }

    // cd
    public void cd(String name) {

        if (name.equals("..")) {

            if (current.parent != null) {
                current = current.parent;
            }
            return;
        }

        FileSystemNode node = current.get(name);

        if (node == null) {
            System.out.println("Not found");
            return;
        }

        try {
            current = node.asDirectory();
        } catch (Exception e) {
            System.out.println("Not a directory");
        }
    }

    // ls
    public void ls() {
        current.ls();
    }

    // write
    public void write(String fileName, String data) {

        FileSystemNode node = current.get(fileName);

        if (node == null) {
            System.out.println("File not found");
            return;
        }

        try {
            node.write(data);
        } catch (Exception e) {
            System.out.println("Cannot write to directory");
        }
    }

    // read
    public void read(String fileName) {

        FileSystemNode node = current.get(fileName);

        if (node == null) {
            System.out.println("File not found");
            return;
        }

        try {
            System.out.println(node.read());
        } catch (Exception e) {
            System.out.println("Cannot read directory");
        }
    }

    // pwd
    public void pwd() {
        printPath(current);
        System.out.println();
    }

    private void printPath(Directory dir) {

        if (dir.parent != null) {
            printPath(dir.parent);
        }

        System.out.print(dir.getName());

        if (!dir.getName().equals("/")) {
            System.out.print("/");
        }
    }
}

// ---------- Main ----------
public class FIleSystemDemo {

    public static void main(String[] args) {

        FileSystem fs = new FileSystem();

        // Create directories
        fs.mkdir("docs");
        fs.mkdir("images");

        // Go to docs
        fs.cd("docs");

        // Create files
        fs.createFile("resume.txt");
        fs.createFile("notes.txt");

        // Write data
        fs.write("resume.txt", "Pradeep Kumar Resume\n");
        fs.write("notes.txt", "LLD Preparation Notes");

        // Read file
        fs.read("resume.txt");

        // Print path
        fs.pwd();

        // List current dir
        fs.ls();

        // Go back
        fs.cd("..");

        // List root
        fs.ls();
    }
}