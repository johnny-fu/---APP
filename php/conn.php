<?php
	// 設定 MySQL 的連線資訊並開啟連線
    // 資料庫位置、使用者名稱、使用者密碼、資料庫名稱
    
	$conn = new mysqli("163.21.245.178", "newuser", "newuser", "app");
    
	if($conn->connect_error){
		die("failed".$conn->connect_error);
	}
?>