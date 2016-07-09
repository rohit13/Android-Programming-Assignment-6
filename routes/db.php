<?php
function getDB()
{
    $dbhost = "localhost";
    $dbuser = "root";
    $dbpass = "";
    $dbname = "androidmoviedb";
    
    //create db connection
    $conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname);
    
    if ($conn->connect_error) {
        die("Connection failed");
    }
    return $conn;
}
?>