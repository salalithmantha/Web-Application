$('#keyword').focusout(function () {

    if (this.value.trim() == "") {
        document.getElementById("submit1").disabled = true;

        $('#keywordR').html("Please enter a keyword");

        $('#keyword').css("border-color", "red");
    }
    else {
        $('#keywordR').html("");
        $('#keyword').css("border-color", "white");

        if (document.id1.location1.disabled == false) {
            var val = document.getElementById("location1").value;
            if (val.trim() != "") {
                if (search == 1)

                    document.getElementById("submit1").disabled = false;
            }
        }
        else {
            if (search == 1)

                document.getElementById("submit1").disabled = false;
            // console.log("hello");

        }

    }


});


function keywordfun() {
    var val = document.getElementById("keyword").value;
    if (val.trim() == "") {
        document.getElementById("submit1").disabled = true;

        //       $('#keywordR').html("Please enter a keyword");

        //         $('#keyword').css("border-color", "red");
    }
    else {
        $('#keywordR').html("");
        $('#keyword').css("border-color", "white");

        if (document.id1.location1.disabled == false) {
            var val = document.getElementById("location1").value;
            if (val.trim() != "") {
                if (search == 1)

                    document.getElementById("submit1").disabled = false;
            }
        }
        else {
            if (search == 1)

                document.getElementById("submit1").disabled = false;
            // console.log("hello");

        }

    }

}


$('#location1').focusout(function () {

    if (this.value.trim() == "") {
        // console.log("hai");
        document.getElementById("submit1").disabled = true;

        $('#location1R').html("Please enter a location");

        $('#location1').css("border-color", "red");
    }
    else {
        $('#location1R').html("");
        $('#location1').css("border-color", "lightgray");
        var val = document.getElementById("keyword").value;
        if (val.trim() != "") {
            // console.log("hello");
            if (search == 1)

                document.getElementById("submit1").disabled = false;
        }


    }

});

function locationfun() {
    var val = document.getElementById("location1").value;
    if (val.trim() == "") {
        // console.log("hai");
        document.getElementById("submit1").disabled = true;

        // $('#location1R').html("Please enter a location");

        // $('#location1').css("border-color", "red");
    }
    else {
        $('#location1R').html("");
        $('#location1').css("border-color", "lightgray");
        var val = document.getElementById("keyword").value;
        if (val.trim() != "") {
            // console.log("hello");
            if (search == 1)

                document.getElementById("submit1").disabled = false;
        }


    }
}


document.id1.from[1].onchange = function () {
    myfun()
};

function myfun() {
    if (document.id1.from[1].checked) {
        document.id1.location1.disabled = false;
        var val = document.getElementById("location1").value;
        if (val.trim() == "") {
            document.getElementById("submit1").disabled = true;
        }
        else {
            var val1 = document.getElementById("keyword").value;
            if (val1.trim() != "") {

                if (search == 1)

                    document.getElementById("submit1").disabled = false;
            }
        }
        document.id1.location1.required = "required";
    }
    else {
        document.id1.location1.disabled = true;
        document.id1.location1.required = "";
    }
}

document.id1.from[0].onchange = function () {
    myfun1()
};

function myfun1() {
    if (document.id1.from[0].checked) {
        document.id1.location1.disabled = true;
        document.id1.location1.required = "";
        $('#location1').css("border-color", "WHITE");
        $('#location1R').html("");
        var val = document.getElementById("keyword").value;
        if (val.trim() != "") {
            if (search == 1)

                document.getElementById("submit1").disabled = false;
        }


    }
}


