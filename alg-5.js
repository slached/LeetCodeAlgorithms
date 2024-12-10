/*
 * @param {ListNode} l1
 * @param {ListNode} l2
 * @return {ListNode}
 */

var addTwoNumbers = function (l1, l2) {
  const lenDiff = Math.abs(l1.length - l2.length);

  if (l1.length > l2.length) {
    for (let i = 0; i < lenDiff; i++) l2.push(0);
  } else {
    for (let i = 0; i < lenDiff; i++) l1.push(0);
  }

  //this fills the lowest element with 0 till (from left) equation of lowest and bigger numbers digits are the same
  const l1LL = new ListNode(l1);
  const l2LL = new ListNode(l2);

  const resultArray = [];

  let tempL1 = l1LL;
  let tempL2 = l2LL;
  const max = Math.max(l1LL.getLen(), l2LL.getLen());

  for (let i = 0; i < max; i++) {
    const valSum = tempL1.val + tempL2.val;
    if (valSum > 9) {
      if (!tempL1.next) {
        // here means this is the last summation
        // and last two digit sum is grater than 9
        resultArray.push(valSum % 10);
        resultArray.push(1);
        break;
      } else tempL1.next.val++;
    }
    resultArray.push(valSum % 10);
    tempL1 = tempL1.next;
    tempL2 = tempL2.next;
  }

  return new ListNode(resultArray).getNum();
};

class ListNode {
  constructor(val) {
    this.val = val === undefined ? 0 : val[0];
    this.next = val.length > 1 ? new ListNode(val.slice(1, val.length)) : null;
  }
  getLen() {
    let len = 0;
    let cur = this;
    while (cur) {
      cur = cur.next;
      len++;
    }
    return len;
  }
  getNum() {
    let digit = "";
    let cur = this;
    while (cur) {
      digit = `${cur.val}${digit}`;
      cur = cur.next;
    }
    return parseInt(digit);
  }
}

console.log(addTwoNumbers([0, 0, 0, 1], [1]));
