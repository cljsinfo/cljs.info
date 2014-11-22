// this quick and dirty script fetches all of the "see-also" symbols from clojuredocs.org
// you might have to do "npm install request" in this directory before running

var fs = require('fs'),
  request = require('request'),
  symbols = require('./symbols.json');

var keys = [];

// add things to symbols
for (var i in symbols) {
  if (symbols.hasOwnProperty(i) !== true) continue;

  var fullName = i,
    ns = extractNamespace(fullName),
    name = extractName(fullName);

  if (ns === 'cljs.core') {
    ns = 'clojure.core';
  }

  symbols[i]["_uri_ns"] = encodeURIComponent(ns);
  symbols[i]["_uri_name"] = encodeURIComponent(name);

  keys.push(fullName);
}

var currentIndex = 0;

function requestSuccess(error, response, body) {
  var fullName = keys[ currentIndex ];
  if (error || response.statusCode !== 200) {
    console.log("Request failed! " + fullName);
    return;
  }

  var data = cleanData( JSON.parse(body) );

  symbols[fullName] = data;

  var status = "fetched " + fullName + ' (' + (currentIndex + 1) + '/' + keys.length + ')';
  console.log(status);
  fs.writeFileSync('see-also.json', JSON.stringify(symbols, null, 2));

  currentIndex++;

  if (keys[currentIndex]) {
    setTimeout(sendRequest, 250);
  }
}

function sendRequest() {
  var fullName = keys[currentIndex],
    nsUri = symbols[fullName]["_uri_ns"],
    nameUri = symbols[fullName]["_uri_name"],
    url = 'http://api.clojuredocs.org/see-also/' + nsUri + '/' + nameUri;

  request(url, requestSuccess);
}

// send the first request
sendRequest();










function extractNamespace(fullName) {
  fullName = fullName + "";
  var slash = fullName.indexOf("/");
  return fullName.substr(0, slash);
}

function extractName(fullName) {
  fullName = fullName + "";
  var slash = (fullName + "").indexOf("/");
  return fullName.substr(slash + 1);
}

// delete some properties we don't care about, etc
function cleanData(d) {
  if (! d) return d;

  for (var i = 0; i < d.length; i++) {
    delete d[i].added;
    delete d[i].created_at;
    delete d[i].line;
    delete d[i].updated_at;
    delete d[i].url;
    delete d[i].url_friendly_name;
    delete d[i].version;
  }

  return d;
}