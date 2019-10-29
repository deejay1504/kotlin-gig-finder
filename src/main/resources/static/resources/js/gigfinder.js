//<![CDATA[

    // Public constants
    var monthlist = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

    window.onload = function() {
        var greeting =  getTodaysDate() + ' - ' + getGreeting() + "!";
        $("#dateHeading").text(greeting);

        $("#startDate").datepicker({
            dateFormat: "dd-mm-yy"
        });
        $("#startDate").datepicker("setDate", "0");

        $("#endDate").datepicker({
            dateFormat: "dd-mm-yy",
        });
        $("#endDate").datepicker("setDate", "7");

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

    function getGreeting() {
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

    function validateDate() {
        var startDate = newDate($("#startDate").val());
        var endDate   = newDate($("#endDate").val());
        if (startdate < endDate) {
            alert('invalid');
        }
    }

//]]>
