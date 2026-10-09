class TextEditor:
    def __init__(self):
        self.text = ''
        self.history = ['']

    def write(self,new_text:str):
        self.text += new_text

    def save(self):
        self.history.append(self.text)

    def undo(self):
        if len(self.history)>1:
            self.history.pop()
            self.text = self.history[-1]
        else:
            self.text = ''

    def get(self):
        return self.text

text_editor = TextEditor()
text_editor.write("Hello ")
text_editor.write("Wrold ")
text_editor.save()
print(text_editor.get())
text_editor.write("Rashed ")
text_editor.save()
print(text_editor.get())
text_editor.undo()
print(text_editor.get())