<?php
$submit = $_POST;
$lat = 0;
$log = 0;
$places_get = 0;
$places_get_pho = 0;
$places_get_rev = 0;
if ($submit) {
    $arrContextOptions = array(
        "ssl" => array(
            "verify_peer" => false,
            "verify_peer_name" => false,),);


    if (trim($_POST['placeid1']) == '') {
        $geolocKey = "AIzaSyBqcIlGrq28X7vTB0_5RegUuvZ6JwwOU8k";
        if (isset($_POST['from']) && $_POST['from'] == 'loc') {
            $string = str_replace(" ", "+", trim($_POST['location1']));

            $details = file_get_contents("https://maps.googleapis.com/maps/api/geocode/json?address=" . $string . "&key=" . $geolocKey, false, stream_context_create($arrContextOptions));
//            echo $details;
            $decphp = json_decode($details, true);
            if ($decphp['status'] == 'OK') {
                $geo = $decphp['results'][0]['geometry'];
                $lat = $geo['location']['lat'];
                $log = $geo['location']['lng'];
//                echo $lat;
//                echo $log;
            } else {
                echo "error geocoding";
            }


        }
        if (isset($_POST['from']) && $_POST['from'] == 'curr') {
            $lat = $_POST['lat'];
            $log = $_POST['long'];
        }
//    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=34.0223
//519,-118.285117&radius=16090&type=cafe&keyword=usc&key= YOUR_API_KEY

        if (isset($_POST['keyword']) && trim($_POST['keyword']) != '') {
            global $keyword;
            $keyword = str_replace(" ", "+", trim($_POST['keyword']));
        }

        if (isset($_POST['category'])) {
            global $category;
            $category = str_replace(" ", "_", trim($_POST['category']));
        }

        if (isset($_POST['distance'])) {
            global $distance;
            $distance = $_POST['distance'];
            if ($distance == '') {
                $distance = 10 * 1609.34;
            } else {
                $distance *= 1609.34;
            }
        }
        if ($distance > 50000) {
            $distance = 50000;
        }

        $placeskey = "AIzaSyBRWIU6rOkQFYOelBkXXhKUU9QnSd1P2lQ";
        $place = "";
        if ($lat != 0 && $log != 0 && $keyword != '') {
            if ($category == "default") {
                $place = file_get_contents("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" . $lat . "," . $log . "&keyword=" . $keyword . "&radius=" . $distance . "&key=" . $placeskey, false, stream_context_create($arrContextOptions));

            } else {
                $place = file_get_contents("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" . $lat . "," . $log . "&keyword=" . $keyword . "&radius=" . $distance . "&type=" . $category . "&key=" . $placeskey, false, stream_context_create($arrContextOptions));
            }
            $details_place = json_decode($place, true);
            if ($details_place['status'] == 'OK') {
                $places_get = 1;
//echo $place;

            } else {
                echo "error places";
            }
        }


    } else {
//https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ7aVxnOTHw
//oARxKIntFtakKo&key= YOUR_API_KEY
        $placeskey = "AIzaSyBRWIU6rOkQFYOelBkXXhKUU9QnSd1P2lQ";

        $place_id = trim($_POST['placeid1']);
        $revpho = file_get_contents("https://maps.googleapis.com/maps/api/place/details/json?placeid=" . $place_id . "&key=" . $placeskey, false, stream_context_create($arrContextOptions));
//echo $revpho;
        $revphodec = json_decode($revpho, true);
        if ($revphodec['status'] == "OK") {
            $result = $revphodec['result'];
            if (array_key_exists('reviews', $result)) {
                $places_get_rev = 1;
            }

            if (array_key_exists('photos', $result)) {
                $places_get_pho = 1;
                $photo = $result['photos'];
                $placeskey = "AIzaSyBRWIU6rOkQFYOelBkXXhKUU9QnSd1P2lQ";

                for ($i = 0; $i
                < sizeof($photo); $i++) {
                    if ($i == 5) {
                        break;
                    }
                    $photocode = file_get_contents("https://maps.googleapis.com/maps/api/place/photo?maxwidth=850&photoreference=" . $photo[$i]['photo_reference'] . "&key=" . $placeskey, false, stream_context_create($arrContextOptions));
                    $file = "photo" . $i . ".png";
                    file_put_contents($file, $photocode);

//                    echo $photocode;
                }
            } else {
//echo "no photos";
            }

        } else {
            echo "errorr placeID";
        }
    }
}


if (isset($_POST['clear']))
    $_POST = array();

?>

