<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$username=validate($_POST['name']);
	$sql="UPDATE `profile` SET message_transaction = NULL where account='$username';";
	$result = $conn->query($sql);
	$conn -> close();
?>