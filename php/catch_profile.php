<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$username=validate($_POST['name']);
	$password=validate($_POST['pass']);
	$sql="SELECT pocket,message_money FROM `profile` where account='$username' and password='$password'";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
    // 將資料陣列轉成 Json 並顯示在網頁上，並要求不把中文編成 UNICODE
    print(json_encode($output, JSON_UNESCAPED_UNICODE));
	/*if($result->fetch_assoc()){
		echo "succ";
		
	}else{
		echo "fail";
	}*/
	$conn -> close();
	

?>