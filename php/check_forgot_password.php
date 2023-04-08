<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$username=validate($_POST['name']);
	$key=validate($_POST['key']);
	$sql="SELECT * FROM `profile` where account='$username' and forget_password = '$key'";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
    // 將資料陣列轉成 Json 並顯示在網頁上，並要求不把中文編成 UNICODE
    print(json_encode($output, JSON_UNESCAPED_UNICODE));
	$conn -> close();
	
?>