from c3p_scorer import calc_score

# returns an array of identifier(s) of the player(s) having the
# winning hand(s), as determined by comparing score(s) of player's
# hand(s) from the array "player_hands", each entry of which is
# itself an array of "<player_id> <card1> <card2> <card3>".  A
# tie is reflected when the return array has more than one entry.
def calc_winners(player_hands):

    best_hand_score = None
    best_hand_players = None

    for player_hand in player_hands:

        player_id = player_hand[0]
        hand = player_hand[1:]

        if best_hand_score is None:
            best_hand_score = calc_score(hand)
            best_hand_players = [player_id]
            continue

        challenger_hand_score = calc_score(hand)
        if challenger_hand_score > best_hand_score:
            best_hand_score = challenger_hand_score
            best_hand_players = [player_id]
            continue

        if challenger_hand_score == best_hand_score:
            best_hand_players += player_id

    return sorted(best_hand_players, key=lambda v: int(v))
