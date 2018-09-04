var express = require('express');
var app = express();
var cors = require('cors');
var https = require('https');
var url = require('url');

obj = {};
lat = 0;
lng = 0;
latdest = 0;
lngdest = 0;


app.use(cors());
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());


app.get('/endpoint', function (req, res) {
    obj = req.query;
    var abc;
    console.log('body: ' + JSON.stringify(req.query));
    res.setHeader('HELLO', 'hello');


    if (obj['currloc'] == 'customloc' && obj['location1'] != "") {
        console.log(obj['currloc']);
        console.log("hello");
        var geolockey = "AIzaSyBqcIlGrq28X7vTB0_5RegUuvZ6JwwOU8k";
        var str = obj.location1.replace(/ /g, "+");
        var url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + str + "&key=" + geolockey;
        console.log(url);

        https.get(url, function (response) {
            var body = '';
            response.on('data', function (chunk) {
                body += chunk;
            });

            response.on('end', function () {
                console.log(body);
                var places = JSON.parse(body);
                if (places.status == 'OK') {
                    lat = places.results[0].geometry.location.lat;
                    latdest = places.results[0].geometry.location.lat;
                    lngdest = places.results[0].geometry.location.lng;
                    lng = places.results[0].geometry.location.lng;
                }
                //function call
                var placeskey = "AIzaSyBRWIU6rOkQFYOelBkXXhKUU9QnSd1P2lQ";
                var keyword = obj.keyword.replace(/ /g, "+");
                if (obj.distance == "undefined") {
                    distance = 10 * 1609.34;
                }
                else {
                    distance = obj.distance * 1609.34;
                }
                var category = obj['category'];
                var url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&keyword=" + keyword + "&radius=" + distance + "&type=" + category + "&key=" + placeskey;


                https.get(url, function (response) {
                    var body = '';
                    response.on('data', function (chunk) {
                        body += chunk;
                    });

                    response.on('end', function () {

                        abc = body;
                        console.log(body);
                        //return body;

                    });
                }).on('error', function (e) {
                    console.log("Got error: " + e.message);
                });


                console.log(url);

            });
        }).on('error', function (e) {
            console.log("Got error: " + e.message);
        });

    }
    else {

        //abc=getplaces(obj,obj["lat"],obj["long"]);

        var placeskey = "AIzaSyBRWIU6rOkQFYOelBkXXhKUU9QnSd1P2lQ";
        var keyword = obj.keyword.replace(/ /g, "+");
        if (obj.distance == "undefined") {
            distance = 10 * 1609.34;
        }
        else {
            distance = obj.distance * 1609.34;
        }
        var category = obj.category;
        var url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + obj.lat + "," + obj.long + "&keyword=" + keyword + "&radius=" + distance + "&type=" + category + "&key=" + placeskey;


        https.get(url, function (response) {
            var body = '';
            response.on('data', function (chunk) {
                body += chunk;
            });

            response.on('end', function () {
                abc = body;
                console.log(body);
                // return body;


            });
        }).on('error', function (e) {
            console.log("Got error: " + e.message);
        });


        console.log(url);


    }


    while (abc === undefined) {
        require('deasync').sleep(1000);

        console.log(abc);
    }

    var ret = JSON.parse(abc);
    ret.latdet = latdest;
    ret.lngdet = lngdest;
    var ret2 = JSON.stringify(ret);


    res.send(ret);
});


app.get('/nextpoint', function (req, res) {
    obj = req.query;
    var abc1;
    console.log('body: ' + JSON.stringify(req.query));
    res.setHeader('HELLO', 'hello');
    var placeskey = "AIzaSyBRWIU6rOkQFYOelBkXXhKUU9QnSd1P2lQ";


    var url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + obj.nextpage + "&key=" + placeskey;
    console.log(url);

    https.get(url, function (response) {
        var body = '';
        response.on('data', function (chunk) {
            body += chunk;
        });

        response.on('end', function () {
            console.log(body);
            abc1 = body;
        });
    }).on('error', function (e) {
        console.log("Got error: " + e.message);
    });


    while (abc1 === undefined) {
        require('deasync').sleep(1000);

        console.log(abc1);
    }


    res.send(abc1);
});


app.listen(3000);


function getplaces(obj, lat, lng) {

    var placeskey = "AIzaSyBRWIU6rOkQFYOelBkXXhKUU9QnSd1P2lQ";
    var keyword = obj['keyword'].replace(/ /g, "+");
    distance = 0;
    if (obj['distance'] == "undefined") {
        distance = 10 * 1609.34;
    }
    else {
        distance *= 1609.34
    }
    var category = obj['category'];
    var url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&keyword=" + keyword + "&radius=" + distance + "&type=" + category + "&key=" + placeskey;


    https.get(url, function (response) {
        var body = '';
        response.on('data', function (chunk) {
            body += chunk;
        });

        response.on('end', function () {
            console.log(body);
            return body;

        });
    }).on('error', function (e) {
        console.log("Got error: " + e.message);
    });


    console.log(url);

}


app.get('/yelppoint', function (req, res) {
    var k1 = ""
    temp = req.query;
    console.log(temp)
    var request = require('request');

// Set the headers
    var headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': 'Bearer 3N08yYkKIoHIAqHhMiL1ySvb3OZAN2Owe_2r8zZ14cx7XnNtAC8mavc31ISu04ee8osoWZjdk8nkUksmCLOMtVpeSE4qTua7kmc1s9dLkBOIHRMeRmJ-PDCnUlm9WnYx'
    }

// Configure the request
    var options = {
        url: 'https://api.yelp.com/v3/businesses/matches/best',
        method: 'GET',
        headers: headers,
        qs: {
            'name': temp.name,
            'address1': temp.address1,
            'address2': temp.address2,
            'city': temp.city,
            'state': temp.state,
            'country': temp.country,
            'phone': temp.phone,
            'postal_code': temp.zipcode
        }
    }

// Start the request
    var k;
    request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                // Print out the response body
                k = JSON.parse(body);
                // console.log(k.businesses)
                if (k.businesses.length != 0) {


                    var options1 = {
                        url: "https://api.yelp.com/v3/businesses/" + k.businesses[0].id + "/reviews",
                        method: 'GET',
                        headers: headers
                    }

                    request(options1, function (error1, response1, body1) {
                        if (!error1 && response1.statusCode == 200) {
                            // Print out the response body
                            k1 = JSON.parse(body1);
                            console.log(k1)

                        }
                    });

                }
                else {
                    k1 = "not found";
                }

                while (k1 === undefined) {
                    require('deasync').sleep(1000);

                    console.log(k1);
                }


            }
            else {
                console.log(response);
            }
        }
    );


    while (k1 === "") {
        require('deasync').sleep(1000);

        console.log(k1);
    }


    res.send(k1);

});




