
# c2 -> c4
    # d7 -> d5
        # d1 -> a4

    # d7 -> d6
# c2 -> c3
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
        
        # Check if all moves are present in both outputs
        if moves1.keys() != moves2.keys():
            print("The outputs have different moves!")
            return

        # Compare the values for each move
        for move, value1 in moves1.items():
            value2 = moves2[move]
            
            if value1 != value2:
                print(f"Move: {move} - Output1: {value1} | Output2: {value2}")

output1 = """
c4c5 : 1
c4d5 : 1
d2d4 : 1
d2d3 : 1
d1b3 : 1
d1c2 : 1
d1a4 : 1
f2f3 : 1
f2f4 : 1
g1h3 : 1
g1f3 : 1
e2e3 : 1
e2e4 : 1
g2g3 : 1
g2g4 : 1
a2a4 : 1
a2a3 : 1
h2h3 : 1
h2h4 : 1
b2b3 : 1
b2b4 : 1
b1c3 : 1
b1a3 : 1
"""

output2 = """
a2a3: 1
b2b3: 1
c2c3: 1
d2d3: 1
e2e3: 1
f2f3: 1
g2g3: 1
h2h3: 1
a2a4: 1
b2b4: 1
c2c4: 1
d2d4: 1
e2e4: 1
f2f4: 1
g2g4: 1
h2h4: 1
b1a3: 1
b1c3: 1
g1f3: 1
g1h3: 1
"""

compare_outputs(output1, output2)