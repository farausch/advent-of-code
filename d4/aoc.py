def get_input_matrix(filename: str) -> list[list[str]]:
    with open(filename) as f:
        return [list(line.strip()) for line in f]
input_matrix = get_input_matrix("input.txt")
KEYWORDS_S1, KEYWORDS_S2 = ["XMAS", "SAMX"], ["MAS", "SAM"]

def is_linear_lr_match(matrix_row: list[str], x: int, keyword: str) -> bool:
    return "".join(matrix_row[x:x+len(keyword)]) == keyword

def is_diagonal_right_match(matrix: list[list[str]], x: int, y: int, keyword: str) -> bool:
    if x + len(keyword) > len(matrix[0]) or y + len(keyword) > len(matrix):
        return False
    return "".join([matrix[y+i][x+i] for i in range(len(keyword))]) == keyword

def is_diagonal_left_match(matrix: list[list[str]], x: int, y: int, keyword: str) -> bool:
    if x - len(keyword) < -1 or y + len(keyword) > len(matrix):
        return False
    return "".join([matrix[y+i][x-i] for i in range(len(keyword))]) == keyword

# Section A
keyword_matches_sum = 0
for y in range(len(input_matrix)):
    for x in range(len(input_matrix[0])):
        if is_linear_lr_match(input_matrix[y], x, KEYWORDS_S1[0]) or is_linear_lr_match(input_matrix[y], x, KEYWORDS_S1[1]):
            keyword_matches_sum += 1
        if is_diagonal_right_match(input_matrix, x, y, KEYWORDS_S1[0]) or is_diagonal_right_match(input_matrix, x, y, KEYWORDS_S1[1]):
            keyword_matches_sum += 1
        if is_diagonal_left_match(input_matrix, x, y, KEYWORDS_S1[0]) or is_diagonal_left_match(input_matrix, x, y, KEYWORDS_S1[1]):
            keyword_matches_sum += 1
for x in range(len(input_matrix[0])):
    col = [input_matrix[y][x] for y in range(len(input_matrix))]
    for y in range(len(col)):
        if is_linear_lr_match(col, y, KEYWORDS_S1[0]) or is_linear_lr_match(col, y, KEYWORDS_S1[1]):
            keyword_matches_sum += 1
print("Section A keyword matches found:", keyword_matches_sum)

# Section B
keyword_matches_sum = 0
for y in range(len(input_matrix)):
    for x in range(len(input_matrix[0])):
        if (is_diagonal_right_match(input_matrix, x, y, KEYWORDS_S2[0]) or is_diagonal_right_match(input_matrix, x, y, KEYWORDS_S2[1])) \
            and (is_diagonal_left_match(input_matrix, x + 2, y, KEYWORDS_S2[0]) or is_diagonal_left_match(input_matrix, x + 2, y, KEYWORDS_S2[1])):
            keyword_matches_sum += 1
print("Section B keyword matches found:", keyword_matches_sum)
            