def get_input(filename: str) -> list[tuple[int, int], list[list[int]]]:
    with open(filename) as f:
        lines = f.readlines()
        ordering_rules = [] 
        print_plans = []
        for line in lines:
            if "|" in line:
                ordering_rules.append((int(line.strip().split("|")[0]), int(line.strip().split("|")[1])))
            elif "," in line:
                print_plans.append([int(num) for num in line.strip().split(",")])
        return ordering_rules, print_plans
ordering_rules, print_plans = get_input("input.txt")

def is_print_plan_valid(ordering_rules: list[tuple[int, int]], print_plan: list[int]) -> bool:
    for ordering_rule in ordering_rules:
        rule_applies = ordering_rule[0] in print_plan and ordering_rule[1] in print_plan
        for _ in print_plan:
            if rule_applies and not print_plan.index(ordering_rule[0]) < print_plan.index(ordering_rule[1]):
                return False
    return True

def fix_invalid_print_plan(ordering_rules: list[tuple[int, int]], print_plan: list[int]) -> list[int]:
    while not is_print_plan_valid(ordering_rules, print_plan):
        for ordering_rule in ordering_rules:
            rule_applies = ordering_rule[0] in print_plan and ordering_rule[1] in print_plan
            if rule_applies and not print_plan.index(ordering_rule[0]) < print_plan.index(ordering_rule[1]):
                index_0, index_1 = print_plan.index(ordering_rule[0]), print_plan.index(ordering_rule[1])
                print_plan[index_0], print_plan[index_1] = print_plan[index_1], print_plan[index_0]
    return print_plan

valid_print_plans, invalid_print_plans = [], []
for print_plan in print_plans:
    if is_print_plan_valid(ordering_rules, print_plan):
        valid_print_plans.append(print_plan)
    else:
        invalid_print_plans.append(print_plan)

# Section A
mid_index_valid_sum = sum([print_plan[len(print_plan) // 2] for print_plan in valid_print_plans])
print("Section A sum of mid-index values in print plans:", mid_index_valid_sum)

# Section B
fixed_print_plans = [fix_invalid_print_plan(ordering_rules, invalid_print_plan) for invalid_print_plan in invalid_print_plans]
mid_values_fixed_sum = sum([print_plan[len(print_plan) // 2] for print_plan in fixed_print_plans])
print("Section B sum of mid-index values in fixed print plans:", mid_values_fixed_sum)