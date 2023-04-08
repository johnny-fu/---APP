<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$client=validate($_POST['client']);
	$expend=validate($_POST['expend']);
	$transaction_message=validate($_POST['transaction_message']);
	$date=validate($_POST['date']);
	$order_date=validate($_POST['order_date']);
	$sql = "SELECT * FROM `transaction` ORDER BY id_transaction DESC LIMIT 0,1";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
	
	$arr = json_encode($output, JSON_UNESCAPED_UNICODE);
	$obj = json_decode($arr, JSON_UNESCAPED_UNICODE);
	$id_transaction_php=$obj["0"]["id_transaction"]+1;
	echo $client;
	$sql2="INSERT INTO `transaction`(id_transaction,retailer,client,expend,transaction_message,date,order_date) VALUES ('$id_transaction_php','not','$client','$expend','$transaction_message','$date','$order_date');";
	$result2 = $conn->query($sql2);
	if ($result2 === TRUE) {
	echo "succ";
	} else {
	echo "Error!";
	}
	$conn -> close();
?>