function applyPsychogram(psychogramData) {
	var criticalValueColor = "#f57888";
	var mediumValueColor = "#ffb900";
	var fineValueColor = "#b9e88b";
	var criticalLevel = 1;
	var mediumLevel = 3;

	var categories = {
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

	for (var categoryName in categories) {
		var subcategories = categories[categoryName].subcategories;
		var categoryDescription = categories[categoryName].description;
		applyCategoryDescription(categoryName, categoryDescription);
		for (var ind = 0; ind < subcategories.length; ind++) {
			var subcategoryName = subcategories[ind].name;
			var score = subcategories[ind].score;
			var subcategoryDescription = subcategories[ind].description;
			applySubcategoryScore(categoryName, ind, score);
			applySubcategoryNameAndDescription(categoryName, ind, subcategoryName, subcategoryDescription);
		}
	}

	function applySubcategoryScore(categoryName, subcategoryIndex, score) {
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

    function applyCategoryDescription(categoryName, description) {
        var cellId = categoryNameMapping[categoryName] + "-title";
        $('#' + cellId).text(description);
    }

	function applySubcategoryNameAndDescription(subCategory, subcategoryIndex, name, description) {
        var nameId = categoryNameMapping[subCategory] + "-" + subcategoryIndex;
        var descriptionId = categoryNameMapping[subCategory] + "-" + subcategoryIndex + "-title";
        $('#' + nameId + ' tspan').text(getStringForSize(name.toUpperCase(), 110));
        $('#' + descriptionId).text(description);
	}

    function getWidthOfText(text) {
        var span = $('<span></span>');
        span.text(text);
        $('body').append(span);
        var w = span.width();
        span.remove();
        return w;
    }

    function getStringForSize(text, desiredSize) {
        var curSize = getWidthOfText(text);
        if(curSize > desiredSize)
        {
            var curText = text.substring(0,text.length-5) + '...';
            return getStringForSize(curText, desiredSize);
        }
        else
        {
            return text;
        }
    }

}