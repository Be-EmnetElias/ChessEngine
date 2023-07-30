from src.board import Board

board = Board("default")

def test_move_count_depth():
    
    expected_output = [20,400,8902,197281,119060324,3195901860,84998978956,2439530234167,69352859712417]
    actual_output = list()

    
    depth = len(expected_output)

    def move_count_depth(depth: int) -> int:

        white_moves = board.all_legal_moves(True)
        black_moves = board.all_legal_moves(False)

        for piece in white_moves:
            for move in white_moves[piece]:
                board.move_piece(piece,move,simulation=True)
                print(f"{piece} {move}")
                board.undo_move(move)

        board.print_board()

    move_count_depth(0)
    

    assert actual_output == expected_output


