<?php
    $con = mysqli_connect("localhost", "id4285570_klemenkac", "myPassw", "id4285570_prijavinapako");
    
    $e_naslov = $_POST["e_naslov"];
    $sifra = $_POST["sifra"];
    $geslo = $_POST["geslo"];

    $statement = mysqli_prepare($con, "INSERT INTO user (id, e_naslov, sifra, geslo) VALUES (NULL, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sis", $e_naslov, $sifra, $geslo);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>