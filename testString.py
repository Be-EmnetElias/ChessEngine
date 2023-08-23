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
a1b1:3829205
a1c1:3815954
a1d1:3570095
a2a3:4627439
a2a4:4387586
b2b3:3768824
c3a4:4628497
c3b1:3996171
c3b5:4317482
c3d1:3995761
d2c1:3793390
d2e3:4407041
d2f4:3941257
d2g5:4370915
d2h6:3967365
d5d6:3835265
d5e6:4727437
e1c1:3551583
e1d1:3559113
e1f1:3377351
e1g1:4119629
e2a6:3553501
e2b5:4032348
e2c4:4182989
e2d1:3074219
e2d3:4066966
e2f1:4095479
e5c4:3494887
e5c6:4083458
e5d3:3288812
e5d7:4404043
e5f7:4164923
e5g4:3415992
e5g6:3949417
f3d3:3949570
f3e3:4477772
f3f4:4327936
f3f5:5271134
f3f6:3975992
f3g3:4669768
f3g4:4514010
f3h3:5067173
f3h5:4743335
g2g3:3472039
g2g4:3338154
g2h3:3819456
h1f1:3687422
h1g1:3991120
"""

output2 = """
a2a3: 4627439
b2b3: 3768824
g2g3: 3472039
d5d6: 3835265
a2a4: 4387586
g2g4: 3338154
g2h3: 3819456
d5e6: 4727437
c3b1: 3996171
c3d1: 3995761
c3a4: 4628497
c3b5: 4317482
e5d3: 3288812
e5c4: 3494887
e5g4: 3415992
e5c6: 4083458
e5g6: 3949417
e5d7: 4404043
e5f7: 4164923
d2c1: 3793390
d2e3: 4407041
d2f4: 3941257
d2g5: 4370915
d2h6: 3967365
e2d1: 3074219
e2f1: 4095479
e2d3: 4066966
e2c4: 4182989
e2b5: 4032348
e2a6: 3553501
a1b1: 3827454
a1c1: 3814203
a1d1: 3568344
h1f1: 3685756
h1g1: 3989454
f3d3: 3949570
f3e3: 4477772
f3g3: 4669768
f3h3: 5067173
f3f4: 4327936
f3g4: 4514010
f3f5: 5271134
f3h5: 4743335
f3f6: 3975992
e1d1: 3559113
e1f1: 3377351
e1g1: 4119629
e1c1: 3551583
"""

compare_outputs(output1, output2)