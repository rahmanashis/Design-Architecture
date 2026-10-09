class TextMemento:
    def __init__(self,text):
        self.__saved_text = text
    def get_saved_text(self):
        return self.__saved_text

class TextEditor:
    def __init__(self):
        self.__text = ""
    def write(self, new_text):
        self.__text += new_text

    def get_text(self):
        return self.__text

    def save(self) -> TextMemento:
        return TextMemento(self.__text)

    def restore(self,tm:TextMemento):
        self.__text = tm.get_saved_text()

class History:
    def __init__(self):
        self.__history = []

    def save_state(self,tm:TextMemento):
        self.__history.append(tm)

    def undo(self) -> TextMemento:
        if len(self.__history)>0:
            self.__history.pop()
            if len(self.__history) == 0:
                return TextMemento("")
            return self.__history[-1]
        else:
            return TextMemento("")

text_editor = TextEditor()
history = History()

text_editor.write("Hello")
text_editor.write(" World")
history.save_state(text_editor.save())
text_editor.write(" Good")
text_editor.write(" Bye")
history.save_state(text_editor.save())
print(text_editor.get_text())
print("-------")
text_editor.restore(history.undo())
print(text_editor.get_text())
text_editor.restore(history.undo())
print(text_editor.get_text())