from re import sub

def run_length_encode(s):
    return sub(
        r'(.)\1*', 
        lambda m: str(len(m.group(0))) + m.group(1),
        s
    )
    
assert "3a2b1c" == run_length_encode("aaabbc")
assert "" == run_length_encode("")
assert "1a" == run_length_encode("a")
assert "1a1b1c1d1e3f" == run_length_encode("abcdefff")
assert "3a" == run_length_encode("aaa")
assert "1a4f" == run_length_encode("affff")
assert "1a2b2a1c1a" == run_length_encode("abbaaca")
