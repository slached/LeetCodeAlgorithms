class Solution(object):
    def twoSum(self, nums, target):

        for i in range(len(nums)):
            for j in range(i + 1, len(nums)):
                if nums[i] + nums[j] == target:
                    return [i, j]


print(Solution().twoSum([1, 2, 3, 4, 5, 6, 7, 12, 34], 35))
