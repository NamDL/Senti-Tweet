<?php
require_once('TwitterAPIExchange.php');

$settings = array(
    'oauth_access_token' => "2427523519-xCT9c3JcMdMXE47uj2pk8ZIlPloGH6Lh2xrzWvn",
    'oauth_access_token_secret' => "QvERgvGnQF4CaSC1wmdgpYADSPgQiDdI2soxm0ZAGeXDo",
    'consumer_key' => "fKhxaXiASOwMvTpTt7YNuah6A",
    'consumer_secret' => "8ot9Q63yNeA8obgrDl9xkc5mkcCshav9tPioB9b9rolLdjP9Vt"
);

$url = 'https://api.twitter.com/1.1/search/tweets.json';
$requestMethod = 'GET'; 
$getfield = "?q=%23".$_GET['hash']."&src=typd";

// Perform the request
$twitter = new TwitterAPIExchange($settings);
echo $twitter->setGetfield($getfield)
             ->buildOauth($url, $requestMethod)
             ->performRequest();
?>






