//<![CDATA[

    // Public constants
    var monthlist = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

    window.onload = function() {
        var greeting =  getTodaysDate() + ' - ' + getGreeting() + "!";
        $("#dateHeading").text(greeting);

        $("#gigStartDate").datepicker({
            dateFormat: "dd-mm-yy",
            showButtonPanel: true
        });
        //$("#gigStartDate").datepicker("setDate", "0");

        $("#gigEndDate").datepicker({
            dateFormat: "dd-mm-yy",
            showButtonPanel: true
        });
        //$("#gigEndDate").datepicker("setDate", "7");

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
        $("#findGigsButton1").show();
        $("#findGigsButton2").show();

        var revStartDate = reverseDate($("#gigStartDate").val());
        var revEndDate   = reverseDate($("#gigEndDate").val());
        var startDate    = Date.parse(revStartDate);
        var endDate      = Date.parse(revEndDate);

        if (startDate > endDate) {
            $("#findGigsButton1").hide();
            $("#findGigsButton2").hide();
            $("#modal-hdr-msg").html("Warning");
            $("#errorMsg").html("Start date cannot be greater than end date");
            $('#myModal').modal("show");
        }
    }

    function reverseDate(dt) {
        var dd = dt.substring(0,2);
        var mm = dt.substring(3,5);
        var yy = dt.substring(6);
        return yy + "-" + mm + "-" + dd;
    }

    function submitForm(href) {
        $("#currentPage").val(href.innerText);
        $("#gigDetailsForm").submit();
    }

//]]>
