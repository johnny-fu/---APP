<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$judge_t=validate($_POST['judge_t']);
	$judge_d=validate($_POST['judge_d']);
	$day=validate($_POST['day']);
	$sql="SELECT * FROM `transaction` where date ='$day' ORDER BY date DESC LIMIT 20;";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
	
	print(json_encode($output, JSON_UNESCAPED_UNICODE));
	$conn -> close();
?>