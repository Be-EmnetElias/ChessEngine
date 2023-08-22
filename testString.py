'''


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
a1b1:49
a1c1:49
a1d1:49
a2a3:49
a2a4:49
b2b3:47
c3a4:48
c3b1:48
c3b5:45
c3d1:48
d2c1:48
d2e3:48
d2f4:48
d2g5:47
d2h6:44
d5d6:46
d5e6:51
e1c1:49
e1d1:49
e2a6:41
e2b5:44
e2c4:46
e2d1:49
e2d3:47
e2f1:53
e2f3:49
e2g4:49
e2h5:47
e5c4:47
e5c6:46
e5d3:48
e5d7:49
e5f3:49
e5f7:48
e5g4:49
e5g6:47
f2f3:48
f2f4:48
f6e6:48
f6e7:1
f6f3:53
f6f4:53
f6f5:54
f6f7:2
f6g5:51
f6g6:51
f6g7:47
f6h4:51
h1f1:49
h1g1:42
h2h3:47
h2h4:46
"""

output2 = """
a2a3: 49
b2b3: 47
f2f3: 48
h2h3: 47
d5d6: 46
a2a4: 49
f2f4: 48
h2h4: 46
d5e6: 51
c3b1: 47
c3d1: 47
c3a4: 47
c3b5: 44
e5d3: 48
e5f3: 49
e5c4: 47
e5g4: 49
e5c6: 46
e5g6: 47
e5d7: 49
e5f7: 48
d2c1: 48
d2e3: 48
d2f4: 48
d2g5: 47
d2h6: 44
e2d1: 49
e2f1: 53
e2d3: 47
e2f3: 49
e2c4: 46
e2g4: 49
e2b5: 44
e2h5: 47
e2a6: 41
a1b1: 48
a1c1: 48
a1d1: 48
h1f1: 48
h1g1: 40
f6f3: 53
f6f4: 53
f6h4: 51
f6f5: 54
f6g5: 51
f6e6: 48
f6g6: 51
f6e7: 1
f6f7: 2
f6g7: 47
e1d1: 48
e1c1: 48
"""

compare_outputs(output1, output2)