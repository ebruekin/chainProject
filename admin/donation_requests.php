<?php 
include "header.php";
include "../sqlconnection.php";

if(isset($_GET['onay_kutusu'])){
    $ID = $_GET['onay_kutusu'];
    $UPDATE = $conn->prepare("UPDATE request SET APPROVAL_STATUS = 1 WHERE ID = ?");
    $UPDATE->execute([$ID]);
    header('location:donation_requests.php');
  }

  if(isset($_GET['bagis_kutusu'])){
    $ID = $_GET['bagis_kutusu'];
    $UPDATE = $conn->prepare("UPDATE request SET PAYMENT_STATUS = 1 WHERE ID = ?");
    $UPDATE->execute([$ID]);
    header('location:donation_requests.php');
  }

?>
<table class="table">
  <thead>
    <tr>
      <th scope="col">EMAİL</th>
      <th scope="col">Başlık</th>
      <th scope="col">Mesajı</th>
      <th scope="col">Bağış Miktarı</th>
      <th scope="col">Bağış Durumu</th>
      <th scope="col">Bağış Ödeme Durumu</th>
    </tr>
  </thead>
  <tbody>
  <?php
            $select_products = $conn->prepare("SELECT  *, request.ID reqID FROM request inner join users on users.ID = request.USER_ID");
            $select_products->execute();
            if($select_products->rowCount() > 0){
               while($fetch_products = $select_products->fetch(PDO::FETCH_ASSOC)){ 
      
            ?>
    <tr>
      <th scope="row"><?= $fetch_products['EMAİL']; ?></th>
      <td><?= $fetch_products['HEADER']; ?></td>
       <th scope="row"><?= $fetch_products['MESSAGE']; ?></th>
      <td><?= $fetch_products['DONOR_PAY']; ?></td>
      <td>
<a <?php echo ($fetch_products['APPROVAL_STATUS'] == "1") ? 'class="btn btn-success"' : 'class="btn btn-danger"' ?> href="donation_requests.php?onay_kutusu=<?= $fetch_products['reqID']; ?>" > AKTİF ET </a>
     </td>
     <td>
<a <?php echo ($fetch_products['PAYMENT_STATUS'] == "1") ? 'class="btn btn-success"' : 'class="btn btn-danger"' ?> href="donation_requests.php?bagis_kutusu=<?= $fetch_products['reqID']; ?>" > Ödeme Durumu </a>
     </td>
    </tr>
    

    <?php
         }
      }else{
         echo '<p class="empty">no products added yet!</p>';
      }
   ?>
  </tbody>
</table>

<?php 
include "footer.php";
?>