<html xmlns="http://www.w3.org/1999/html">
<head>
    <title></title>

    <script type="text/javascript">


        function clea() {
            document.getElementById("myForm").reset();
            document.id1.keyword.value = "";
            document.id1.category.value = "default";
            document.id1.distance.value = "";
            document.id1.location1.value = "";
            document.getElementById("curr").checked = true;
            document.id1.category.value = "default";
            document.getElementById("dynreq").disabled = true;
            document.getElementById("out").innerHTML = "";
            document.getElementById('map').style.height = 0;
            document.getElementById('map').style.width = 0;
            document.getElementById("mapDiv").innerHTML = "";

        }

        function jsfunction(place) {
            //console.log(place);
            //document.write(place);
            document.getElementById("placeid1").value = place;
            document.id1.submit();

        }

        lat11 = 0;
        lng22 = 0;


        function initMap(lat1, lng1) {
            lat11 = lat1;
            lng22 = lng1;


            var bodyRect = document.body.getBoundingClientRect();

            var x = (event.clientX) - (10);
            var y = event.clientY + (bodyRect.top * -1) + 18;
            console.log(x, y);


            document.getElementById('map').style.left = x - 10;
            document.getElementById('map').style.top = y + 5;
            document.getElementById('mapDiv').style.left = x + 4;
            document.getElementById('mapDiv').style.top = y + 15;


            //console.log(lat1+"   "+lng1);
            directionsService = new google.maps.DirectionsService();
            directionsDisplay = new google.maps.DirectionsRenderer();

            var uluru = {lat: lat1, lng: lng1};
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 14,
                center: uluru
            });
            var marker = new google.maps.Marker({
                position: uluru,
                map: map
            });
            directionsDisplay.setMap(map);


            google.maps.event.trigger(map, 'resize');
            return;
        }

        function calcRoute(tMode) {
            var start =
        <
                ? php echo
            '{lat:'.$lat.
            ',lng:'.$log.
            '}';
                ?
        >
            ;
            var end = {lat: lat11, lng: lng22};//lat11+""+lng22;
            var request = {
                origin: start,
                destination: end,
                travelMode: tMode
            };
            directionsService.route(request, function (result, status) {
                if (status == 'OK') {
                    directionsDisplay.setDirections(result);
                }
            });
        }


    </script>

    <script async defer
            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBqcIlGrq28X7vTB0_5RegUuvZ6JwwOU8k&callback=initMap">
    </script>

    <style type="text/css">

        .form1 table {
            margin: auto;
            padding: 0px;
            border: 1px solid;
        }

        .form1 {

            align-content: center;
            margin: auto;
        }

        .form1 form {
            padding: 0px;
        }

        #p1 {

            font-style: italic;
            font-weight: bold;
            font-size: 20px;

        }

        table {
            border-collapse: collapse;

        }

        a {
            text-decoration: none;
            color: black;
        }

        .button {
            background-color: #e7e7e7; /* Green */
            border: none;
            color: black;
            padding: 5px 15px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 11px;
        }

    </style>
</head>
<body onload="Get()">
<div id="map" style="height:0;width:0;position: absolute "></div>
<div id="mapDiv" style="position: absolute;height: 0;width=0;"></div>


<script type="text/javascript">

    function Get() {
        if (document.getElementById("curr").checked)
            document.getElementById("dynreq").disabled = true;
        //console.log("hai");
        var ht = new XMLHttpRequest();
        ht.open("GET", "http://ip-api.com/json", false);
        ht.send();
        var k = ht.responseText;
        loc = JSON.parse(k);
        document.getElementById("lat").value = loc.lat;
        document.getElementById("long").value = loc.lon;
        document.id1.search.disabled = false;
        return;
    }
