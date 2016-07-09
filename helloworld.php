<?php
 require '../vendor/autoload.php';
 require 'routes/db.php';
 require 'routes/movies.php';

 $app = new \Slim\App;

 $app->get(
    '/',
    function(){
        echo "<h1>Movie Homepage!!</h1>";
    }
 );

 //get method to access delete movie functionality.
 $app->get(
    '/delete',
    function(){
        echo "<h1>Android Programming</h1><br>";
        echo "<h1>Delete Movie</h1><br>";
        echo "<form action=\"http://rohitshivsharma.com/delete\" method=\"post\">";
        echo "ID: <input type=\"text\" name=\"id\"><br><br>";
        echo "<input type=\"submit\" value=\"Submit\">";
        echo "</form>";
        }
 );

//get method to access add movie functionality.
 $app->get(
    '/add',
    function(){
        echo "<h1>Android Programming</h1><br>";
        echo "<h1>Add Movie</h1><br>";
        echo "<form action=\"http://rohitshivsharma.com/add\" method=\"post\">";
        echo "PrimaryID: <input type=\"text\" name=\"primaryID\"><br><br>";
        echo "ID: <input type=\"text\" name=\"id\"><br><br>";
        echo "Name: <input type=\"text\" name=\"name\"><br><br>";
        echo "Description: <input type=\"text\" name=\"description\"><br><br>";
        echo "Stars: <input type=\"text\" name=\"stars\"><br><br>";
        echo "Length: <input type=\"text\" name=\"length\"><br><br>";
        echo "Image: <input type=\"text\" name=\"image\"><br><br>";
        echo "Year: <input type=\"text\" name=\"year\"><br><br>";
        echo "Rating: <input type=\"text\" name=\"rating\"><br><br>";
        echo "Director: <input type=\"text\" name=\"director\"><br><br>";
        echo "Url: <input type=\"text\" name=\"url\"><br><br>";
        echo "<input type=\"submit\" value=\"Submit\">";
        echo "</form>";
        }
 );

//get method to fetch all the movies in json format.
 $app->get(
 	'/movies/',
 	function(){
		getMovies();
 	}
 );

//get method to fetch a movie by it's id in json format.
$app->get(
	'/movies/id/{mid}',
	function($request, $response, $args){
		getMovieDetails($args['mid']);
	}
);

//get method to fetch all the movies whose rating are greater than the input rating.
$app->get(
	'/movies/rating/{mrating}',
	function($request, $response, $args){
		getMovieRating($args['mrating']);
	}
);

//post method to delete the movie from database based on the provided id.
$app->post(
	'/delete',
	function($request, $response, $args){
		$movieData = json_decode($request->getBody(),true);
		deleteMovie($movieData['id']);
});

//post method to add a movie in database based on the provided data.
$app->post(
	'/add',
	function($request, $response, $args){
		$data = json_decode($request->getBody(),true);
		addMovie($data);
});
 $app->run();
?>