<?php include "header.php"?>
<div class="container-xxl bg-light my-5 mx-auto py-5">
<table class="table">
  <thead class="thead-dark">
    <tr>
      <th scope="col">Başlık</th>
      <th scope="col">Mesaj</th>
      <th scope="col">İstenilen Miktar</th>
      <th scope="col">Durumu</th>
      <th scope="col">Bağış Durumu</th>
    </tr>
  </thead>
  <tbody>
  <?php
  $ID = $_SESSION['USER_ID'];
            $select_products = $conn->prepare("SELECT * FROM `request` WHERE USER_ID = ?");
            $select_products->execute([$ID]);
            if($select_products->rowCount() > 0){
               while($fetch_products = $select_products->fetch(PDO::FETCH_ASSOC)){ 
      
            ?>
    <tr>
      <th scope="row"><?= $fetch_products['HEADER']; ?></th>
      <td><?= $fetch_products['MESSAGE']; ?></td>
      <td><?= $fetch_products['DONOR_PAY']; ?></td>
      <td><?php echo ($fetch_products['APPROVAL_STATUS'] = 1) ? "Aktif" : "Pasif";?></td>
      <td><?php echo ($fetch_products['PAYMENT_STATUS'] = 1) ? "Bekliyor" : "Bağış Alındı";?></td>
    </tr>
    <?php
         }
      }
   ?>
  </tbody>
</table>


<?php include "footer.php"?>