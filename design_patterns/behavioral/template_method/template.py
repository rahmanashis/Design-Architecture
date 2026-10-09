from abc import ABC,abstractmethod

class DataParser:
    def _open(self):
        print("Opening a file")

    def _close(self):
        print("Closing a file\n")

    def _parser(self):
        self._open()
        self._data_parser()
        self._close()

    @abstractmethod
    def _data_parser(self):
        pass


class CSVParser(DataParser):
    def _data_parser(self):
        print("Parsing CSV file")

class JSONParser(DataParser):
    def _data_parser(self):
        print("Parsing JSON file")

cp = CSVParser()
cp._parser()

jp = JSONParser()
jp._parser()


# Opening a file
# Parsing CSV file
# Closing a file

# Opening a file
# Parsing JSON file
# Closing a file
