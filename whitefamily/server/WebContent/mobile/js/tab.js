// JavaScript Document

function selectTag(showContent,selfObj){
	// ������ǩ
	var tag = document.getElementById("paixu").getElementsByTagName("a");
	var taglength = tag.length;
	for(i=0; i<taglength; i++){
		tag[i].className = "";
	}
	selfObj.className = "selectTag";
	// ��������
	for(i=0; j=document.getElementById("c"+i); i++){
		j.style.display = "none";
	}
	document.getElementById(showContent).style.display = "block";
	
	
}