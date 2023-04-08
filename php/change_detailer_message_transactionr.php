<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$client=validate($_POST['client']);
	$id=validate($_POST['id']);
	$judge=validate($_POST['judge']);
	if($judge=='1'){
	$sql="UPDATE `profile` SET message_transaction='OK' where account='$client';";
	$sql2="UPDATE `transaction` SET retailer='OK' where id_transaction='$id';";
	}
	else if($judge=='0'){
	$sql="UPDATE `profile` SET message_transaction='NO' where account='$client';";
	$sql2="UPDATE `transaction` SET retailer='NO' where id_transaction='$id';";
	}
	else{
	$sql="UPDATE `profile` SET message_transaction=null where account='$client';";
	$sql2="UPDATE `transaction` SET retailer='' where id_transaction='$id';";
	}
	$conn->query($sql);
	$conn->query($sql2);
	$conn -> close();
?>