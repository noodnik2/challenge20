
sorted_ranks = "23456789TJQKA"
rank_scores = { rank: value for value, rank in enumerate(sorted_ranks) }

# returns a tuple "score" for the given hand (an array of cards),
# which can be compared to that from any other hand, according to
# the grading system described in README.md
def calc_score(hand):
    (straight, flush) = (is_straight(hand), is_flush(hand))
    if straight and flush:
        return standard_hand_score(5, hand)
    if is_three_of_a_kind(hand):
        return standard_hand_score(4, hand)
    if straight:
        return standard_hand_score(3, hand)
    if flush:
        return standard_hand_score(2, hand)
    if is_pair(hand):
        return pair_hand_score(1, hand)
    return standard_hand_score(0, hand)

def is_straight(hand):
    return ''.join(ordered_card_ranks(hand)) in sorted_ranks

def is_flush(hand):
    return len({card[1] for card in hand}) == 1

def is_three_of_a_kind(hand):
    return len({card[0] for card in hand}) == 1

def is_pair(hand):
    return len({card[0] for card in hand}) == 2

def standard_hand_score(hand_type, hand):
    return tuple(sum([[hand_type], descending_rank_scores(hand)], []))

def pair_hand_score(hand_type, hand):
    rank_frequencies = calc_rank_freq(hand)
    assert len(rank_frequencies) == 2
    higher_pair_rank = max(rank_frequencies, key=rank_frequencies.get)
    lower_pair_rank = min(rank_frequencies, key=rank_frequencies.get)
    return (
        hand_type,
        rank_scores[higher_pair_rank],
        rank_scores[lower_pair_rank]
    )

def calc_rank_freq(hand):
    freq = {}
    for rank in [card[0] for card in hand]:
        freq[rank] = freq.get(rank, 0) + 1
    return freq

def ordered_card_ranks(hand):
    return sorted([card[0] for card in hand], key=lambda r: rank_scores[r])

def descending_rank_scores(hand):
    return sorted([rank_scores[card[0]] for card in hand], reverse=True)
