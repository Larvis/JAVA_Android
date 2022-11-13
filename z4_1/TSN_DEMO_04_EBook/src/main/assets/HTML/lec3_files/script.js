menu.onclick = function myFunction() {
  var x = document.getElementById("myTopnav");
  if (x.className === "top_nav") {
    x.className += "  responsive";
  } else {
    x.className = "top_nav";
  }
    var x = document.getElementById("mask"); // для затемнения контента
  if (x.className === "main") {
    x.className += "  responsive";
  } else {
    x.className = "main";
  }
}

menu2.onclick = function myFunction() {
  var x = document.getElementById("myPravnav");
  if (x.className === "pravoe_menu") {
    x.className += "  responsive";
  } else {
    x.className = "pravoe_menu";
  }
  var x = document.getElementById("mask2"); // для затемнения контента
  if (x.className === "d") {
    x.className += "  responsive";
  } else {
    x.className = "d";
  }
}

menu3.onclick = function myFunction() {
  var x = document.getElementById("myPoisk");
  if (x.className === "poisk") {
    x.className += "  responsive";
  } else {
    x.className = "poisk";
  }
}

  // замена лупы на close
 var x=false
  function imgchange(obj,imgX,imgY) {
   if  (x){
   obj.src=imgX
   } else {
   obj.src=imgY
   }
  x=!x
 }
 
	
 // предзагрузка
  var img1=new Image(); 
img1.src='https://niva-chevy.ru/jpg-oform/close.png'; 