<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$account=validate($_POST['account']);
	$key=validate($_POST['key']);
	$sql="UPDATE `profile` SET forget_password ='$key' where account='$account';";
	$result = $conn->query($sql);
	$conn -> close();
?>