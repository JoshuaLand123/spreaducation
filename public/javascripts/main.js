
function validateProfileForm() {
    if (navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1) {
        var dobValue = $('#dob').val();
        var dobSafariPattern = new RegExp('^[1-2][0,1,9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$');
        if(dobValue == undefined || dobValue == "" || !dobSafariPattern.test(dobValue)) {
            alert("Bitte kontrolliere, wie du deinen Geburtstag eingegeben hast. Falls du Safari benutzt, musst du JJJJ-MM-TT eingeben.");
            return false;
        }
    }
}