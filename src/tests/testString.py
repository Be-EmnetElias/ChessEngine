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
e2d3:22
e2e3:22
e2f3:22
e2d2:22
e2f2:22
e2d1:22
b7b8q:18
b7b8b:22
b7b8r:18
b7b8n:21
b7c8q:16
b7c8b:17
b7c8r:17
b7c8n:17
b7a8q:22
b7a8b:22
b7a8r:22
b7a8n:22
f1e3:22
f1g3:22
f1d2:22
f1h2:22
h1g3:22
h1f2:22
"""

output2 = """
b7c8q: 2
b7c8r: 3
b7c8b: 17
b7c8n: 17
b7a8q: 16
b7a8r: 16
b7a8b: 20
b7a8n: 20
b7b8q: 18
b7b8r: 18
b7b8b: 22
b7b8n: 21
f1d2: 22
f1h2: 22
f1e3: 22
f1g3: 22
h1f2: 22
h1g3: 22
e2d1: 22
e2f3: 22
e2d2: 22
e2f2: 22
e2d3: 22
e2e3: 22
"""

compare_outputs(output1, output2)