</script>
<div class="form1">
    <form name="id1" id="myForm" method="post" action="place.php" align="center">
        <table>
            <tr>
                <th colspan="2"><p>Travel and Entertainment Search</p></th>
            </tr>
            <tr>
                <td colspan="2">
                    <hr/>
                </td>
            </tr>

            <tr>
                <td>Keyword <input type="text" required="required" pattern=".*[^ ].*" name="keyword" id="key1"
                                   value="<?php echo isset($_POST['keyword']) ? trim($_POST['keyword']) : '' ?>"></td>
                <td></td>
            </tr>
            <tr>
                <td>Category <select name="category">
                        <option selected="selected" value="default">default</option>
                        <option value="cafe">cafe</option>
                        <option value="bakery">bakery</option>
                        <option value=restaurant>restaurant</option>
                        <option value="beauty salon">beauty salon</option>
                        <option value="casino">casino</option>
                        <option value="movie theater">movie theater</option>
                        <option value="lodging">lodging</option>
                        <option value="airport">airport</option>
                        <option value="train station">train station</option>
                        <option value="subway station">subway station</option>
                        <option value="bus station">bus station</option>
                    </select></td>
                <td></td>
            </tr>
            <script type="text/javascript">
                document.id1.category.value = "<?php echo isset($_POST['category']) ? $_POST['category'] : 'default';?>";
            </script>


            <tr>
                <td>Distance(miles) <input name="distance" type="number" placeholder="10"
                                           value="<?php echo isset($_POST['distance']) ? trim($_POST['distance']) : '' ?>">
                </td>
                <td>from <input type="radio" val="def" value="curr" name="from" id="curr"<?php
                    if (!isset($_POST['form']))
                        echo 'checked="checked"';
                    if (isset($_POST['from']) && $_POST['from'] == 'curr')
                        echo 'checked="checked"'; ?>>Here
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <div id="dyn" style="">
                        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp <input type="radio" name="from" value="loc"
                                                                        id="loc" <?php if (isset($_POST['from']) && $_POST['from'] == 'loc') echo 'checked="checked"' ?>
                        >
                        <input name="location1" type="text" id="dynreq" pattern=".*[^ ].*" placeholder="location"
                               value="<?php echo isset($_POST['location1']) ? trim($_POST['location1']) : '' ?>"<?php echo isset($_POST['location1']) ? '' : 'disabled' ?>
                        >
                    </div>
                </td>
            </tr>


            <tr>
                <td>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                    <input type="hidden" value="" id="lat" name="lat">
                    <input type="hidden" value="" id="long" name="long">
                    <input type="hidden" id="placeid1" name="placeid1" value="">
                    <input type="submit" name="search" value="search" disabled onsubmit="submit();">
                    <input type="button" name="clear" value="clear" onclick="clea();">
                </td>
                <td></td>
            </tr>
        </table>

    </form>
</div>
<div id="out" style="text-align: center;">

</div>


<script type="text/javascript">
    document.id1.from[1].onchange = function () {
        myfun()
    };

    function myfun() {
        if (document.id1.from[1].checked) {
            document.id1.location1.disabled = false;
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
        }
    }


    <
        ? php
    if ($places_get == 1) {
        echo
        'var places='.json_encode($place).
        ';';
        echo
        'var k=placesFun();';
    }


        ?
    >


    function placesFun() {

        var obj = JSON.parse(places);
        var result = obj.results;
        var placeTable = "<table border='1' align='center' style='min-width: 500px'><tr><th>Category</th><th>Name</th><th>Address</th></tr>";
        var row = "";
        for (var i = 0; i < result.length; i++) {
            row += "<tr><td><img src=" + result[i].icon + "></td><td><a href='javascript:void(0);' id=" + i + ">" + result[i].name + "</a></td><td><a href='javascript:void(0);' id=" + 'A' + i + ">" + result[i].vicinity + "</a></td></tr>";

        }
        var placeTable1 = "</table>"
        document.getElementById("out").innerHTML = placeTable + row + placeTable1;


    <
            ? php
        if ($places_get == 1) {
            $local = $details_place['results'];
            for ($j = 0; $j < sizeof($local); $j++) {
                //document.write(result[j].place_id);

                echo
                'var k'.$j.
                '=result['.$j.
                '].place_id;';
                echo
                "\n";
                echo
                'document.getElementById("'.$j.
                '").onclick = function () {jsfunction(k'.$j.
                ');};';
                echo
                "\n";
            }
        }


            ?
    >

    <
            ? php
        if ($places_get == 1) {
            $local = $details_place['results'];
            for ($j = 0; $j < sizeof($local); $j++) {
                //document.write(result[j].place_id);

                echo
                'var kklat'.$j.
                '=result['.$j.
                '].geometry.location.lat;';
                echo
                'var kklng'.$j.
                '=result['.$j.
                '].geometry.location.lng;';
                echo
                "\n";
                echo
                'document.getElementById("A'.$j.
                '").onclick = function () {resize();initMap(kklat'.$j.
                ',kklng'.$j.
                ');};';
                echo
                "\n";
            }
        }


            ?
    >

        function resize() {
            if (parseInt(document.getElementById('map').style.height) == 0) {
                document.getElementById('map').style.height = 300;
                document.getElementById('map').style.width = 300;
                mapButtons(true);
            }
            else {
                document.getElementById('map').style.height = 0;
                document.getElementById('map').style.width = 0;
                mapButtons(false);
            }

            return;
        }


        return;


    }

    function mapButtons(bool) {
        if (bool) {
            document.getElementById("mapDiv").innerHTML = "<input type='button' id='bt1' class='button' value='Walk there'><br/><input id='bt2' type='button' class='button' value='Bike there '></br><input id='bt3' type='button' class='button' value='Drive there'>";
            console.log("map button");
            document.getElementById("bt1").onclick = function () {
                calcRoute("WALKING");
            };
            document.getElementById("bt2").onclick = function () {
                calcRoute("BICYCLING");
            };
            document.getElementById("bt3").onclick = function () {
                calcRoute("DRIVING");
            };
        }
        else {
            document.getElementById("mapDiv").innerHTML = "";
        }
        return;

    }


