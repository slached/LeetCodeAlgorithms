
const numbers = [3, 3, 6, 1, 2, 6, 2]

const singleNumber = (nums) => {
    let frequency = {}
    for (const [index, number] of nums.entries()) {
        if (frequency[number] === undefined) frequency[number] = 0
        frequency[number]++
    }
    let returnValue
    for (const [key, value] of Object.entries(frequency)) {
        if (value === 1) returnValue = key
    }
    return returnValue
}


console.log(singleNumber(numbers))