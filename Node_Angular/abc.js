var express = require('express');
var app = express();
var cors = require('cors');
var https = require('https');
var obj = {};


app.use(cors());
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json())


app.post('/endpoint', function (req, res) {
    obj = JSON.stringify(req.body);
    console.log('body: ' + JSON.stringify(req.body));
    res.send(req.body);
});


app.listen(3000);