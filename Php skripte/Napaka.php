<?php
    $con = mysqli_connect("localhost", "id4285570_klemenkac", "myPassw", "id4285570_prijavinapako");
    
    $id = $_POST["id"];
    $dom = $_POST["dom"];
    $soba = $_POST["soba"];
    $tip_napake = $_POST["tip_napake"];
    $opis = $_POST["opis"];
    $user = $_POST["user"];
    $slika = $_POST["slika"];
    $datum = $_POST["datum"];
	$x = $_POST["x"];
	$y = $_POST["y"];
	$koncano= $_POST["koncano"];
	

    $statement = mysqli_prepare($con, "INSERT INTO napaka (id, dom, soba, tip_napake, opis, user, slika, datum, x, y, koncano) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sssssssssss", $id, $dom, $soba, $tip_napake, $opis, $user, $slika, $datum, $x, $y, $koncano);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>