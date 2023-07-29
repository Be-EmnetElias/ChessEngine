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
    
    def __str__(self) -> str:
        match self:
            case MoveType.DEFAULT:
                return "DEFAULT"
            case MoveType.CAPTURE:
                return "CAPTURE"
            case MoveType.CASTLE_KING_SIDE:
                return "CASTLE_KING_SIDE"
            case MoveType.CASTLE_QUEEN_SIDE:
                return "CASTLE_QUEEN_SIDE"
            case MoveType.PROMOTION:
                return "PROMOTION"
            case MoveType.ENPASSANT:
                return "ENPASSANT"
            case _:
                return "NO MOVE"

    DEFAULT:int = 0
    CAPTURE:int = 1
    CASTLE_KING_SIDE:int = 2
    CASTLE_QUEEN_SIDE:int = 3
    PROMOTION:int = 4
    ENPASSANT:int = 5

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
    
    def get_letter(self) -> str:
        if self.is_white:
            return str(self.name)[5:6]
        else:
            return str(self.name)[5:6].lower()
    
    def __str__(self) -> str:
        show_data = False

        if show_data:
            return f'Name: {str(self.name)[5:]} Position: {self.getPos()} CanSlide: {self.can_slide} EnPassant: {self.enPassant} FirstMove: {self.first_move} \n'

        return str(self.name)[5:6]
        
class Move:
    '''
    A move contains a piece, previous position, target position, the type of move and the enemy piece that was captured
    '''
    def __init__(self, piece: Piece, captured_piece: Piece, previous_position: tuple(), target_position: tuple(), move_type: MoveType) -> None:
        
        self.piece = piece
        self.captured_piece = captured_piece
        self.previous_position = previous_position
        self.target_position = target_position
        self.move_type = move_type

    def __str__(self) -> str:
        return f"{self.move_type}: {self.piece.name} to {self.target_position} { f'capturing {self.captured_piece.name}' if self.captured_piece else ''}"