const keywords = (string) => {
    var regexes = [
        new RegExp(" \s\s+","gi"),
        new RegExp("\\bthe\\b","gi"),
        new RegExp("\\ba\\b","gi"),
        new RegExp("\\ban\\b","gi"),
        new RegExp("\\bto\\b","gi"),
        new RegExp("\\bby\\b","gi"),
        new RegExp("\\bfor\\b","gi"),
        new RegExp("\\bin\\b","gi"),
        new RegExp("\\band\\b","gi"),
        new RegExp("\\bwhen\\b","gi"),
        new RegExp("\\bwhere\\b","gi"),
        new RegExp("\\bwhat\\b","gi"),
        new RegExp("\\bhow\\b","gi"),
        new RegExp("\\bwhy\\b","gi"),
        new RegExp("\\bis\\b","gi"),
        new RegExp("\\bare\\b","gi"),
        new RegExp(" \s\s+","gi"),
        new RegExp("[=+*\/-]","gi"),
        new RegExp("\\bor\\b","gi"),
        new RegExp("\\bof\\b","gi"),
        new RegExp("[0-9]","gi"),
        new RegExp("[, ]+","gi")
    ];

    let i; 
    for (i = 0; i < regexes.length; i++) {
        if (i > 16 || i < 20){
            string = string.replace(regexes[parseInt(i, 10)]," ");
        } else {
            string = string.replace(regexes[parseInt(i, 10)],",");
        }

        string = string.replace(regexes[parseInt(i, 10)]," ");
    }

    string = string.replace(new RegExp(" ","gi"),",");

    string = string.split(",").filter((item,j,allItems) => {
        return j === allItems.indexOf(item);
    });

    return string; 
};


module.exports = keywords;
