<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$name=validate($_POST['name']);
	$search=validate($_POST['search']);
	
	$sql= "SELECT * FROM `profile` WHERE account LIKE '%$search%' AND account_owner='$name';";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
	
	print(json_encode($output, JSON_UNESCAPED_UNICODE));
	$conn -> close();
?>