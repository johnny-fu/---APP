<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$id=validate($_POST['id']);
	$sql="SELECT * FROM `transaction` where id_transaction='$id';";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
	$arr = json_encode($output, JSON_UNESCAPED_UNICODE);
	$obj = json_decode($arr, JSON_UNESCAPED_UNICODE);
	$client=$obj["0"]["client"];
	$expend=$obj["0"]["expend"];
	$sql2="SELECT * FROM `profile` where account='$client'";
	$result2 = $conn->query($sql2);
	while ($row1 = $result2->fetch_assoc()) // 當該指令執行有回傳
    {
        $output2[] = $row1; // 就逐項將回傳的東西放到陣列中
    }
	$arr2 = json_encode($output2, JSON_UNESCAPED_UNICODE);
	$obj2 = json_decode($arr2, JSON_UNESCAPED_UNICODE);
	$pocket=$obj2["0"]["pocket"]+$expend;
	$sql3="UPDATE `profile` SET pocket='$pocket' where account='$client';";
	$conn->query($sql3);
	$sql4="DELETE FROM `transaction` where id_transaction='$id';";
	$result4 = $conn->query($sql4);
	$conn -> close();
?>