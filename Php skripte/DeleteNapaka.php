<?php
    $conn = mysqli_connect("localhost", "id4285570_klemenkac", "myPassw", "id4285570_prijavinapako");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$id = $_POST["id"];

$sql = "DELETE FROM napaka WHERE id='$id'";

if ($conn->query($sql) === TRUE) {
    echo "Record updated successfully";
} else {
    echo "Error updating record: " . $conn->error;
}
$conn->close();
?>