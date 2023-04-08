<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$titles=validate($_POST['titles']);
	$money=validate($_POST['money']);
	$subtitles=validate($_POST['subtitles']);
	$image=$_POST['image'];
	$sql = "SELECT * FROM `meal` ORDER BY id_meal DESC LIMIT 0,1";
	$result = $conn->query($sql);
	while ($row = $result->fetch_assoc()) // 當該指令執行有回傳
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
	if(!file_exists($target_dir)){
		mkdir($target_dir,0777,true);
	}
	$target_dir="C:/xampp/htdocs/login/images";   
	$imagename=rand() . "_" . time() . ".png";
	$target_dir = $target_dir . "/". $imagename;
	if(file_put_contents($target_dir,base64_decode($image))){
		echo json_encode([
			"message" => "the file has been uploaded.",
			"status" => "ok"
		]);
		$arr = json_encode($output, JSON_UNESCAPED_UNICODE);
		$obj = json_decode($arr, JSON_UNESCAPED_UNICODE);
		$id_meal=$obj["0"]["id_meal"]+1;
		$sql2="INSERT INTO `meal`(id_meal,name,detail,price,image) VALUES ('$id_meal','$titles','$subtitles','$money','$imagename');";
		$result2 = $conn->query($sql2);
		if ($result2 == TRUE) {
		echo "succ";
		} else {
		echo "Error!";
		}
	}
	else {
		echo json_encode([
			"message" => "sorry",
			"status" => "error"
		]);
	}
	
	$conn -> close();
?>