<?php
	require_once "conn.php";
	require_once "vaildate.php";
	
	$username=validate($_POST['name']);
	$money=validate($_POST['money']);
	
	$sql="UPDATE `profile` SET pocket='$money' where account='$username';";
	$result = $conn->query($sql);
	if ($result === TRUE) {
	echo "New record created successfully";
	} else {
	echo "Error!";
	}
	
	$conn -> close();
?>