'''
The board class handles the internal representation of the chess board including all logic. It will need to calculate all possible moves, to a certain depth, that can be played on a given position 
Given a position from main.py, the following will need to happen:

    Calculate all possible moves for both teams on this given position. Simulate moves on the same board and reverse the moves made

    For all pseudo-legal moves:
        update pinned pieces => check for king safety => store move

    The game will end if one of these conditions are met:
        - A team has no possible moves left
        - more than 50 moves have been made without a capture
        - insufficient material > when a checkmate is impossible with the pieces remaining

    
    TODO: Make the visuals and sounds more interesting. Add visuals for spaces in check, move history, move hints and drag highlights. 
    Also add sounds for piece moves, captures, check and checkmate

    
'''

from helper import Name
from helper import Piece
from helper import MoveType
from helper import GameState
from helper import Move

class Board:

    def __init__(self,initial_position: str):
        '''
        Initializes the board according to a position. 

        Parameters:
        initial_position (str): initial board position
        '''

        self.board = [[None for col in range(8)] for row in range(8)]

        # Pseudo legal translations - note that moves like enpassant and pawn jumps are calculated later
        self.PSEUDO_LEGAL_MOVEMENT = {
            Name.PAWNW: [(0,-1),(-1,-1),(1,-1)],
            Name.PAWNB: [(0,1),(-1,1),(1,1)],
            Name.KNIGHT: [(1,-2),(2,-1), (2,1),(1,2), (-1,2),(-2,1), (-2,-1), (-1,-2)],
            Name.ROOK: [(1,0),(-1,0),(0,1),(0,-1)],
            Name.BISHOP: [(1,1), (1,-1),(-1,1),(-1,-1)],
            Name.KING: [(1,1), (1,-1),(-1,1),(-1,-1),(1,0),(-1,0),(0,1),(0,-1)],
            Name.QUEEN: [(1,0),(-1,0),(0,1),(0,-1),(1,1),(1,-1),(-1,1),(-1,-1)]
        }

        self.MOVE_NUMBER = 0
        self.GAME_STATE = GameState.ACTIVE
        self.WHITE_TURN = True

        self.LEGAL_MOVES = {} # Key: Piece, Value: List of Tuples (col, row, MoveType)

        
        print(f'Setting board to {initial_position} position')
        self.setBoard(initial_position)

    def setBoard(self, FENSTRING: str) -> None:
        '''
        Sets the board according to the initialPosition parameter. Use "default" for standard chess game or
        a valid FEN string.

        Parameters: 
        FENSTRING (str): a valid FEN string
        '''

        self.GAME_STATE = GameState.ACTIVE
        if FENSTRING == "default":
            FENSTRING = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

        if FENSTRING == "random":
            FENSTRING = "rnbqkbnr/p1p1pppp/1p6/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3"
        
        if FENSTRING == "castle_king_side":
            FENSTRING = "r3kb1r/ppp1pppp/2nq1n2/5b2/2BP4/2N1PN2/PP3PPP/R1BQK2R w KQkq - 3 7"

        if FENSTRING == "knight_only":
            FENSTRING = "8/4N3/8/8/8/8/8/8 w - - 0 59"

        if FENSTRING == "pawn_only":
            FENSTRING = "8/8/8/8/8/8/4P3/8 w - - 0 59"

        if FENSTRING == "test_castle":
            FENSTRING = "4k2r/6r1/8/8/8/8/3R4/R3K3 w Qk - 0 1"

        print(FENSTRING)
        board_information = FENSTRING.split(" ")
        position = board_information[0]
        turn = board_information[1]
        castle_rights = board_information[2]
        enpassant_squares = board_information[3]
        halfmoves = board_information[4]

        for row in range(8):
            for col in range(8):
                self.board[row][col] = None

        self.WHITE_TURN = True if turn == "w" else False
        row, col = 0, 0

        for c in position:
            name:Name
            ind:int
            color:bool
            match c:
                case 'r':
                    name = Name.ROOK
                    ind = 10
                    color = False
                case 'n':
                    name = Name.KNIGHT
                    ind = 9
                    color = False
                case "b":
                    name = Name.BISHOP
                    ind = 8
                    color = False
                case "q":
                    name = Name.QUEEN
                    ind = 7
                    color = False
                case "k":
                    name = Name.KING
                    ind = 6
                    color = False
                case "p":
                    name = Name.PAWNB
                    ind = 11
                    color = False
                case "R":
                    name = Name.ROOK
                    ind = 4
                    color = True
                case "N":
                    name = Name.KNIGHT
                    ind = 3
                    color = True
                case "B":
                    name = Name.BISHOP
                    ind = 2
                    color = True
                case "Q":
                    name = Name.QUEEN
                    ind = 1
                    color = True
                case "K":
                    name = Name.KING
                    ind = 0
                    color = True
                case "P":
                    name = Name.PAWNW
                    ind = 5
                    color = True
                case '/':
                    name = None
                    col = -1
                    row+=1
                case _:
                    name = None
                    col += int(c)-1

            if name:
                self.board[row][col] = Piece(name,color,col,row,ind)

               
            col+=1

            if(col >=8):
                col = 0
        
        white_king = self.get_king(True)
        black_king = self.get_king(False)
        white_rook_queen_side = self.get_piece((0,7)) if self.get_piece((0,7)) and self.get_piece((0,7)).name == Name.ROOK else None
        white_rook_king_side = self.get_piece((7,7)) if self.get_piece((7,7)) and self.get_piece((7,7)).name == Name.ROOK else None
        black_rook_queen_side = self.get_piece((0,0)) if self.get_piece((0,0)) and self.get_piece((0,0)).name == Name.ROOK else None
        black_rook_king_side = self.get_piece((7,0)) if self.get_piece((7,0)) and self.get_piece((7,0)).name == Name.ROOK else None

        if white_rook_queen_side:
            white_rook_queen_side.first_move = False
        if white_rook_king_side:
            white_rook_king_side.first_move = False
        if black_rook_king_side:
            black_rook_king_side.first_move = False
        if black_rook_queen_side:
            black_rook_queen_side.first_move = False

        if not(castle_rights == "-"):
            for c in castle_rights:
                if c == "-":
                    continue
                if c == "K" and white_rook_king_side:
                    white_rook_king_side.first_move = True
                if c == "Q" and white_rook_queen_side:
                    white_rook_queen_side.first_move = True
                if c == "k" and black_rook_king_side:
                    black_rook_king_side.first_move = True
                if c == "q" and black_rook_queen_side:
                    black_rook_queen_side.first_move = True

        if not(enpassant_squares == "-"):
            square_conversion = {
                'a': 0,
                'b': 1,
                'c': 2,
                'd': 3,
                'e': 4,
                'f': 5,
                'g': 6,
                'h': 7,
            }
            row = 8-int(enpassant_squares[1])
            col = int(square_conversion[enpassant_squares[0]])
            dy = -1 if row > 6 else 1
            enpassant_piece = self.get_piece((col,row+dy))
            if enpassant_piece:
                enpassant_piece.enPassant = True

    def get_position_fenstring(self) -> str:
        result = ""
        enpassant_piece = list()
        for row in range(8):
            empty_space_count = 0
            for col in range(8):
                piece = self.get_piece((col,row))
                if piece and piece.enPassant:
                    enpassant_piece.append((col,row))
                if piece:
                    if empty_space_count > 0:
                        result += str(empty_space_count)
                        empty_space_count = 0

                    result += piece.get_letter()
                
                else:
                    empty_space_count+=1
                    if col == 7 and empty_space_count > 0:
                        result += str(empty_space_count)
            if row != 7:
                result += "/"
        
        turn = " w " if self.WHITE_TURN else " b "
        result += turn
        
        count = 0
        if self.get_piece((7,7)) and self.get_piece((7,7)).name == Name.ROOK and self.get_piece((7,7)).first_move and self.get_king(True).first_move:
            result += "K"
        else:
            count+=1
        if self.get_piece((0,7)) and self.get_piece((0,7)).name == Name.ROOK and self.get_piece((0,7)).first_move and self.get_king(True).first_move:
            result += "Q"
        else:
            count+=1
        if self.get_piece((7,0)) and self.get_piece((7,0)).name == Name.ROOK and self.get_piece((7,0)).first_move and self.get_king(False).first_move:
            result += "k"
        else:
            count+=1
        if self.get_piece((0,0)) and self.get_piece((0,0)).name == Name.ROOK and self.get_piece((0,0)).first_move and self.get_king(False).first_move:
            result += "q"
        else:
            count+=1

        if count == 4:
            result += "-"
        
        square_conversion = {
                0: 'a',
                1: 'b',
                2: 'c',
                3: 'd',
                4: 'e',
                5: 'f',
                6: 'g',
                7: 'h'
        }

        if len(enpassant_piece) > 0:
            result += " "
            col, row = enpassant_piece[0]
            dy = 1 if row > 6 else -1
            result += str(square_conversion[row])
            result += str(8-col-dy)
        else:
            result += " -"

        result += " random"
            
        print(f"{result}")
        return result 
    
    def print_board(self) -> None:
        for row in range(8):
            for col in range(8):
                piece = self.board[row][col]
                if piece:
                    print(f"{self.board[row][col]} ", end="")
                else:
                    print(". ",end="")
            print("\n")

    def user_valid_move(self, piece: Piece, position:tuple()) -> tuple:
        '''
        Returns True if the user move was a valid move, False otherwise. All valid moves are already calculated.
        '''

        valid = False
        move_type = None
        # Calculate all legal moves, this tuple will contain the move type which is not in position parameter
        legal_moves_for_piece = self.legal_moves(piece, check_for_checkmate=True)

        # Iterate through the legal moves and look for this position
        for move in legal_moves_for_piece:
            col, row, type_of_move = move
            if position == (col,row):
                # Pass the legal move containing the move type
                valid = True # TODO: TURNS piece.is_white == self.WHITE_TURN
                move_type = type_of_move
                
                if self.are_enemies(piece,self.get_piece((col,row))) and move_type == MoveType.DEFAULT:
                    move_type = MoveType.CAPTURE
                if valid:
                    self.move_piece(piece,move, simulation=False)
        
        return (valid,move_type)
    
    def move_piece(self, piece: Piece, position: tuple(), simulation: bool) -> set():
        '''
        Moves this piece to this position <- a tuple containing location and move type. 
        Assumes that this move is legal. Updates the board, move history...
        After the board position has changed due to this move, LEGAL_MOVES is updated

        Returns:
            A set of tuples describing which pieces have moved -> (piece, target position, previous position)

        Note: This function may end up too large and should be broken into smaller functions
        '''
        
        result = list()

        previous_position = piece.getPos()
        previous_col, previous_row = previous_position
        target_col, target_row, move_type = position
        target = (target_col, target_row)

        if move_type in(MoveType.DEFAULT, MoveType.CASTLE_KING_SIDE, MoveType.CASTLE_QUEEN_SIDE):

            captured_piece = self.get_piece((target_col,target_row))
            self.board[target_row][target_col] = piece
            self.board[previous_row][previous_col] = None

            piece.col = target_col
            piece.row = target_row

            if not simulation:
                piece.x = target_col * 100
                piece.y = target_row * 100

            result.append(Move(piece,captured_piece,previous_position, target, move_type))

        if move_type == MoveType.CASTLE_KING_SIDE:  # When storing if using move number as key, then don't update the castling as a separate move maybe
            rook = self.get_piece((7,7)) if piece.is_white else self.get_piece((7,0))
            
            previous_position = rook.getPos()
            self.board[target_row][target_col-1] = rook
            self.board[rook.row][rook.col] = None

            rook = self.board[target_row][target_col-1]

            rook.col = target_col-1
            rook.row = target_row

            if not simulation:
                rook.x = rook.col * 100
                rook.y = rook.row * 100

            result.append(Move(rook,None,previous_position,(target_col-1,target_row),MoveType.DEFAULT))

        if move_type == MoveType.CASTLE_QUEEN_SIDE:
            rook = self.get_piece((0,7)) if piece.is_white else self.get_piece((0,0))

            self.board[target_row][target_col+1] = rook
            self.board[rook.row][rook.col] = None

            rook = self.board[target_row][target_col+1]

            rook.col = target_col+1
            rook.row = target_row

            if not simulation:
                rook.x = rook.col * 100
                rook.y = rook.row * 100

            result.append(Move(rook,None,previous_position,(target_col+1,target_row),MoveType.DEFAULT))
            

        if move_type == MoveType.PROMOTION: # Eventually will need to ask what type of promotion

            captured_piece = self.get_piece((target_col,target_row))

            self.board[target_row][target_col] = piece
            self.board[previous_row][previous_col] = None

            piece.col = target_col
            piece.row = target_row

            if not simulation:
                piece.x = target_col * 100
                piece.y = target_row * 100

            piece.name = Name.QUEEN
            piece.img_index = 1 if piece.is_white else 7
            piece.can_slide = True

            result.append(Move(piece,captured_piece,previous_position,(target_col+1,target_row),MoveType.PROMOTION))

        if move_type == MoveType.ENPASSANT:
            captured_piece = self.board[previous_row][target_col]
            self.board[target_row][target_col] = piece
            self.board[previous_row][previous_col] = None
            self.board[previous_row][target_col] = None

            result.append(Move(piece,captured_piece,previous_position,target,move_type))
            


        if not simulation:
            for p in self.get_pieces(None): # Enpassant is only allowed for one turn
                p.enPassant = False
            
            piece.enPassant = (piece.name == Name.PAWNW or piece.name == Name.PAWNB) and piece.first_move and abs(target_row-previous_row) == 2

            piece.first_move = False

            self.MOVE_NUMBER += 1
            self.WHITE_TURN = not self.WHITE_TURN
            self.update_game_status()
                
        return result

    def undo_move(self, move: Move) -> None:
        pass    

    def update_game_status(self) -> GameState:
        
        if not self.all_legal_moves(True):
            self.GAME_STATE = GameState.BLACKWON

        if not self.all_legal_moves(False):
            self.GAME_STATE = GameState.WHITEWON

    def all_legal_moves(self, color: bool) -> dict():
        '''
        Iterates through each piece on the board and calculates possible legal moves
        
        Returns:
            A dict containing each piece on this team and a list of its possible moves
        '''

        pieces = self.get_pieces(is_white=color)
        legal_moves = {}
        for piece in pieces:
            piece_moves = self.legal_moves(piece, check_for_checkmate=True)
            if piece_moves:
                legal_moves[piece] = piece_moves

        return legal_moves

    def legal_moves(self, piece: Piece, check_for_checkmate: bool) -> set():
        self.update_all_pinned_pieces()

        if piece.name == Name.PAWNW or piece.name == Name.PAWNB:
            return self.legal_moves_pawn(piece, check_for_checkmate)
        if piece.name == Name.KING:
            return self.legal_moves_king(piece,check_for_checkmate)
        
        legal_moves = set()
        displacements = self.PSEUDO_LEGAL_MOVEMENT[piece.name]

        for displacement in displacements:

            if len(piece.pinned_directions) > 0 and displacement not in piece.pinned_directions:
                continue

            dx, dy = displacement
            curr_col, curr_row = piece.col + dx, piece.row + dy

            while self.position_valid((curr_col,curr_row)):
                target_piece = self.get_piece((curr_col,curr_row))

                # Empty spaces or enemies are added as long as the king is safe after this move
                if (not target_piece or self.are_enemies(target_piece, piece)) and self.king_safe_after_move(piece, (curr_col, curr_row, MoveType.DEFAULT), check_for_checkmate):
                    legal_moves.add((curr_col, curr_row, MoveType.DEFAULT))

                # If a piece is in the way, or this piece cannot slide, break out and start exploring next displacement
                if target_piece or not piece.can_slide:
                    break

                curr_col += dx
                curr_row += dy

        return legal_moves

    def legal_moves_king(self, piece: Piece, check_for_checkmate: bool) -> set():
        king_moves = set()
        displacements = self.PSEUDO_LEGAL_MOVEMENT[Name.KING]

        for displacement in displacements:
            if len(piece.pinned_directions) > 0 and displacement not in piece.pinned_directions:
                continue

            dx, dy = displacement
            curr_col, curr_row = piece.col + dx, piece.row + dy

            # TODO: Still need a way to check if in_between_target_piece is not in check

            if self.position_valid((curr_col,curr_row)):
                
                target_piece = self.get_piece((curr_col,curr_row))
                target_pos = (curr_col,curr_row)

                if (not target_piece or self.are_enemies(target_piece, piece)) and self.king_safe_after_move(piece, (curr_col, curr_row, MoveType.DEFAULT), check_for_checkmate):
                    king_moves.add((curr_col, curr_row, MoveType.DEFAULT))
                    
                    if dx == 1 and dy == 0 and piece.first_move and self.position_valid((curr_col+dx,curr_row)) and not self.get_piece((curr_col+dx,curr_row)) and self.can_rook_castle(piece,dx) and self.king_safe_after_move(piece, (curr_col+dx, curr_row, MoveType.CASTLE_KING_SIDE), check_for_checkmate):
                        king_moves.add((curr_col+dx, curr_row, MoveType.CASTLE_KING_SIDE))

                    if dx == -1 and dy == 0  and piece.first_move and self.position_valid((curr_col+dx,curr_row)) and not self.get_piece((curr_col+dx,curr_row)) and self.can_rook_castle(piece,dx) and self.king_safe_after_move(piece, (curr_col+dx, curr_row, MoveType.CASTLE_QUEEN_SIDE), check_for_checkmate):
                        king_moves.add((curr_col+dx, curr_row, MoveType.CASTLE_QUEEN_SIDE))

        return king_moves
    
    def legal_moves_pawn(self, piece: Piece, check_for_checkmate: bool) -> set():
        pawn_moves = set()
        displacements = self.PSEUDO_LEGAL_MOVEMENT[piece.name]

        for displacement in displacements:
            if len(piece.pinned_directions) > 0 and displacement not in piece.pinned_directions:
                continue

            dx, dy = displacement
            curr_col, curr_row = piece.col + dx, piece.row + dy

            if not self.position_valid((curr_col,curr_row)):
                continue
            
            move_type = MoveType.PROMOTION if (piece.is_white and curr_row == 0) or (not piece.is_white and curr_row == 7) else MoveType.DEFAULT
            target_piece = self.get_piece((curr_col,curr_row))
            target_pos = (curr_col, curr_row)
            enpassant_piece = self.get_piece((curr_col,piece.row))

            # Forward Move
            if dx == 0 and target_piece == None and self.king_safe_after_move(piece, (curr_col,curr_row,move_type), check_for_checkmate) and check_for_checkmate: 
                pawn_moves.add((curr_col,curr_row,move_type))

                # Double Forward Move
                if piece.first_move and self.get_piece((curr_col,curr_row+dy)) == None and self.king_safe_after_move(piece, (curr_col,curr_row+dy,move_type), check_for_checkmate) and check_for_checkmate:
                    pawn_moves.add((curr_col,curr_row+dy,move_type))

            # Diagonal Capture
            if dx != 0 and self.are_enemies(piece,target_piece) and self.king_safe_after_move(piece, (curr_col,curr_row,move_type), check_for_checkmate):
                pawn_moves.add((curr_col,curr_row,move_type))
            
            # Enpassant Capture
            if dx != 0 and target_piece == None and self.are_enemies(enpassant_piece, piece) and enpassant_piece.enPassant and self.king_safe_after_move(piece, (curr_col,curr_row,MoveType.ENPASSANT), check_for_checkmate):
                pawn_moves.add((curr_col,curr_row,MoveType.ENPASSANT))

        return pawn_moves

    def king_safe_after_move(self,piece: Piece, pos: tuple(), check_for_checkmate: bool) -> bool:
        '''
        Only calculated when checking for checkmate. Simulates the move, then checks if this pieces king is on a square being
        attacked by the enemy team
        '''
        if not check_for_checkmate:
            return True
        
        king_safe_after_move = True
        spaces_in_check = set()
        previous_col, previous_row = piece.getPos()
        target_col, target_row, move_type = pos
        target = (target_col, target_row)

        captured_piece = None
        previous_name = piece.name

        
        if move_type in (MoveType.DEFAULT, MoveType.CASTLE_KING_SIDE, MoveType.CASTLE_QUEEN_SIDE):

            captured_piece = self.get_piece((target_col,target_row))
            self.board[target_row][target_col] = piece
            self.board[previous_row][previous_col] = None

            piece.col = target_col
            piece.row = target_row


        if move_type == MoveType.CASTLE_KING_SIDE: 
            rook = self.get_piece((7,7)) if piece.is_white else self.get_piece((7,0))
            
            self.board[target_row][target_col-1] = rook
            self.board[rook.row][rook.col] = None

            rook = self.board[target_row][target_col-1]

            rook.col = target_col-1
            rook.row = target_row

        if move_type == MoveType.CASTLE_QUEEN_SIDE:
            rook = self.get_piece((0,7)) if piece.is_white else self.get_piece((0,0))

            self.board[target_row][target_col+1] = rook
            self.board[rook.row][rook.col] = None

            rook = self.board[target_row][target_col+1]

            rook.col = target_col+1
            rook.row = target_row

        if move_type == MoveType.PROMOTION: # Eventually will need to ask what type of promotion

            captured_piece = self.get_piece((target_col,target_row))

            self.board[target_row][target_col] = piece
            self.board[previous_row][previous_col] = None

            piece.col = target_col
            piece.row = target_row

            piece.name = Name.QUEEN
            piece.can_slide = True

        if move_type == MoveType.ENPASSANT:
            captured_piece = self.board[previous_row][target_col]
            self.board[target_row][target_col] = piece
            self.board[previous_row][previous_col] = None
            self.board[previous_row][target_col] = None

            piece.col = target_col
            piece.row = target_row

        # Check if king is in the attacking squares of enemy pieces
        king = self.get_king(piece.is_white).getPos()

        for enemy_piece in self.get_pieces(not(piece.is_white)):
            legal_moves = self.legal_moves(enemy_piece, check_for_checkmate=False)

            for move in legal_moves:
                spaces_in_check.add(move[:2])

            if king in spaces_in_check:
                king_safe_after_move = False
                break

        if move_type in(MoveType.DEFAULT, MoveType.CASTLE_KING_SIDE, MoveType.CASTLE_QUEEN_SIDE):
            piece.col = previous_col
            piece.row = previous_row

            self.board[target_row][target_col] = captured_piece
            self.board[previous_row][previous_col] = piece

        if move_type == MoveType.CASTLE_KING_SIDE:  # When storing if using move number as key, then don't update the castling as a separate move maybe
            
            col, row = (7,7) if piece.is_white else (7,0)
            rook = self.board[target_row][target_col-1]

            self.board[target_row][target_col-1] = None
            self.board[row][col] = rook

            rook.col = col
            rook.row = row


        if move_type == MoveType.CASTLE_QUEEN_SIDE:
            col, row = (0,7) if piece.is_white else (0,0)
            rook = self.board[target_row][target_col+1]

            self.board[target_row][target_col+1] = None
            self.board[row][col] = rook

            rook.col = col
            rook.row = row

        if move_type == MoveType.PROMOTION:
            self.board[target_row][target_col] = captured_piece
            self.board[previous_row][previous_col] = piece

            piece.col = previous_col
            piece.row = previous_row

            piece.name = Name.PAWNW if target_row == 0 else Name.PAWNB
            piece.can_slide = False

        if move_type == MoveType.ENPASSANT:
            self.board[target_row][target_col] = None
            self.board[previous_row][previous_col] = piece
            self.board[previous_row][target_col] = captured_piece

            piece.col = previous_col
            piece.row = previous_row
        
        return king_safe_after_move
    
    def update_all_pinned_pieces(self) -> None:
        '''
        Updates every pinned piece and which direction they are pinned in. 
        '''
        # Clear old data
        for piece in self.get_pieces(is_white=None):
            piece.pinned_directions.clear()

        kings = (self.get_king(True),self.get_king(False))
        displacements = self.PSEUDO_LEGAL_MOVEMENT[Name.QUEEN]

        # Starting from each king, expand outwards in 8 directions
        for king in kings:
            if not king:
                break
            for displacement in displacements:
                dx, dy = displacement
                curr_col, curr_row = king.col + dx, king.row + dy
                piece_maybe_pinned = None

                while self.position_valid((curr_col,curr_row)):
                    target_piece = self.get_piece((curr_col,curr_row))

                    if target_piece == None:
                        curr_col += dx
                        curr_row += dy
                        continue

                    if self.are_enemies(king, target_piece):
                        if piece_maybe_pinned and target_piece.can_slide and displacement in self.PSEUDO_LEGAL_MOVEMENT[target_piece.name]:
                            piece_maybe_pinned.pinned_directions.add((displacement))
                            piece_maybe_pinned.pinned_directions.add((dx * -1, dy * -1))
                        break
                    else:
                        if piece_maybe_pinned:
                            break
                        else:
                            piece_maybe_pinned = target_piece

                    curr_col += dx
                    curr_row += dy
                    

    '''
    HELPER METHODS
    '''

    def can_rook_castle(self, king: Piece, dx: int) -> bool:
        if king.is_white:
            if dx==1:
                rook = self.get_piece((7,7))
            else:
                rook = self.get_piece((0,7))
        else:
            if dx==1:
                rook = self.get_piece((7,0))
            else:
                rook = self.get_piece((0,0))

        return True if rook and rook.first_move else False

        
    def get_king(self, is_white: bool) -> Piece:
        for piece in self.get_pieces(is_white):
            if piece.name == Name.KING:
                return piece
        return None

    def are_enemies(self, A: Piece, B: Piece) -> bool:
        '''
        Return True if these pieces are on opposite teams, False otherwise
        '''
        if A == None or B == None:
            return False
        
        return not (A.is_white == B.is_white)
        
    def get_piece(self, pos: tuple()) -> Piece:
        '''
        Returns the Piece at this position
        '''
        col, row = pos
        return self.board[row][col]
    
    def position_valid(self,pos: tuple()) -> bool:
        '''
        Return True if the position is within the 8 x 8 board
        '''

        col,row = pos
        return col<=7 and col>=0 and row<=7 and row>=0
        
    def get_pieces(self, is_white: bool) -> set():
        '''
        Returns the pieces of this team. is_white can be True for white pieces
        False for black pieces and None for all pieces
        '''

        pieces = set()
        for row in range(8):
            for col in range(8):
                piece = self.board[row][col]
                if not piece:
                    continue

                if is_white == None:
                    pieces.add(piece)
                elif is_white and piece.is_white:
                    pieces.add(piece)
                elif not is_white and not piece.is_white:
                    pieces.add(piece)
        return pieces
        
