//<![CDATA[

    // Public constants
    var monthlist = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

    window.onload = function() {
        var greeting =  getTodaysDate() + ' - ' + getGreeting() + "!";
        $('#dateHeading').text(greeting);
        setCurrentQuarter();
    }


    function getTodaysDate() {
      var today = new Date();
      var day = today.getDay();
      var daylist = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];
      var minutes = today.getMinutes();
      minutes = (minutes < 10) ? '0' + minutes : minutes;
      return daylist[day] + ' ' + today.getDate() + ' ' + monthlist[today.getMonth()]
        + ' ' + today.getFullYear() + ' - ' + today.getHours() + ':' + minutes;
    }

    function getGreeting(){
        var data = [
                [0,   4, "Good Night"],
                [5,  11, "Good Morning"],
                [12, 16, "Good Afternoon"],
                [17, 20, "Good Evening"],
                [21, 23, "Good Night"]
            ],
        hr = new Date().getHours();

        for(var i = 0; i < data.length; i++){
            if(hr >= data[i][0] && hr <= data[i][1]){
                return (data[i][2]);
            }
        }
    }

    function setCurrentQuarter() {
        var currentQuarter = $("#currentQuarterId").val();
        var today = new Date();
        var year = today.getFullYear();
        var startMonth = 0;
        var endMonth   = 2;
        if (currentQuarter == 2) {
            startMonth = 3;
            endMonth   = 5;
        } else if (currentQuarter == 3) {
            startMonth = 6;
            endMonth   = 8;
        } else if (currentQuarter == 4) {
            startMonth = 9;
            endMonth   = 11;
        }
        var month = new Array(3);
        var pos = 0;
        for (i = startMonth; i <= endMonth; i++) {
            month[pos] = monthlist[i] + ' ' + year;
            pos++;
        }
        for (i = 0; i < 3; i++) {
           $("#month" + i).val(month[i]);
        }
    }

//]]>
