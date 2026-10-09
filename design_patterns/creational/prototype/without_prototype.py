class ChessPiece:
    def __init__(self,name:str,position:str,color:str):
        self.name = name
        self.color = color
        self.position = position

    def display(self):
        return f"{self.color} {self.name} at {self.position}"

class ChessBoard:
    def __init__(self):
        self.pieces = []

    def add_piece(self,  piece:ChessPiece):
        self.pieces.append(piece)

    def display_board(self):
        for p in self.pieces:
            print(p.display())

p1 = ChessPiece("King", "e4", "Black")
p2 = ChessPiece("Knight", "B4", "Black")
p3 = ChessPiece("King","A4","White")

cb = ChessBoard()
cb.add_piece(p1)
cb.add_piece(p2)
cb.add_piece(p3)

cb.display_board()

cb2 = ChessBoard()
cb2.add_piece(p1)
cb2.add_piece(p2)
cb2.add_piece(p3)
print("------------------")
cb2.display_board()