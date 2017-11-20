<?php
    $con = mysqli_connect("localhost", "id1701907_klemenkac", "myPassw", "id1701907_prijavinapako");
    
    $id = $_POST["id"];
    $dom = $_POST["dom"];
    $soba = $_POST["soba"];
    $tip_napake = $_POST["tip_napake"];
    $opis = $_POST["opis"];
    $user = $_POST["user"];
    $slika = $_POST["slika"];
    $datum = $_POST["datum"];

    $statement = mysqli_prepare($con, "INSERT INTO napaka (id, dom, soba, tip_napake, opis, user, slika, datum) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssssssss", $id, $dom, $soba, $tip_napake, $opis, $user, $slika, $datum);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>