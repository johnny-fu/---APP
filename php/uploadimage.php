<?php
	
	//設定上傳檔案到那個目錄。
	$file_path = "C:\xampp\htdocs\login\upload";
	
	//上傳到目錄的檔案路徑及檔案名稱。
	$file_path = $file_path . basename( $_FILES['uploaded_file']['name']);
	
	//進行上傳的動作。
	if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
	   echo "success";
	} else{
	   echo "fail";
	}
?>