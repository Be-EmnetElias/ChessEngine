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
a6b5:1
a6b7:1
a6c4:1
a6c8:1
a6d3:1
a6e2:1
a8b8:1
a8c8:1
a8d8:1
b4b3:1
b4c3:1
b6a4:1
b6c4:1
b6c8:1
b6d5:1
c7c5:1
c7c6:1
d7d6:1
e6d5:1
e7c5:1
e7d6:1
e7d8:1
e7f8:1
e8c8:1
e8d8:1
e8f8:1
e8g8:1
f6d5:1
f6e4:1
f6g4:1
f6g8:1
f6h5:1
f6h7:1
g2f1b:1
g2f1n:1
g2f1q:1
g2f1r:1
g2g1b:1
g2g1n:1
g2g1q:1
g2g1r:1
g6g5:1
g7f8:1
g7h6:1
h8f8:1
h8g8:1
h8h2:1
h8h3:1
h8h4:1
h8h5:1
h8h6:1
h8h7:1
"""

output2 = """
b4b3: 1
g6g5: 1
c7c6: 1
d7d6: 1
c7c5: 1
g2f1q: 1
g2f1r: 1
g2f1b: 1
g2f1n: 1
g2g1q: 1
g2g1r: 1
g2g1b: 1
g2g1n: 1
e6d5: 1
b4c3: 1
b6a4: 1
b6c4: 1
b6d5: 1
b6c8: 1
f6e4: 1
f6g4: 1
f6d5: 1
f6h5: 1
f6h7: 1
f6g8: 1
a6e2: 1
a6d3: 1
a6c4: 1
a6b5: 1
a6b7: 1
a6c8: 1
g7h6: 1
g7f8: 1
a8b8: 1
a8c8: 1
a8d8: 1
h8h2: 1
h8h3: 1
h8h4: 1
h8h5: 1
h8h6: 1
h8h7: 1
h8f8: 1
h8g8: 1
e7c5: 1
e7d6: 1
e7d8: 1
e7f8: 1
e8d8: 1
e8f8: 1
e8g8: 1
e8c8: 1

"""

compare_outputs(output1, output2)