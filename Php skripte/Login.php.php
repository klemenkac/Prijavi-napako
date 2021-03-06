<?php
    $con = mysqli_connect("localhost", "id4285570_klemenkac", "myPassw", "id4285570_prijavinapako");
    
    $e_naslov = $_POST["e_naslov"];
    $geslo = $_POST["geslo"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE e_naslov = ? AND geslo = ?");
    mysqli_stmt_bind_param($statement, "ss", $e_naslov, $geslo);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $e_naslov, $sifra, $geslo);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["sifra"] = $sifra;
        $response["e_naslov"] = $e_naslov;
        $response["geslo"] = $geslo;
    }
    
    echo json_encode($response);
?>