
class Solution:
    def minOperations(self, nums: List[int], x: int, k: int) -> int:
        n = len(nums)

        # Coordinate compression
        vals = sorted(set(nums))
        size = len(vals)

        pos = {v: i + 1 for i, v in enumerate(vals)}

        # Fenwick Trees
        count = [0] * (size + 1)
        total = [0] * (size + 1)

        def update(bit, i, delta):
            while i <= size:
                bit[i] += delta
                i += i & -i

        def query(bit, i):
            res = 0
            while i > 0:
                res += bit[i]
                i -= i & -i
            return res

        def kth(target):
            idx = 0
            step = 1 << (size.bit_length() - 1)

            while step:
                nxt = idx + step

                if nxt <= size and count[nxt] < target:
                    target -= count[nxt]
                    idx = nxt

                step >>= 1

            return idx + 1

        def window_cost():
            mid = kth((x + 1) // 2)
            median = vals[mid - 1]

            left_count = query(count, mid)
            left_sum = query(total, mid)

            all_sum = query(total, size)

            right_count = x - left_count
            right_sum = all_sum - left_sum

            return (median * left_count - left_sum +
                    right_sum - median * right_count)

        # Build first window
        for i in range(x):
            p = pos[nums[i]]
            update(count, p, 1)
            update(total, p, nums[i])

        costs = []

        for i in range(n - x + 1):
            costs.append(window_cost())

            if i + x < n:
                old = pos[nums[i]]
                new = pos[nums[i + x]]

                update(count, old, -1)
                update(total, old, -nums[i])

                update(count, new, 1)
                update(total, new, nums[i + x])

        # DP: minimum cost to select j non-overlapping windows
        INF = float('inf')

        prev = [0] * (n + 1)

        for j in range(1, k + 1):
            curr = [INF] * (n + 1)

            for i in range(1, n + 1):
                curr[i] = curr[i - 1]

                if i >= x:
                    curr[i] = min(
                        curr[i],
                        prev[i - x] + costs[i - x]
                    )

            prev = curr

        return prev[n]
