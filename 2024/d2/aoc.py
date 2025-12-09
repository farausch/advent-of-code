def get_input_from_file(filename: str, delimiter: str) -> list:
    with open(filename, 'r') as file:
        return [list(map(int, line.strip().split(delimiter))) for line in file]

def is_purely_increasing(report: list) -> bool:
    return all(report[i] < report[i + 1] for i in range(len(report) - 1))

def is_purely_decreasing(report: list) -> bool:
    return all(report[i] > report[i + 1] for i in range(len(report) - 1))

def has_levels_diff_in_range(report: list, min: int = 1, max: int = 3) -> bool:
    return all(min <= abs(report[i] - report[i + 1]) <= max for i in range(len(report) - 1))

reports_list = get_input_from_file('input.txt', ' ')

# Section A
safe_reports = [report for report in reports_list if (is_purely_increasing(report) or is_purely_decreasing(report)) and has_levels_diff_in_range(report)]
print("Section A amount of safe reports:", len(safe_reports))

# Section B
total_safe_reports = 0
for report in reports_list:
    report_variants = [report]
    for i in range(0, len(report)):
        report_variants.append([report[j] for j in range(len(report)) if j != i])
    report_has_safe_variant = any((is_purely_increasing(r) or is_purely_decreasing(r)) and has_levels_diff_in_range(r) for r in report_variants)
    total_safe_reports += report_has_safe_variant
print("Section B amount of safe reports:", total_safe_reports)

