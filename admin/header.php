
<?php
session_start();
if(isset($_SESSION['admin_id'])){
	$admin_id = $_SESSION['admin_id'];
 }else{
	$admin_id = '';
	header('location:login.php');
 };
?>
<!doctype html>
<html lang="en">
  <head>
  	<title>SDS</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900" rel="stylesheet">
		
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="css/style.css">

		<script>
function sendData(id) {
  var selectedOption = document.getElementById("myDropdown_" + id).value;
  
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "process.php", true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  xhr.onreadystatechange = function() {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        // İşlem başarılı, yanıtı burada işleyebilirsiniz
        console.log(xhr.responseText);
      } else {
        // İşlem başarısız
        console.error("Bir hata oluştu.");
      }
    }
  };
  
  var data = "selectedOption=" + encodeURIComponent(selectedOption) + "&id=" + id;
  xhr.send(data);
}
</script>

  </head>
  <body>
		
  <div class="wrapper d-flex align-items-stretch">
			<nav id="sidebar" class="bg-success">
				<div class="custom-menu">
					<button type="button" id="sidebarCollapse" class="btn btn-success">
	          <i class="fa fa-bars"></i>
	          <span class="sr-only">Toggle Menu</span>
	        </button>
        </div>
				<div class="p-4 pt-5 ">
		  		<h1><a href="index.php" class="logo">SDS</a></h1>
	        <ul class="list-unstyled components mb-5">
	          <li>
	              <a href="index.php">Kullanıcılar</a>
	          </li>
	          <li>
              <a href="records.php">Kayıt İstekleri</a>
	          </li>
	          <li>
              <a href="donation_requests.php">Bağış İstekleri</a>
	          </li>
			  <li>
              <a href="all_donates.php">Tüm Bağışlar</a>
	          </li>
			  <li>
              <a href="admin_logout.php">Çıkış Yap</a>
	          </li>
	        </ul>

	        

	       

	      </div>
    	</nav>

        <!-- Page Content  -->
      <div id="content" class="p-4 p-md-5 pt-5">

        