<?php
	require_once "conn.php";
	require_once "vaildate.php";
	$k=$_POST['k'];
	$judge=validate($_POST['judge']);
	$id_meal=validate($_POST['id_meal']);
	echo $id_meal;
	if($judge=='delete'){
		$sql="DELETE FROM `meal` where id_meal='$id_meal';";
	}
	else if($judge=='reverse_name'){
		$sql="UPDATE `meal` SET name ='$k' WHERE id_meal='$id_meal';";
	}
	else if($judge=='reverse_detail'){
		$sql="UPDATE `meal` SET detail ='$k' WHERE id_meal='$id_meal';";
	}
	else if($judge=='reverse_price'){
		$sql="UPDATE `meal` SET price ='$k' WHERE id_meal='$id_meal';";
	}
	else if($judge=='reverse_images'){
		$target_dir="C:/xampp/htdocs/login/images";   
		$imagename=rand() . "_" . time() . ".png";
		$target_dir = $target_dir . "/". $imagename;
		if(file_put_contents($target_dir,base64_decode($k))){
			$sql="UPDATE `meal` SET image ='$imagename' WHERE id_meal='$id_meal';";
		}
	}
	$result = $conn->query($sql);
	$conn -> close();
?>