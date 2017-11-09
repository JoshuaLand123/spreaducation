$(function() {
    var criticalValueColor = "#f57888";
    var fineValueColor = "#b9e88b";
    var criticalLevel = 1;

    var scores = {
        specialist: [1, 1, 4, 5],
        environment: [1, 1, 2, 2],
        constitution: [3, 1, 2, 2],
        interests: [2, 2, 4],
        workmode: [5, 2, 1, 0],
    };

    var categoryNameMapping = {
        specialist: "s",
        environment: "e",
        constitution: "c",
        interests: "i",
        workmode: "w"
    };

    for (var categoryName in scores) {
        var scoreList = scores[categoryName];
        for (var ind = 0; ind < scoreList.length; ind++) {
            var score = scoreList[ind];            
            renderSubcategoryScore(categoryName, ind, score);
        }
    }

    function renderSubcategoryScore(categoryName, subcategoryIndex, score) {
        if (score > 0) {
            var affectedCells = Math.min(score, 5);
            var color = score > criticalLevel ? fineValueColor : criticalValueColor;
            for (var counter = 0; counter < affectedCells; counter++) {
                var cellId = getCellId(categoryName, subcategoryIndex, 5 - counter);
                var cell = document.getElementById(cellId);
                if (cell) cell.setAttribute("fill", color);
            }
        }        
    }

    function getCellId(categoryName, subcategoryIndex, cellIndex) {
        return categoryNameMapping[categoryName] + "-" + subcategoryIndex + "-" + cellIndex;
    }
})();