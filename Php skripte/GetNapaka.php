<?php
    $conn = mysqli_connect("localhost", "id1701907_klemenkac", "myPassw", "id1701907_prijavinapako");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM napaka";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $flag[]=$row;
    }
    print(json_encode($flag));
}

$conn->close();
?>