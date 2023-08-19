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
a5a6:3653
a5a4:3394
b4f4:606
b4b1:4199
b4b2:3328
b4c4:3797
b4b3:3658
b4d4:3622
b4e4:3391
b4a4:3019
e2e3:3107
e2e4:2815
g2g3:1014
g2g4:3770
"""

output2 = """
e2e3: 3107
g2g3: 1014
a5a6: 3653
e2e4: 2748
g2g4: 3702
b4b1: 4199
b4b2: 3328
b4b3: 3658
b4a4: 3019
b4c4: 3797
b4d4: 3622
b4e4: 3391
b4f4: 606
a5a4: 3394
"""

compare_outputs(output1, output2)