const numbers = [18, 26, 40, 35, 10, 9, 17, 48, 4, 14, 32, 25, 20, 47, 15, 8, 11, 46, 44, 2,
    24, 31, 1, 22, 49, 41, 4, 27, 19, 14, 12, 16, 40, 18, 3, 28, 43, 50, 28, 34,
    37, 10, 24, 45, 36, 44, 42, 8, 38, 21, 39, 25, 9, 6, 12, 21, 20, 19, 6, 22,
    36, 2, 27, 23, 35, 37, 33, 50, 46, 32, 23, 45, 5, 31, 49, 39, 42, 11, 29, 15,
    16, 47, 29, 1, 5, 13, 30, 17, 43, 33, 38, 3, 13, 7, 41, 30, 7, 48, 34, 26,
    101]

let quantityArr = []
for (const [index, number] of numbers.entries()) {
    if (quantityArr[number] === undefined) quantityArr[number] = 0
    quantityArr[number]++
}
console.log(quantityArr.indexOf(1))

