<?php
    $conn = mysqli_connect("localhost", "id4285570_klemenkac", "myPassw", "id4285570_prijavinapako");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$id = $_POST["id"];
$dom = $_POST["dom"];
$soba = $_POST["soba"];
$tip_napake = $_POST["tip_napake"];
$opis = $_POST["opis"];
$datum = $_POST["datum"];

$sql = "UPDATE napaka SET dom='$dom', soba='$soba', tip_napake='$tip_napake',opis='$opis', datum='$datum' WHERE id='$id'";

if ($conn->query($sql) === TRUE) {
    echo "Record updated successfully";
} else {
    echo "Error updating record: " . $conn->error;
}
$conn->close();
?>