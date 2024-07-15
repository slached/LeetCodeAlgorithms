const reverseParentheses = (s) => {

    //count how many parentheses
    let parentheses = 0
    for (let i = 0; i < s.length; i++) {
        if (s.at(i) === "(") parentheses++
    }


    //this function finds value of inner parentheses and does the lots of things either but will not tell rest
    let whichParentheses = 0
    for (let i = 0; i < s.length; i++) {
        if (s.at(i) === "(") {
            whichParentheses++
            if (whichParentheses === parentheses) {
                for (let j = i + 1; j < s.length; j++) {
                    if (s.at(j) === ")") {
                        //here is where inner value is
                        //first we reverse the string than rerun this process till there is no parentheses
                        const reversedString = s.substring(i + 1, j).split("").reduce((acc, char) => char + acc, "")
                        //first and second half is slices string into two pieces without inner parentheses and
                        //after reverse the current string merge these 3 part into one and rerun the function
                        const firstHalf = s.substring(0, i)
                        const secondHalf = s.substring(j + 1, s.length)
                        const lastVersionOfString = firstHalf + reversedString + secondHalf
                        return reverseParentheses(lastVersionOfString)
                    }
                }
            }
        }
    }
    //this is return of last run here will trigger if only string does not contain any brackets
    return s
}

console.log(reverseParentheses("(a(bc)d)"))