from enum import Enum, auto

class Name(Enum):
    PAWNW:int = 0
    PAWNB:int = 1
    KNIGHT:int= 2
    BISHOP:int= 3
    ROOK:int= 4
    QUEEN:int= 5
    KING:int= 6

class MoveType(Enum):

    def __str(self):
        return  str(self.value) + " D"
    # A default move is one that moves 1 piece from an inital location
    # to a target location, without affecting any other piece except for
    # the target
    DEFAULT:int = 0
    CASTLE_KING_SIDE:int = 1
    CASTLE_QUEEN_SIDE:int = 2
    PROMOTION:int = 3
    ENPASSANT:int = 4



class GameState(Enum):
    ACTIVE:int=0
    STALEMATE:int=1
    BLACKWON:int=2
    WHITEWON:int=3
    INSUFFICIENT_MATERIAL:int=4
    
    
class Piece:

    def __init__(self, name: Name, color: bool, col: int, row: int, imgIndex: int) -> None:
        self.name = name
        self.is_white = color
        self.col = col
        self.x = col * 100
        self.row = row
        self.y = row * 100
        self.img_index = imgIndex

        self.first_move = True
        self.enPassant = False
        self.can_slide = False if (name == Name.KING or name == Name.PAWNW or name == Name.PAWNB or name == Name.KNIGHT) else True
        self.pinned_directions = set()

    def getPos(self) -> tuple():
        return (self.col,self.row)
    
    def __str__(self) -> str:
        show_data = False

        if show_data:
            return f'Name: {str(self.name)[5:]} Position: {self.getPos()} CanSlide: {self.can_slide}'

        return str(self.name)[5:6]
        
