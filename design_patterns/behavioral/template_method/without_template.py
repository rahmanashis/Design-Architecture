# This code will wrok for different type of files... open,parse and close. open and close may be same for all kings of file
class CSVParser:
    def open(self):
        print("Opening a file")
    def close(self):
        print("Closing a file\n")
    def parser(self):
        self.open()
        print("Parsing CSV file")
        self.close()



class JSONParser:
    def open(self):
        print("Opening a file")
    def close(self):
        print("Closing a file\n")
    def parser(self):
        self.open()
        print("Parsing JSON file")
        self.close()


# we are repeating same open and close for different files which is redundant

cp = CSVParser()
cp.parser()

jp = JSONParser()
jp.parser()