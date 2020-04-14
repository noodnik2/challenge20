Notes

#### Finding Primes
- For small numbers (e.g., < 10M)
  - Sieve of Eratosthenes
  - Trial division

```
function is_prime(n)
    if n ≤ 3 then
        return n > 1
    else if n mod 2 = 0 or n mod 3 = 0
        return false

    let i ← 5

    while i × i ≤ n do
        if n mod i = 0 or n mod (i + 2) = 0
            return false
        i ← i + 6

    return true
```  
  
#### Magic Squares

- Use different algorithms to populate (for size nxn square):
  - n is odd (magic # is n * center cell value)
    - E.g., simple n=3, from 1..9, center cell always 5 => magic # is 15
    - 8 possible solutions (flip { horiz, vert, diag r, diag l }, rotate { 0, 90, 180, 270 })
  - 4x4 magic number at least 34
    - Specific algorithm to populate cells
      - Use alternation of “groups” & “clockwise/counterclockwise” fills
    - Magic # is 2 x (first # + last #) -> e.g., (1 + 16) x 2 => 34 for n=4
