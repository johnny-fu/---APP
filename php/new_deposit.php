<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$date=validate($_POST['date']);
	$name=validate($_POST['name']);
	$stored_money=validate($_POST['stored_money']);
	$account=validate($_POST['account']);
	$account_retailer=validate($_POST['account_retailer']);
	$sql = "SELECT * FROM `deposit` ORDER BY id_deposit DESC LIMIT 0,1";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
	
	$arr = json_encode($output, JSON_UNESCAPED_UNICODE);
	$obj = json_decode($arr, JSON_UNESCAPED_UNICODE);
	$id_deposit=$obj["0"]["id_deposit"]+1;
	echo $id_deposit;
	$sql2 = "INSERT INTO `deposit`(id_deposit,date,name,stored_money,account,account_retailer) VALUES ('$id_deposit','$date','$name','$stored_money','$account','$account_retailer');";
	$result2 = $conn->query($sql2);
	if ($result2 == TRUE) {
	echo "succ";
	} else {
	echo "Error!";
	}
	$conn -> close();
?>