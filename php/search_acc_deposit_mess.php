<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$name=validate($_POST['name']);
	$sql ="SELECT * FROM `deposit` where account = '$name' order by date desc, id_deposit desc limit 2;";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
	{
		$output[] = $row; // 就逐項將回傳的東西放到陣列中
	}
	print(json_encode($output, JSON_UNESCAPED_UNICODE));
	$conn -> close();
?>