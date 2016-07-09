<?php
function getMovies()
{
    $conn = getDB(); //get DB connection.
    //$sql  = "SELECT * FROM Movies ORDER BY name ASC";
    //prepare query.
    if (!$result = $conn->prepare("SELECT * FROM Movies ORDER BY name ASC")) {
        die("Connection failed");
    }
    
    //execute query
    $result->execute();
    $res        = $result->get_result();
    $return_arr = array();
    while ($row = $res->fetch_assoc()) {
        $row_array['id']          = $row['id'];
        $row_array['name']        = $row['name'];
        $row_array['description'] = $row['description'];
        $row_array['rating']      = $row['rating'];
        $row_array['url']         = $row['url'];
        
        array_push($return_arr, $row_array);
    }
    echo json_encode($return_arr);
    $conn->close();
}

function getMovieDetails($mid)
{
    $conn = getDB();//get DB connection.
    //create prepare statement
    if (!$result = $conn->prepare("SELECT * FROM Movies WHERE id = ?")) {
        die("Connection failed");
    }
    
    //bind param with query
    $result->bind_param("s", $mid);
    //execute query
    $result->execute();
    //get result
    $res        = $result->get_result();
    $return_arr = array();
    
    while ($row = $res->fetch_assoc()) {
        $row_array['id']          = $row['id'];
        $row_array['name']        = $row['name'];
        $row_array['description'] = $row['description'];
        $row_array['year']        = $row['year'];
        $row_array['length']      = $row['length'];
        $row_array['stars']       = $row['stars'];
        $row_array['director']    = $row['director'];
        $row_array['rating']      = $row['rating'];
        $row_array['url']         = $row['url'];
        
        array_push($return_arr, $row_array);
    }
    echo json_encode($return_arr);
    
    $conn->close();
}

function getMovieRating($mrating)
{
    $conn = getDB();//get DB connection.
    //create prepare statement
    if (!$result = $conn->prepare("SELECT * FROM Movies WHERE rating >= ? ORDER BY name ASC")) {
        die("Connection failed");
    }
    
    //bind param with query
    $result->bind_param("s", $mrating);
    //execute query
    $result->execute();
    //get result
    $res        = $result->get_result();
    $return_arr = array();
    
    while ($row = $res->fetch_assoc()) {
        $row_array['id']          = $row['id'];
        $row_array['rating']      = $row['rating'];
        $row_array['name']        = $row['name'];
        $row_array['description'] = $row['description'];
        $row_array['url']         = $row['url'];
        array_push($return_arr, $row_array);
    }
    echo json_encode($return_arr);
    
    $conn->close();
}

function deleteMovie($mid)
{
    $conn = getDB();//get DB connection.
    //create prepare statement
    if (!$result = $conn->prepare("DELETE FROM Movies WHERE id = ?")) {
        die("Connection failed");
    }
    
    //bind param with query
    $result->bind_param("s", $mid);
    //execute query
    if ($result->execute()) {
        $return_arr = "Success";
    } else {
        $return_arr = "Failure";
    }
    
    echo json_encode($return_arr);
    
    $conn->close();
}

function addMovie($data)
{
    $conn = getDB();//get DB connection.
    //create prepare statement
    if (!$result = $conn->prepare("INSERT INTO Movies VALUES (?,?,?,?,?,?,?,?,?,?,?)")) {
        die("Connection failed" . $conn->connect_error);
    }
    //bind param
    $result->bind_param("sssssssssss", $data['primaryID'], $data['id'], $data['name'], $data['description'], $data['stars'], $data['length'], $data['image'], $data['year'], $data['rating'], $data['director'], $data['url']);
    //execute query
    if ($result->execute()) {
        $return_arr = "Movie added.";
    } else {
        $return_arr = "Movie could not be added.";
    }
    echo json_encode($return_arr);
    
    $conn->close();
}
?>