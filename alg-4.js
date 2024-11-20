/*
 * @param {number[][]} points
 * @return {number}
 */

const minTimeToVisitAllPoints = function (points) {
  let sec = 0;

  const moveTheDot = (point, toWhere, incOrDec) => {
    sec++;
    switch (toWhere) {
      case "diagonal_same":
        return [point[0] + incOrDec, point[1] + incOrDec];
      case "diagonal_opposite":
        return [point[0] - incOrDec, point[1] + incOrDec];
      case "vertical":
        return [point[0], point[1] + incOrDec];
      case "horizontal":
        return [point[0] + incOrDec, point[1]];
    }
  };

  for (let i = 0; i < points.length - 1; i++) {
    let currentPoint = points[i];
    const nextPoint = points[i + 1];

    //compare the x and y at the same time
    while (JSON.stringify(currentPoint) !== JSON.stringify(nextPoint)) {
      // if diagonal inc or dec happen continue must trigger

      // x and y both lower
      if (currentPoint[0] < nextPoint[0] && currentPoint[1] < nextPoint[1]) {
        currentPoint = moveTheDot(currentPoint, "diagonal_same", 1);
        continue;
        // x and y both higher
      } else if (currentPoint[0] > nextPoint[0] && currentPoint[1] > nextPoint[1]) {
        currentPoint = moveTheDot(currentPoint, "diagonal_same", -1);
        continue;
      }
      // x higher y lower
      else if (currentPoint[0] > nextPoint[0] && currentPoint[1] < nextPoint[1]) {
        currentPoint = moveTheDot(currentPoint, "diagonal_opposite", 1);
        continue;
      }
      // x lower y higher
      else if (currentPoint[0] < nextPoint[0] && currentPoint[1] > nextPoint[1]) {
        currentPoint = moveTheDot(currentPoint, "diagonal_opposite", -1);
        continue;
      }
      if (currentPoint[0] < nextPoint[0]) {
        currentPoint = moveTheDot(currentPoint, "horizontal", 1);
      } else if (currentPoint[0] > nextPoint[0]) {
        currentPoint = moveTheDot(currentPoint, "horizontal", -1);
      }

      if (currentPoint[1] < nextPoint[1]) {
        currentPoint = moveTheDot(currentPoint, "vertical", 1);
      } else if (currentPoint[1] > nextPoint[1]) {
        currentPoint = moveTheDot(currentPoint, "vertical", -1);
      }
    }
  }
  return sec;
};

console.log(
  minTimeToVisitAllPoints([
    [3, 2],
    [-123, 52],
  ])
);
