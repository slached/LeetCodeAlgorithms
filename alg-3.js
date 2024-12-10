/*
 * @param {number[]} nums
 * @return {number}
 */

var maxSumDivThree = function (nums) {
  const allPossibilitiesOfSum = [];
  // this outer loop is presents how many element does include to the summation
  let allSums = {};
  for (let i = 1; i <= nums.length; i++) {
    allSums[i] = [];
    let allCombinationsInCurrentSum = combination(nums.length, i);
    while (allCombinationsInCurrentSum > 0) {
      // tempSumArr contains elements that involves to the summation for each iteration
      const tempSumArr = [];
      // note i in here presents how many element going to push into tempSumArr (after the push we will sum these numbers)
      
      // this for loop for selection of the numbers
      for (let j = 0; j <  nums.length -1; j++) {
        
      }

      allSums[i].push(tempSumArr);
      allCombinationsInCurrentSum--;
    }
  }

  return allPossibilitiesOfSum;
};

const summationOfArray = (array) => {
  if (array.length <= 0) return 0;
  return array[0] + summationOfArray(array.slice(1, array.length));
};

const combination = (top, bottom) => {
  let res = 1;
  for (let i = 0; i < bottom; i++) res *= top - i;
  for (let i = bottom; i > 1; i--) res /= i;
  return res;
};

console.log(maxSumDivThree([1, 2, 3, 4, 5]));