</script>

<script>

    <
        ? php
    if ($places_get_rev == 1 || $places_get_pho == 1) {
        echo
        'var reviews='.json_encode($revpho).
        ';';
        echo
        'var k=revphoFun(reviews);';
    }

        ?
    >

    function revphoFun(rev) {
        //console.log(rev);
        obj = JSON.parse(rev);
        var result = obj.result;
        var review = result.reviews;
        var name = result.name + "<br/><br/><br/><br/>";
        var showrev = "<p id='showrev'>click to show reviews</p>";
        var showpho = "<p id='showpho'>click to show photos</p>";

        var img11 = "<img id='img1'  src='http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png' width='40px' height='20px'><br/><br/>";
        var img22 = "<img id='img2' src='http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png' width='40px' height='20px'><br/><br/>";
        var src1 = "http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png";
        var src2 = "http://cs-server.usc.edu:45678/hw/hw6/images/arrow_up.png";

    <
            ? php
            $bool = 'false';
        if ($places_get_rev == 1)
            $bool = 'true';
        echo
        'if('.$bool.
        '){';
            ?
    >


        var rev = "<table border='1' align='center' width='600'>";


        for (var i = 0; i < review.length; i++) {
            if (i == 5) {
                break;
            }
            rev += "<tr align='center'><td><img width='30px' height='30px' src=" + review[i].profile_photo_url + ">" + review[i].author_name + "</td></tralign><tr><td>" + review[i].text + "</td></tr>";

        }
        rev += "</table>";
    <
            ? php echo
        "}else{ var rev=\"<table border='1' align='center' width='600'><tr align='center'><td>No Reviews Found.</td></tr></table>\";}";
            ?
    >





    <
            ? php
            $bool = 'false';
        if ($places_get_pho == 1)
            $bool = 'true';
        echo
        'if('.$bool.
        '){';
            ?
    >

        var photo = "<table border='1' align='center' width='600'><tr><td><a href='photo0.png'target='_blank'><img src='photo0.png'></a></td></tr><tr><td><a href='photo1.png'target='_blank'><img src='photo1.png'></a></td></tr><tr><td><a href='photo2.png'target='_blank'><img src='photo2.png'></a></td></tr><tr><td><a href='photo3.png'target='_blank'><img src='photo3.png'></a></td></tr><tr><td><a href='photo4.png'target='_blank'><img src='photo4.png'</a></td></tr></table>";


    <
            ? php echo
        "}else{ var photo=\"<table border='1' align='center' width='600'><tr align='center'><td>No Photos Found.</td></tr></table>\";}";
            ?
    >


        var div1 = "<div id='revdiv' style='text-align: center;'></div>";
        var div2 = "<div id='imgdiv' style='text-align: center;'></div>";


        document.getElementById("out").innerHTML = name + showrev + img11 + div1 + showpho + img22 + div2;


        document.getElementById("img1").onclick = function acd() {
            if (document.getElementById("img1").src == src1) {
                document.getElementById("showrev").innerHTML = "click to hide reviews";
                document.getElementById("img1").src = src2;
                document.getElementById("revdiv").innerHTML = rev;
                document.getElementById("imgdiv").innerHTML = "";
                document.getElementById("img2").src = src1;
                document.getElementById("showpho").innerHTML = "click to show images";


            }
            else {
                document.getElementById("img1").src = src1;
                document.getElementById("showrev").innerHTML = "click to show reviews";

                document.getElementById("revdiv").innerHTML = "";
            }

        };


        document.getElementById("img2").onclick = function aed() {
            if (document.getElementById("img2").src == src1) {
                document.getElementById("img2").src = src2;
                document.getElementById("showpho").innerHTML = "click to hide images";
                document.getElementById("imgdiv").innerHTML = photo;
                document.getElementById("revdiv").innerHTML = "";
                document.getElementById("showrev").innerHTML = "click to show reviews";
                document.getElementById("img1").src = src1;

            }
            else {
                document.getElementById("img2").src = src1;
                document.getElementById("showpho").innerHTML = "click to show reviews";
                document.getElementById("imgdiv").innerHTML = "";

            }

        };


    }


</script>


</body>
</html>