angular.module('myform', [])
    .controller('formController', ['$scope', '$http', function ($scope, $http) {
        $scope.category = "default";
        $scope.currloc = "currloc";
        $scope.next = "";

        $scope.clear1 = function () {
            console.log("clear");
            latdest = 0;
            lngdest = 0;
            latorg = 0;
            lngorg = 0;
            Get();
            document.getElementById("nextbt").style.display = "none";
            document.getElementById("prevbt").style.display = "none";
            document.getElementById("id1").reset();
            /*$scope.keyword = "";
            $scope.distance = "";
            $scope.category = "default";
            $scope.currloc = "currloc";
            $scope.location1 = "";
            */
            $scope.location1 = "";

            document.getElementById("currloc").checked = true;
            document.getElementById("customloc").checked = false;
            //document.getElementById()
            document.getElementById('location1').disabled = true;
            document.getElementById("table").innerHTML = "";
            document.getElementById("detailsplace").innerHTML = "";
            $("#clearTabres").click();
            $("#clearinfotab").click();
            $("#prevbt").css("display", "none");
            $("#nextbt").css("display", "none");
            $("#list").click();
            document.getElementById("details").disabled = true;


        };


        $scope.next1 = function () {
            document.getElementById("progress").innerHTML = "    <div class='progress' ><div class='progress-bar progress-bar-info progress-bar-striped active' role='progressbar' aria-valuenow='50' aria-valuemin='0' aria-valuemax='100' style='width:50%'></div></div>";


            var page;
            if (pages.length == 0) {
                pages.push(Object.assign({}, $scope.PostDataResponse));
                page = $scope.PostDataResponse.next_page_token;

            }
            else {
                var a = $scope.next.next_page_token + "";
                pages.push(Object.assign({}, $scope.next));
                page = $scope.next.next_page_token;
            }


            $http({
                method: 'get',
                url: 'https://salalithtravelandenter.appspot.com/nextpoint?nextpage=' + page,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                }// ,
                // data: {
                //     nextpage: page
                // }
            }).then(function (response) {
                $scope.next = response.data;
                tableprint($scope.next);
                console.log($scope.next);
            }), function (response) {
                $scope.ResponseDetails = response.status;

            };


        };


        $scope.prev1 = function () {

            var page = "";
            if (pages.length == 0) {
                //pages.pop();
                $scope.submit();

            }
            else {

                $scope.next = pages.pop();
                tableprint($scope.next);


            }


        };


        $scope.yelprev1 = function () {

            var data = {
                keyword: $scope.keyword,
                distance: $scope.distance,
                category: $scope.category,
                location1: $scope.location1,
                currloc: $scope.currloc
            };


            var config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                }
            }
            console.log(placeobj);
            var addr = placeobj.formatted_address.split(",");
            var addrlen = addr.length;
            var comp = placeobj.address_components;
            var country = "";
            var state = "";
            var city = "";
            var zipcode = 0;
            var phone = placeobj.international_phone_number.replace(/ /g, "");
            phone = phone.replace(/-/g, "");
            var phone1 = phone.replace(/\+/g, '');
            console.log(phone1);
            phone = "+" + phone;
            for (var i = 0; i < comp.length; i++) {
                for (var j = 0; j < comp[i].types.length; j++) {
                    if (comp[i].types[j] == "administrative_area_level_1") {
                        state = comp[i].short_name;
                    }
                    if (comp[i].types[j] == "locality") {
                        city = comp[i].short_name;
                    }
                    if (comp[i].types[j] == "postal_code") {
                        zipcode = comp[i].short_name;
                    }

                    if (comp[i].types[j] == "country")
                        country = comp[i].short_name;
                }
            }

            var q = "?name=" + placeobj.name + "&address1=" + addr[0] + "&address2=" + addr[1] + "&city=" + city + "&state=" + state + "&country=" + country + "&zipcode=" + zipcode + "&phone=" + phone1;
            //var res=encodeURI(q);
            $http({
                method: 'get',
                url: 'https://salalithtravelandenter.appspot.com/yelppoint' + q,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                }

            }).then(function (response) {
                $scope.yelp = response.data;
                //tableprint($scope.PostDataResponse);
                console.log($scope.yelp);
                yelprev = $scope.yelp;
            }), function (response) {
                $scope.ResponseDetails = response.status;

            };


            console.log("pressed me");

        };


        $scope.submit = function () {

            document.getElementById("progress").innerHTML = "    <div class='progress' ><div class='progress-bar progress-bar-info progress-bar-striped active' role='progressbar' aria-valuenow='50' aria-valuemin='0' aria-valuemax='100' style='width:50%'></div></div>";
            var data = {
                keyword: $scope.keyword,
                distance: $scope.distance,
                category: $scope.category,
                location1: $scope.location1,
                currloc: $scope.currloc
            };


            var config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                }
            }

            /*$http.post('https://salalithtravelandenter.appspot.com/endpoint',data,config)
                .then(function (response) {
                    $scope.PostDataResponse = response.data;
                    console.log($scope.PostDataResponse);
                }),function (response) {
                    $scope.ResponseDetails = response.status;
                };*/
            var q = "?keyword=" + $scope.keyword + "&category=" + $scope.category + "&distance=" + $scope.distance + "&currloc=" + $scope.currloc + "&location1=" + $scope.location1 + "&lat=" + loc.lat + "&long=" + loc.lon;
            //var res=encodeURI(q);
            $http({
                method: 'get',
                url: 'https://salalithtravelandenter.appspot.com/endpoint' + q,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                }
                // data: {
                //     keyword: $scope.keyword,
                //     category: $scope.category,
                //     distance: $scope.distance,
                //     currloc: $scope.currloc,
                //     location1: $scope.location1,
                //     lat: loc.lat,
                //     long: loc.lon
                // }
            }).then(function (response) {
                $scope.PostDataResponse = response.data;
                tableprint($scope.PostDataResponse);
                console.log($scope.PostDataResponse);
            }), function (response) {
                $scope.ResponseDetails = response.status;

            };


        };


    }]);

function addCode(code) {
    var JS = document.createElement('script');
    JS.text = code;
    document.body.appendChild(JS);
}



