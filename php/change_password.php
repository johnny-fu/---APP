<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$account=validate($_POST['account']);
	$password=validate($_POST['password']);
	$sql="UPDATE `profile` SET password ='$password' where account='$account';";
	$result = $conn->query($sql);
	$conn -> close();
?>