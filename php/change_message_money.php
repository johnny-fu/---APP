<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$username=validate($_POST['name']);
	$password=validate($_POST['pass']);
	$money=(int)validate($_POST['money']);
	$sql="UPDATE `profile` SET message_money='$money' where account='$username' and password='$password'";
	$result = $conn->query($sql);
	
	
	$conn -> close();

?>