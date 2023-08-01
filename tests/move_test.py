'''
Tests legal moves by iterating through all possible games of a certain depth.
'''

from src.board import Board
from src.helper import style


board = Board("default")

def test_move_count_depth():
    
    def move_count_depth(depth: int, is_white: bool) -> int:
        if depth == 0:
            return 1

        moves = {}
        for piece in board.get_pieces(is_white):
            moves_piece = ({piece:board.legal_moves(piece, check_for_checkmate=True)})
            moves |= moves_piece

        num_of_moves = 0

        for piece in moves:
            for move in moves[piece]:
                current_move = board.move_piece(piece, move, simulation=True)
                # print(f"{style.HEADER}{piece.name}{style.ENDC} : {move}")
                # board.print_board(highlight=current_move)
                num_of_moves += move_count_depth(depth-1, not is_white)
                board.undo_move(move)

        return num_of_moves


    # depth_1 = move_count_depth(1,True)
    # assert depth_1 == 20

    # depth_2 = move_count_depth(2,True)
    # assert depth_2 == 401

    # depth_3 = move_count_depth(3,True)
    # assert depth_3 == 8902

    depth_4 = move_count_depth(4,True)
    assert depth_4 == 197281

    # depth_5 = move_count_depth(5,True)
    # assert depth_5 == 119060324

    # depth_6 = move_count_depth(6,True)
    # assert depth_6 == 3195901860

    # depth_7 = move_count_depth(7,True)
    # assert depth_7 == 84998978956

    
    # [20,400,8902,197281,119060324,3195901860,84998978956,2439530234167,69352859712417]











