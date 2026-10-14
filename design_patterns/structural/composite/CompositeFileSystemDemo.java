package design_patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

public class CompositeFileSystemDemo {

    // ==========================
    // 1. Component
    // ==========================
    interface FileSystemItem {
        void showDetails();
        int getSize();
    }

    // ==========================
    // 2. Leaf
    // ==========================
    static class File implements FileSystemItem {
        private String name;
        private int size;

        File(String name, int size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public void showDetails() {
            System.out.println("File: " + name + " (" + size + "KB)");
        }

        @Override
        public int getSize() {
            return size;
        }
    }

    // ==========================
    // 3. Composite
    // ==========================
    static class Folder implements FileSystemItem {
        private String name;
        private List<FileSystemItem> children = new ArrayList<>();

        Folder(String name) {
            this.name = name;
        }

        void add(FileSystemItem item) {
            children.add(item);
        }

        @Override
        public void showDetails() {
            System.out.println("Folder: " + name);
            for (FileSystemItem item : children) {
                item.showDetails();
            }
        }

        @Override
        public int getSize() {
            int totalSize = 0;
            for (FileSystemItem item : children) {
                totalSize += item.getSize();
            }
            return totalSize;
        }
    }

    // ==========================
    // 4. Client
    // ==========================
    public static void main(String[] args) {

        File file1 = new File("resume.pdf", 120);
        File file2 = new File("photo.png", 200);
        File file3 = new File("notes.txt", 50);

        Folder documents = new Folder("Documents");
        documents.add(file1);
        documents.add(file3);

        Folder root = new Folder("Root");
        root.add(documents);
        root.add(file2);

        // Treating file & folder uniformly
        root.showDetails();
        System.out.println("Total size: " + root.getSize() + "KB");
    }
}