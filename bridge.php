<?php
//php bridge.php --target=/home/eko/htdocs/hello.php --method=GET --nama=eko --alamat=kalipuro
  //php bridge.php --target=/home/eko/htdocs/proses_login.php --method=POST --namauser=eko --pwd=123
  $target ="";
  $method = "";
  if($argv !==NULL) {
    foreach( $argv as $argument ) {
            if( $argument == $argv[ 0 ] ) continue;
            $pair = explode( "=", $argument );
            $variableName = substr( $pair[0], 2 );
            $variableValue = $pair[1];
            if($variableName == "target") {
                $target = $variableValue;
            } else if($variableName == "method") {
                $method = $variableValue;
            } else {
                if($method == "GET")
                    $_GET[$variableName] = $variableValue;
                else if ($method="POST")
                    $_POST[$variableName] = $variableValue;
            }
   
    }
    //echo $target;
    require_once($target); 
  }
 ?>