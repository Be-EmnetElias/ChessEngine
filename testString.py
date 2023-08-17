'''
Move: h2h4 - Output1: 218848 | Output2: 218829 --run depth 4 on rnbqkbnr/pppppppp/8/8/7P/8/PPPPPPP1/RNBQKBNR b Qkq e2 1-0
Move: g7g5 - Output1: 11664 | Output2: 11645 -- run depth 3 on rnbqkbnr/pppppp1p/8/6p1/7P/8/PPPPPPP1/RNBQKBNR w Qkq d3 1-1


'''
def compare_outputs(output1: str, output2: str) -> None:
    """
    Compare two outputs and print the differences.

    :param output1: The first output string.
    :param output2: The second output string.
    """
    lines1 = output1.strip().split('\n')
    lines2 = output2.strip().split('\n')

    moves1 = {line.split(":")[0].strip(): int(line.split(":")[1].strip()) for line in lines1}
    moves2 = {line.split(":")[0].strip(): int(line.split(":")[1].strip()) for line in lines2}

    # Find moves that are missing or added
    missing_moves = set(moves1.keys()) - set(moves2.keys())
    added_moves = set(moves2.keys()) - set(moves1.keys())

    perfect_match = True

    if missing_moves:
        print("Moves missing in Output2:", ", ".join(missing_moves))
        perfect_match = False

    if added_moves:
        print("Moves added in Output2:", ", ".join(added_moves))
        perfect_match = False

    # If there are no missing or added moves, then compare the values for each move
    if not missing_moves and not added_moves:
        for move, value1 in moves1.items():
            value2 = moves2[move]

            if value1 != value2:
                print(f"Move: {move} - Output1: {value1} | Output2: {value2}")
                perfect_match = False

    if perfect_match:
        print("The outputs are identical!")



output1 = """
f7f6:9366
f7f5:9887
h7h5:9102
h7h6:9350
e7e5:14634
e7e6:14608
g7g5:11664
g7g6:10390
c7c5:10796
c7c6:10275
b8c6:10809
b8a6:9842
a7a6:9366
a7a5:10335
g8f6:10783
g8h6:9863
b7b6:10352
b7b5:10339
d7d6:13281
d7d5:13806
"""

output2 = """
a7a6: 9366
b7b6: 10352
c7c6: 10275
d7d6: 13281
e7e6: 14608
f7f6: 9366
g7g6: 10390
h7h6: 9350
a7a5: 10335
b7b5: 10339
c7c5: 10796
d7d5: 13806
e7e5: 14634
f7f5: 9887
g7g5: 11645
h7h5: 9102
b8a6: 9842
b8c6: 10809
g8f6: 10783
g8h6: 9863
"""

compare_outputs(output1, output2)