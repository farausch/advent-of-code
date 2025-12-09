import re

def get_input_string(filename: str) -> str:
    with open(filename, "r") as f:
        return f.read().strip()
input = get_input_string("input.txt")

mul_regex = r"mul\((\d+),(\d+)\)"

# Section A
correct_muls = re.findall(mul_regex, input)
correct_muls_sum = sum(int(a) * int(b) for a, b in correct_muls)
print(f"Section A sum: {correct_muls_sum}")

# Section B
filter_regex = r"don't\(\).*?do\(\)"
input = input + "do()"
filtered_input = re.sub(filter_regex, "", input, flags=re.DOTALL)
correct_muls = re.findall(mul_regex, filtered_input)
correct_muls_sum = sum(int(a) * int(b) for a, b in correct_muls)
print(f"Section B sum: {correct_muls_sum}")