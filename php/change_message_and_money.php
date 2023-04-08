<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$username=validate($_POST['name']);
	$money=(int)validate($_POST['money']);
	$message_money=validate($_POST['message_money']);
	$sql="UPDATE `profile` SET message_money='$message_money',pocket='$money' where account='$username';";
	$result = $conn->query($sql);
	$conn -> close();
?>