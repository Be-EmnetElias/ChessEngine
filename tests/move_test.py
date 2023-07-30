from src.board import Board

board = Board("default")

def test_move_count_depth():
    
    

    def move_count_depth(depth: int, is_white: bool) -> int:
        if depth == 0:
            return 1

        moves = board.all_legal_moves(is_white)
        num_of_moves = 0

        for piece in moves:
            for move in moves[piece]:
                board.move_piece(piece, move, simulation=True)
                num_of_moves += move_count_depth(depth-1, not is_white)
                board.undo_move(move)

        return num_of_moves


    depth_1 = move_count_depth(1,True)
    assert depth_1 == 20

    depth_2 = move_count_depth(2,True)
    assert depth_2 == 400

    depth_3 = move_count_depth(3,True)
    assert depth_3 == 8902

    depth_4 = move_count_depth(4,True)
    assert depth_4 == 197281

    depth_5 = move_count_depth(5,True)
    assert depth_5 == 119060324

    depth_6 = move_count_depth(6,True)
    assert depth_6 == 3195901860

    depth_7 = move_count_depth(7,True)
    assert depth_7 == 84998978956

    
    # [20,400,8902,197281,119060324,3195901860,84998978956,2439530234167,69352859712417]











