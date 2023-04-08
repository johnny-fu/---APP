<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$username=validate($_POST['name']);
	$account_owner=validate($_POST['account_owner']);
	$sql = "SELECT * FROM `profile` ORDER BY id_profile DESC LIMIT 0,1";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
	
	$arr = json_encode($output, JSON_UNESCAPED_UNICODE);
	$obj = json_decode($arr, JSON_UNESCAPED_UNICODE);
	$id_profile_php=$obj["0"]["id_profile"]+1;
	echo $id_profile_php;
	echo $client;
	$sql2="INSERT INTO `profile`(id_profile,account,name,pocket,account_owner,first_login) VALUES ('$id_profile_php','$username','user','0','$account_owner','0');";
	$result2 = $conn->query($sql2);
	if ($result2 == TRUE) {
	echo "succ";
	} else {
	echo "Error!";
	}
	$conn -> close();
?>