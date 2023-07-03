<?php 
include "header.php";
include "../sqlconnection.php";

if(isset($_GET['onay_kutusu'])){

  $ID = $_GET['onay_kutusu'];
  $UPDATE = $conn->prepare("UPDATE users SET ACTİVE = 1 WHERE ID = ?");
  $UPDATE->execute([$ID]);
  header('location:records.php');
}

?>
<table class="table">
  <thead>
    <tr>
      <th scope="col">TC</th>
      <th scope="col">EMAİL</th>
      <th scope="col">Rolü</th>
      <th scope="col">Öğreni Belgesi</th>
      <th scope="col">Onay Durumu</th>
    </tr>
  </thead>
  <tbody>
  <?php
            $select_products = $conn->prepare("SELECT * FROM `users`");
            $select_products->execute();
            if($select_products->rowCount() > 0){
               while($fetch_products = $select_products->fetch(PDO::FETCH_ASSOC)){ 
      
            ?>
    <tr>
      <th scope="row"><?= $fetch_products['TC']; ?></th>
      <td><?= $fetch_products['EMAİL']; ?></td>
      <td>
        <?php
        if ($fetch_products['ROLE'] == "0") {
          ECHO 'ADMİN';
        }elseif ($fetch_products['ROLE'] == "1") {
          ECHO 'Öğrenci';
        }elseif ($fetch_products['ROLE'] == "2") {
          ECHO 'Bağışçı';
        }
        ?>
      </td>
      <td>
<a class="btn btn-primary" href="../img/<?= $fetch_products['DOC']; ?>.txt" download="<?= $fetch_products['DOC']; ?>.txt"> Belgeyi İndir </a>

     </td>
     <td>
<a <?php echo ($fetch_products['ACTİVE'] == "1") ? 'class="btn btn-success"' : 'class="btn btn-danger"'  ?> href="records.php?onay_kutusu=<?= $fetch_products['ID']; ?>" > AKTİF ET </a>
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