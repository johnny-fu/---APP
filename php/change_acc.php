<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$account=validate($_POST['account']);
	$password=validate($_POST['password']);
	$name=validate($_POST['name']);
	$sql="UPDATE `profile` SET password = '$password', name = '$name',first_login = '1',message_money = '0' where account='$account';";
	$result = $conn->query($sql);
	$conn -> close();
?>