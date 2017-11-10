function applyPsychogram(psychogramData) {

    var criticalValueColor = "#f57888";
    var mediumValueColor = "#ffb900";
    var fineValueColor = "#b9e88b";
    var criticalLevel = 1;
    var mediumLevel = 3;

    var scores = {
        subjects: psychogramData["subjects"],
        environment: psychogramData["environment"],
        constitution: psychogramData["constitution"],
        interests: psychogramData["interests"],
        working: psychogramData["working"]
    };

    var categoryNameMapping = {
        subjects: "s",
        environment: "e",
        constitution: "c",
        interests: "i",
        working: "w"
    };

    for (var categoryName in scores) {
        var scoreList = scores[categoryName];
        for (var ind = 0; ind < scoreList.length; ind++) {
            var subcategoryName = scoreList[ind][0].toUpperCase();
            var score = scoreList[ind][1];
            renderSubcategoryScore(categoryName, ind, score);
            applySubcategoryText(categoryName, ind, subcategoryName);
        }
    }

    function renderSubcategoryScore(categoryName, subcategoryIndex, score) {
        if (score > 0) {
            var affectedCells = Math.min(score, 5);
            var color;
            if (score <= criticalLevel) color = criticalValueColor;
                else if (score <= mediumLevel) color = mediumValueColor;
                else color = fineValueColor;
            for (var counter = 0; counter < affectedCells; counter++) {
                var cellId = getCellId(categoryName, subcategoryIndex, 5 - counter);
                var cell = $('#' + cellId);
                if (cell) cell.attr("fill", color);
            }
        }
    }

    function getCellId(categoryName, subcategoryIndex, cellIndex) {
        return categoryNameMapping[categoryName] + "-" + subcategoryIndex + "-" + cellIndex;
    }

    function applySubcategoryText(categoryName, subcategoryIndex, subcategoryName) {
        var cellId = categoryNameMapping[categoryName] + "-" + subcategoryIndex;
        $('#' + cellId + ' tspan').text(subcategoryName);
    }
}