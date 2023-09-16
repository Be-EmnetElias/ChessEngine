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
e1d1:1894
e1c1:1887
e1f1:1857
e1g1:2059
d5d6:1991
d5e6:2241
a2a4:2149
a2a3:2186
b2b3:1964
g2g4:1843
g2g3:1882
g2h3:1970
e5d7:2124
e5f7:2080
e5c6:2027
e5g6:1997
e5c4:1880
e5g4:1878
e5d3:1803
c3b5:2138
c3a4:2203
c3b1:2038
c3d1:2040
d2e3:2136
d2f4:2000
d2g5:2134
d2h6:2019
d2c1:1963
e2d3:2050
e2c4:2082
e2b5:2057
e2a6:1907
e2d1:1733
e2f1:2060
a1b1:1969
a1c1:1968
a1d1:1885
h1g1:2013
h1f1:1929
f3f4:2132
f3f5:2396
f3f6:2111
f3g4:2169
f3h5:2267
f3e3:2174
f3d3:2005
f3g3:2214
f3h3:2360
"""

output2 = """
a2a3: 2186
b2b3: 1964
g2g3: 1882
d5d6: 1991
a2a4: 2149
g2g4: 1843
g2h3: 1970
d5e6: 2241
c3b1: 2038
c3d1: 2040
c3a4: 2203
c3b5: 2138
e5d3: 1803
e5c4: 1880
e5g4: 1878
e5c6: 2027
e5g6: 1997
e5d7: 2124
e5f7: 2080
d2c1: 1963
d2e3: 2136
d2f4: 2000
d2g5: 2134
d2h6: 2019
e2d1: 1733
e2f1: 2060
e2d3: 2050
e2c4: 2082
e2b5: 2057
e2a6: 1907
a1b1: 1969
a1c1: 1968
a1d1: 1885
h1f1: 1929
h1g1: 2013
f3d3: 2005
f3e3: 2174
f3g3: 2214
f3h3: 2360
f3f4: 2132
f3g4: 2169
f3f5: 2396
f3h5: 2267
f3f6: 2111
e1d1: 1894
e1f1: 1855
e1g1: 2059
e1c1: 1887
"""

compare_outputs(output1, output2)