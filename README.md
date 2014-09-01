This project contains two apps:

1. HuntWriter -  this is a convenient NFC tag writer app that consumes the same data as the app. You can use any tag writer app you want, but this makes it easy to get your tags right for the app. NOTE: by default this is going to lock your tags as READ ONLY!

2. SimpleIOHunt - this is the app that is used for the scavenger hunt. It can read NFC tags and will display the clues and trivia questions.

Setup
========

* You need writable NFC tags. I got mine from http://tagsfordroid.com/store.html
* Add the google play services lib as a dependency to the SimpleIOHunt app
* To use Artisan and Artisan Push, update your Artisan APP ID and google cloud messaging sender id in the ArtisanInstrumentedApplication class.
* Update huntdata/sampleHunt.json with your clues and trivia questions and add new pictures into that directory--landscape is better for the pictures.

Origin
========

This is a fork of the Google I/O 2014 Hunt app
https://code.google.com/p/google-io-hunt/

Docs for the original apps can be found at:
http://nfchunt.appspot.com/static/README.html
